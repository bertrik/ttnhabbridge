package nl.sikken.bertrik.hab.ttn;

import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Listener process for receiving data from the TTN.
 */
public final class TtnListener {

    private static final Logger LOG = LoggerFactory.getLogger(TtnListener.class);
    private static final long DISCONNECT_TIMEOUT_MS = 3000;

    private final IMessageReceived callback;
    private final MqttClient mqttClient;
    private final MqttConnectOptions options;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor.
     * 
     * @param callback the listener for a received message.
     * @param url      the URL of the MQTT server
     * @param appId    the user name
     * @param appKey   the password
     */
    public TtnListener(IMessageReceived callback, String url, String appId, String appKey) {
        LOG.info("Creating client for MQTT server '{}' for app '{}'", url, appId);
        try {
            this.mqttClient = new MqttClient(url, MqttClient.generateClientId(), new MemoryPersistence());
        } catch (MqttException e) {
            throw new IllegalArgumentException(e);
        }
        this.callback = callback;
        mqttClient.setCallback(new MqttCallbackHandler(mqttClient, "+/devices/+/up", this::handleMessage));

        // create connect options
        options = new MqttConnectOptions();
        options.setUserName(appId);
        options.setPassword(appKey.toCharArray());
        options.setAutomaticReconnect(true);
    }

    // notify our caller in a thread safe manner
    private void handleMessage(String topic, String payload) {
        try {
            TtnUplinkMessage uplinkMessage = convertMessage(topic, payload);
            callback.messageReceived(uplinkMessage);
        } catch (JsonProcessingException e) {
            LOG.warn("Caught {}", e.getMessage());
        } catch (Throwable e) {
            // safety net
            LOG.error("Caught unhandled throwable", e);
        }
    }

    // package private for testing
    TtnUplinkMessage convertMessage(String topic, String payload) throws JsonProcessingException {
        if (topic.startsWith("v3/")) {
            Ttnv3UplinkMessage v3message = objectMapper.readValue(payload, Ttnv3UplinkMessage.class);
            return v3message.toUplinkMessage();
        } else {
            Ttnv2UplinkMessage v2message = objectMapper.readValue(payload, Ttnv2UplinkMessage.class);
            return v2message.toUplinkMessage();
        }
    }

    /**
     * Starts this module.
     * 
     * @throws MqttException in case something went wrong with MQTT
     */
    public void start() throws MqttException {
        LOG.info("Starting MQTT listener");

        LOG.info("Connecting to MQTT server");
        mqttClient.connect(options);
    }

    public void stop() {
        LOG.info("Stopping MQTT listener");
        try {
            mqttClient.disconnect(DISCONNECT_TIMEOUT_MS);
        } catch (MqttException e) {
            // don't care, just log
            LOG.warn("Caught exception on disconnect: {}", e.getMessage());
        }
    }

    /**
     * MQTT callback handler, (re-)subscribes to the topic and forwards incoming
     * messages.
     */
    private static final class MqttCallbackHandler implements MqttCallbackExtended {

        private final MqttClient client;
        private final String topic;
        private final IMqttMessageArrived listener;

        private MqttCallbackHandler(MqttClient client, String topic, IMqttMessageArrived listener) {
            this.client = client;
            this.topic = topic;
            this.listener = listener;
        }

        @Override
        public void connectionLost(Throwable cause) {
            LOG.warn("Connection lost: {}", cause.getMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            LOG.info("Message arrived on topic '{}'", topic);

            // notify our listener, in an exception safe manner
            try {
                String json = new String(mqttMessage.getPayload(), StandardCharsets.US_ASCII);
                listener.messageArrived(topic, json);
            } catch (Exception e) {
                LOG.trace("Caught exception", e);
                LOG.error("Caught exception in MQTT listener: {}", e.getMessage());
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // nothing to do
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            LOG.info("Connected to '{}', subscribing to MQTT topic '{}'", serverURI, topic);
            try {
                client.subscribe(topic);
            } catch (MqttException e) {
                LOG.error("Caught exception while subscribing!");
            }
        }
    }

    interface IMqttMessageArrived {
        void messageArrived(String topic, String json);
    }

}
