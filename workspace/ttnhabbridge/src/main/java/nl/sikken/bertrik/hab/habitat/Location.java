package nl.sikken.bertrik.hab.habitat;

/**
 * Representation of a HAB receiver location.
 */
public final class Location {

    private final Double lat;
    private final Double lon;
    private final Double alt;

    /**
     * Constructor.
     * 
     * @param lat latitude (degrees)
     * @param lon longitude (degrees)
     * @param alt altitude (meter)
     */
    public Location(Double lat, Double lon, Double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getAlt() {
        return alt;
    }

    /**
     * @return true if this is a fully valid location
     */
    public boolean isValid() {
        return lat != null && lon != null && alt != null;
    }

}
