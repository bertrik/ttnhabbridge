package nl.sikken.bertrik.hab;

/**
 * Object describing a HAB receiver.
 */
public final class HabReceiver {
    
    private final String callSign;
    private final Location location;
    private final String network;
    
    /**
     * Constructor.
     * 
     * @param callSign the call sign
     * @param location the location
     */
    public HabReceiver(String callSign, Location location) {
        this(callSign, location, "LoRaWAN");
    }

    public HabReceiver(String callSign, Location location, String network) {
        this.callSign = callSign;
        this.location = location;
        this.network = network;
    }
    
    public String getCallsign() {
        return callSign;
    }

    public Location getLocation() {
        return location;
    }
    
    public String getNetwork() {
        return network;
    }

    @Override
    public String toString() {
        return callSign;
    }
    
}
