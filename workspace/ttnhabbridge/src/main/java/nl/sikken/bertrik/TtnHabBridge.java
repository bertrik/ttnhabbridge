package nl.sikken.bertrik;

import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.Sentence;
import nl.sikken.bertrik.hab.SodaqOnePayload;
import nl.sikken.bertrik.hab.habitat.HabReceiver;
import nl.sikken.bertrik.hab.habitat.HabitatUploader;
import nl.sikken.bertrik.hab.habitat.IHabitatRestApi;
import nl.sikken.bertrik.hab.habitat.Location;
import nl.sikken.bertrik.hab.ttn.TtnListener;
import nl.sikken.bertrik.hab.ttn.TtnMessage;
import nl.sikken.bertrik.hab.ttn.TtnMessageGateway;

/**
 * Bridge between the-things-network and the habhub network.
 * 
 * Possible improvements: 
 * - add uncaught exception handler 
 */
public final class TtnHabBridge {

    private static final Logger LOG = LoggerFactory.getLogger(TtnHabBridge.class);
    private static final String CONFIG_FILE = "ttnhabbridge.properties";

    private final TtnListener ttnListener;
    private final HabitatUploader habUploader;
    private final ObjectMapper mapper;

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
        this.ttnListener = new TtnListener(this::handleTTNMessage, 
                                           config.getTtnMqttUrl(), config.getTtnAppId(), config.getTtnAppKey());
        final IHabitatRestApi restApi = 
                HabitatUploader.newRestClient(config.getHabitatUrl(), config.getHabitatTimeout());
        this.habUploader = new HabitatUploader(restApi);
        this.mapper = new ObjectMapper();
    }

    /**
     * Starts the application.
     * 
     * @throws MqttException in case of a problem starting MQTT client
     */
    private void start() throws MqttException {
        LOG.info("Starting TTN-HAB bridge application");

        // start sub-modules
        habUploader.start();
        ttnListener.start();

        LOG.info("Started TTN-HAB bridge application");
    }

    /**
     * Handles an incoming TTN message
     * 
     * @param topic the topic on which the message was received
     * @param message the message contents
     */
    private void handleTTNMessage(String topic, String message) {
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
                final HabReceiver receiver = new HabReceiver(gw.getId(),
                        new Location(gw.getLatitude(), gw.getLongitude(), gw.getAltitude()));
                receivers.add(receiver);
            }

            // send listener data
            for (HabReceiver receiver : receivers) {
                habUploader.scheduleListenerDataUpload(receiver, now);
            }

            // send payload telemetry data
            habUploader.schedulePayloadTelemetryUpload(line, receivers, now);
        } catch (IOException e) {
            LOG.warn("JSON unmarshalling exception '{}' for {}", e.getMessage(), message);
        } catch (BufferUnderflowException e) {
            LOG.warn("Sodaq payload exception: {}", e.getMessage());
        }
    }

    /**
     * Stops the application.
     * 
     * @throws MqttException
     */
    private void stop() {
        LOG.info("Stopping TTN HAB bridge application");
        ttnListener.stop();
        habUploader.stop();
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
