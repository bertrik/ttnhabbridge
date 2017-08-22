package nl.sikken.bertrik.hab.ttn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a message received from the TTN MQTT stream.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MqttData {

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("dev_id")
    private String devId;

    @JsonProperty("hardware_serial")
    private String hardwareSerial;

    @JsonProperty("port")
    private int port;

    @JsonProperty("counter")
    private int counter;

    @JsonProperty("payload_raw")
    private byte[] payload;

    @JsonProperty("metadata")
    private MqttMetaData metaData;

    public String getAppId() {
        return appId;
    }

    public String getDevId() {
        return devId;
    }

    public String getHardwareSerial() {
        return hardwareSerial;
    }

    public int getPort() {
        return port;
    }

    public int getCounter() {
        return counter;
    }

    public byte[] getPayload() {
        return payload;
    }

    public MqttMetaData getMetaData() {
        return metaData;
    }

}
