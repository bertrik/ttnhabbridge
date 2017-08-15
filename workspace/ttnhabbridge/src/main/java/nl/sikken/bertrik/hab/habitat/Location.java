package nl.sikken.bertrik.hab.habitat;

/**
 * Representation of a HAB receiver location.
 */
public final class Location {

    private final double lat;
    private final double lon;
    private final double alt;

    /**
     * Constructor.
     * 
     * @param lat latitude (degrees)
     * @param lon longitude (degrees)
     * @param alt altitude (meter)
     */
    public Location(double lat, double lon, double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getAlt() {
        return alt;
    }

}
