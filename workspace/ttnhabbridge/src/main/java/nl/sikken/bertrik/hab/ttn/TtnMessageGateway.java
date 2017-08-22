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
    private double latitude;
    
    @JsonProperty("longitude")
    private double longitude;
    
    @JsonProperty("altitude")
    private double altitude;

    public String getId() {
        return id;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }
    
}
