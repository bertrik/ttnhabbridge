package nl.sikken.bertrik.hab.ttn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nl.sikken.bertrik.hab.habitat.Location;

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

    private TtnMessageGateway() {
        // jackson constructor
    }
    
    /**
     * Constructor.
     * 
     * @param id the id (name)
     * @param trusted whether it is trusted
     * @param time the time
     * @param lat the latitude (degrees)
     * @param lon the longitude (degrees)
     * @param alt the altitude (meters)
     */
    public TtnMessageGateway(String id, boolean trusted, String time, Double lat, Double lon, Double alt) {
        this();
        this.id = id;
        this.trusted = trusted;
        this.time = time;
        this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
    }
    
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
    
    @JsonIgnore
    public Location getLocation() {
        return new Location(latitude, longitude, altitude);
    }
    
}
