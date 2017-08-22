package nl.sikken.bertrik;

import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.Sentence;
import nl.sikken.bertrik.hab.SodaqOnePayload;
import nl.sikken.bertrik.hab.habitat.HabReceiver;
import nl.sikken.bertrik.hab.habitat.HabitatUploader;
import nl.sikken.bertrik.hab.habitat.IHabitatRestApi;
import nl.sikken.bertrik.hab.habitat.Location;
import nl.sikken.bertrik.hab.ttn.TtnMessage;
import nl.sikken.bertrik.hab.ttn.TtnMessageGateway;

/**
 * Bridge between the-things-network and the habhub network.
 * 
 * Possible improvements:
 * - put the MQTT functionality in its own module
 * - add uncaught exception handler
 * - add example systemd startup scripts
 */
public final class TtnHabBridge {

    private static final Logger LOG = LoggerFactory.getLogger(TtnHabBridge.class);
    private static final String CONFIG_FILE = "ttnhabbridge.properties";
    
    private final ITtnHabBridgeConfig config;
    private final HabitatUploader uploader;
    private final ObjectMapper mapper;
    
    private MqttClient mqttClient;
    
    /**
     * Main application entry point.
     * 
     * @param arguments application arguments (none taken)
     * @throws IOException in case of a problem reading a config file 
     * @throws MqttException in case of a problem starting MQTT client
     */
    public static void main(String[] arguments) throws IOException, MqttException {
        final ITtnHabBridgeConfig config = readConfig(new File(CONFIG_FILE));
        final TtnHabBridge app = new TtnHabBridge(config);
        
        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    /**
     * Constructor.
     * 
     * @param config the application configuration
     */
    private TtnHabBridge(ITtnHabBridgeConfig config) {
        this.config = config;

        final IHabitatRestApi restApi = HabitatUploader.newRestClient(config.getHabitatUrl(), config.getHabitatTimeout());
        this.uploader = new HabitatUploader(restApi);
        this.mapper = new ObjectMapper();
    }
    
    /**
     * Starts the application.
     * @throws MqttException in case of a problem starting MQTT client
     */
    private void start() throws MqttException {
        LOG.info("Starting TTN-HAB bridge application");
        
        // start sub-modules
        uploader.start();
        
        // start MQTT client
        this.mqttClient = new MqttClient(config.getMqttServerUrl(), config.getMqttClientId());
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(config.getMqttUserName());
        options.setPassword(config.getMqttPassword());
        options.setAutomaticReconnect(true);
        mqttClient.connect(options);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                final String payload = new String(message.getPayload(), StandardCharsets.US_ASCII);
                LOG.info("Message arrived on topic {}: {}", topic, payload);
                try {
                    handleMessageArrived(topic, payload);
                } catch (Exception e) {
                    LOG.info("Exception in message handling: {}", e.getMessage());
                }
            }
            
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                LOG.info("deliveryComplete");
                // we don't care
            }
            
            @Override
            public void connectionLost(Throwable cause) {
                LOG.info("connectionLost: {}", cause.getMessage());
            }
        });
        mqttClient.subscribe(config.getMqttTopic());
        
        LOG.info("Started TTN-HAB bridge application");
    }
    
    private void handleMessageArrived(String topic, String message) {
        final Date now = new Date();

        try {
            // try to decode the payload
            final TtnMessage data = mapper.readValue(message, TtnMessage.class);
            
            final SodaqOnePayload sodaq = SodaqOnePayload.parse(data.getPayload());
            LOG.info("Got SODAQ message: {}", sodaq);
            
            final String callSign = data.getDevId();
            final int id = data.getCounter();
            final double latitude = sodaq.getLatitude();
            final double longitude = sodaq.getLongitude();
            final double altitude = sodaq.getAltitude();
            
            // get the payload coordinates and construct a sentence
            final Sentence sentence = new Sentence(callSign, id, now, latitude, longitude, altitude);
            final String line = sentence.format();
    
            // create listeners
            final List<HabReceiver> receivers = new ArrayList<>();
            for (TtnMessageGateway gw : data.getMetaData().getMqttGateways()) {
                final HabReceiver receiver = 
                        new HabReceiver(gw.getId(), new Location(gw.getLatitude(), gw.getLongitude(), gw.getAltitude()));
                receivers.add(receiver);
            }
    
            // send listener data
            for (HabReceiver receiver : receivers) {
                uploader.scheduleListenerDataUpload(receiver, now);
            }
            
            // send payload telemetry data
            uploader.schedulePayloadTelemetryUpload(line, receivers, now);
        } catch (IOException e) {
            LOG.warn("JSON unmarshalling exception '{}' for {}", e.getMessage(), message);
        } catch (BufferUnderflowException e) {
            LOG.warn("Sodaq payload exception: {}", e.getMessage());
        }
    }

    /**
     * Stops the application.
     * @throws MqttException 
     */
    private void stop() {
        LOG.info("Stopping TTN HAB bridge application");
        try {
            mqttClient.close();
        } catch (MqttException e) {
            // what can we do about this?
            LOG.warn("Error closing MQTT client: {}", e.getMessage());
        }
        uploader.stop();
    }

    private static ITtnHabBridgeConfig readConfig(File file) throws IOException {
        final TtnHabBridgeConfig config = new TtnHabBridgeConfig();
        try {
            config.load(file);
        } catch (IOException e) {
            LOG.info("Failed to load config {}, writing defaults", file.getAbsoluteFile());
            config.save(file);
        }
        return config;
    }

}
