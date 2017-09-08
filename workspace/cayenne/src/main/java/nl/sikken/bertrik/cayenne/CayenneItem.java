package nl.sikken.bertrik.cayenne;

import java.util.Arrays;
import java.util.Locale;

/**
 * Representation of one measurement item in a cayenne message.
 */
public final class CayenneItem {
    
    private final int channel;
    private final ECayenneItem type;
    private final Double[] values;

    /**
     * Constructor.
     * 
     * @param channel the unique channel
     * @param type the type
     * @param values the values
     */
    public CayenneItem(int channel, ECayenneItem type, Double[] values) {
        this.channel = channel;
        this.type = type;
        this.values = values;
    }

    public int getChannel() {
        return channel;
    }

    public ECayenneItem getType() {
        return type;
    }

    public Double[] getValues() {
        return values;
    }
    
    public String[] format() {
        return type.format(values);
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "{chan=%d,type=%s,value=%s}", channel, type, Arrays.toString(format()));  
    }
    
}
