package nl.sikken.bertrik.hab.ttn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a gateway in the metadata of the TTN MQTT JSON format.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TtnMessageGateway {

    @JsonProperty("gtw_id")
    private String id;
    
    @JsonProperty("gtw_trusted")
    private boolean trusted;
    
    @JsonProperty("time")
    private String time;
    
    @JsonProperty("latitude")
    private Double latitude;
    
    @JsonProperty("longitude")
    private Double longitude;
    
    @JsonProperty("altitude")
    private Double altitude;

    public String getId() {
        return id;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public String getTime() {
        return time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }
    
}
