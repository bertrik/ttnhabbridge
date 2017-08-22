package nl.sikken.bertrik.hab.habitat;

/**
 * Object describing a HAB receiver.
 */
public final class HabReceiver {
    
    private final String callSign;
    private final Location location;
    
    /**
     * Constructor.
     * 
     * @param callSign the call sign
     * @param location the location
     */
    public HabReceiver(String callSign, Location location) {
        this.callSign = callSign;
        this.location = location;
    }
    
    public String getCallsign() {
        return callSign;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return callSign;
    }
    
}
