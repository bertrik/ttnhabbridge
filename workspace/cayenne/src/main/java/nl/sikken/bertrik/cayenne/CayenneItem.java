package nl.sikken.bertrik.cayenne;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
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
    
    /**
     * Parses one item from the byte buffer and returns it.
     * 
     * @param bb the byte buffer
     * @return a new cayenne item
     * @throws CayenneException if an error occurs during parsing
     */
    public static CayenneItem parse(ByteBuffer bb) throws CayenneException {
        try {
            final int channel = bb.get();
            final int type = bb.get() & 0xFF;
            final ECayenneItem ct = ECayenneItem.parse(type);
            if (ct == null) {
                throw new CayenneException("Invalid cayenne type " + type);
            }
            final Double[] values = ct.parse(bb);
            return new CayenneItem(channel, ct, values);
        } catch (BufferUnderflowException e) {
            throw new CayenneException(e);
        }
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "{chan=%d,type=%s,value=%s}", channel, type, Arrays.toString(format()));  
    }
    
}
