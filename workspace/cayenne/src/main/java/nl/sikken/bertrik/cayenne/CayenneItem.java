package nl.sikken.bertrik.cayenne;

import java.util.Arrays;
import java.util.Locale;

/**
 * Representation of one measurement item in a cayenne message.
 */
public final class CayenneItem {
    
    private final int channel;
    private final ECayenneItem type;
    private final String[] value;

    /**
     * Constructor.
     * 
     * @param channel the unique channel
     * @param type the type
     * @param value the (string) value
     */
    public CayenneItem(int channel, ECayenneItem type, String[] value) {
        this.channel = channel;
        this.type = type;
        this.value = value;
    }

    public int getChannel() {
        return channel;
    }

    public ECayenneItem getType() {
        return type;
    }

    public String[] getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "{chan=%d,type=%s,value=%s}", channel, type, Arrays.toString(value));  
    }
    
}
