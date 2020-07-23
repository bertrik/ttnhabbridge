package nl.sikken.bertrik.hab.ttn;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a message received from the TTN MQTT stream.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TtnMessage {

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
    private byte[] payloadRaw = new byte[0];

    @JsonProperty("payload_fields")
    private Map<String, String> payloadFields = new HashMap<>();
    
    @JsonProperty("metadata")
    private TtnMessageMetaData metaData;

    private TtnMessage() {
    	// Jackson constructor
    }
    
    // constructor for testing
    public TtnMessage(String devId, int counter, TtnMessageMetaData metaData, byte[] payloadRaw) {
    	this();
    	this.devId = devId;
    	this.counter = counter;
    	this.metaData = metaData;
    	this.payloadRaw = payloadRaw.clone();
    }
    
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

    public byte[] getPayloadRaw() {
        return payloadRaw.clone();
    }

    public Map<String, String> getPayloadFields() {
        return payloadFields;
    }

    public TtnMessageMetaData getMetaData() {
        return metaData;
    }

}
