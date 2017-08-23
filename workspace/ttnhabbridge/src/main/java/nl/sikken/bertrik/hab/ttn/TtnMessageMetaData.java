package nl.sikken.bertrik.hab.ttn;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of meta-data part of MQTT message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TtnMessageMetaData {

    @JsonProperty("time")
    private String time;
    
    @JsonProperty("gateways")
    private List<TtnMessageGateway> gateways;
    
    private TtnMessageMetaData() {
        // empty jackson constructor
    }
    
    /**
     * Constructor.
     * 
     * @param time the time
     * @param gateways list of gateways
     */
    public TtnMessageMetaData(String time, List<TtnMessageGateway> gateways) {
        this();
        this.time = time;
        this.gateways = gateways;
    }

    public Instant getTime() {
        return Instant.parse(time);
    }
    
    public List<TtnMessageGateway> getMqttGateways() {
        return gateways;
    }

}
