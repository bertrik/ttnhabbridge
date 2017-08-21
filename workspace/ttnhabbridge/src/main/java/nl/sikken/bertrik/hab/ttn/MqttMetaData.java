package nl.sikken.bertrik.hab.ttn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of meta-data part of MQTT message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MqttMetaData {

    private String time;
    
    @JsonProperty("gateways")
    private List<MqttGateway> gateways;
    
    public String getTime() {
        return time;
    }
    
    public List<MqttGateway> getMqttGateways() {
        return gateways;
    }

}
