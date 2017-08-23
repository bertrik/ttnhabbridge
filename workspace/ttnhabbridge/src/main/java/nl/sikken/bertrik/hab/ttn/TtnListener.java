package nl.sikken.bertrik.hab.ttn;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener process for receiving data from the TTN.
 */
public final class TtnListener {
    
    private static Logger LOG = LoggerFactory.getLogger(TtnListener.class);
    private static final long DISCONNECT_TIMEOUT_MS = 3000;
    
    private final String clientId;
    private final IMessageReceived callback;
    private final String url;
    private final String userName;
    private final String password;
    private final String topic;

    private MqttClient mqttClient;

    /**
     * Constructor.
     * 
     * @param receiveCallback the interface for indicating a received message.
     * @param url the URL of the MQTT server
     * @param userName the user name
     * @param password the password
     * @param topic the MQTT topic
     */
    public TtnListener(IMessageReceived receiveCallback, String url, String userName, String password, String topic) {
        this.callback = receiveCallback;
        this.url = url;
        this.clientId = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = password;
        this.topic = topic;
    }
    
    /**
     * Starts this module.
     * 
     * @throws MqttException 
     */
    public void start() throws MqttException {
        LOG.info("Starting TTN listener");
        
        // connect
        LOG.info("Connecting as user '{}' to MQTT server {}", userName, url);
        this.mqttClient = new MqttClient(url, clientId);
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        mqttClient.connect(options);
        
        // subscribe
        LOG.info("Subscribing to topic '{}'", topic);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                final String message = new String(mqttMessage.getPayload(), StandardCharsets.US_ASCII);
                LOG.info("Message arrived on topic {}: {}", topic, message);
                callback.messageReceived(topic, message);
            }
            
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // we don't care
            }
            
            @Override
            public void connectionLost(Throwable cause) {
                LOG.info("connectionLost: {}", cause.getMessage());
            }
        });
        mqttClient.subscribe(topic);

        LOG.info("Started TTN listener");
    }
    
    /**
     * Stops this module.
     */
    public void stop() {
        LOG.info("Stopping TTN listener");
        try {
            mqttClient.disconnect(DISCONNECT_TIMEOUT_MS);
            mqttClient.close();
        } catch (MqttException e) {
            // don't care, just log
            LOG.warn("Caught exception while shutting down", e.getMessage());
        }
        LOG.info("Stopped TTN listener");
    }
    
}
