package nl.sikken.bertrik.hab.ttn;

import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener process for receiving data from the TTN.
 */
public final class TtnListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(TtnListener.class);
    private static final long DISCONNECT_TIMEOUT_MS = 3000;
    
    private static final String TOPIC = "+/devices/+/up";
    
    private final String clientId;
    private final IMessageReceived callback;
    private final String url;
    private final String appId;
    private final String appKey;
    private final String topic;

    private MqttClient mqttClient;

    /**
     * Constructor.
     * 
     * @param receiveCallback the interface for indicating a received message.
     * @param url the URL of the MQTT server
     * @param appId the user name
     * @param appKey the password
     */
    public TtnListener(IMessageReceived receiveCallback, String url, String appId, String appKey) {
        this.callback = receiveCallback;
        this.url = url;
        this.clientId = MqttClient.generateClientId();
        this.appId = appId;
        this.appKey = appKey;
        this.topic = TOPIC;
    }
    
    /**
     * Starts this module.
     * 
     * @throws MqttException in case something went wrong with MQTT 
     */
    public void start() throws MqttException {
        LOG.info("Starting TTN listener");
        
        // connect
        LOG.info("Connecting as user '{}' to MQTT server {}", appId, url);
        this.mqttClient = new MqttClient(url, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(appId);
        options.setPassword(appKey.toCharArray());
        options.setAutomaticReconnect(false);
        mqttClient.connect(options);
        
        // subscribe
        LOG.info("Subscribing to topic '{}'", topic);
        mqttClient.subscribe(topic, this::messageArrived);

        LOG.info("Started TTN listener");
    }
    
    /**
     * Handles an incoming message.
     * 
     * @param topic the topic
     * @param mqttMessage the message
     * @throws Exception who knows?
     */
    void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload(), StandardCharsets.US_ASCII);
        LOG.info("Message arrived on topic '{}': {}", topic, message);
        // forward it to our user
        callback.messageReceived(topic, message);
    }
    
    /**
     * Stops this module.
     */
    public void stop() {
        LOG.info("Stopping TTN listener");
        try {
            mqttClient.disconnect(DISCONNECT_TIMEOUT_MS);
        } catch (MqttException e) {
            // don't care, just log
            LOG.warn("Caught exception on disconnect: {}", e.getMessage());
        } finally {
            try {
                mqttClient.close();
            } catch (MqttException e) {
                // don't care, just log
                LOG.warn("Caught exception on close: {}", e.getMessage());
            }
        }
        LOG.info("Stopped TTN listener");
    }
    
}
