package nl.sikken.bertrik.cayenne;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A cayenne message containing cayenne data items.
 */
public final class CayenneMessage {
    
    private final Map<Integer,String[]> map = new HashMap<>();
    
    /**
     * Parses the byte array into a cayenne message.
     * 
     * @param data the raw data
     * @return the cayenne message
     * @throws CayenneException in case of a parsing problem
     */
    public static CayenneMessage parse(byte[] data) throws CayenneException {
        final CayenneMessage message = new CayenneMessage();
        try {
            final ByteBuffer bb = ByteBuffer.wrap(data);
            while (bb.hasRemaining()) {
                final int channel = bb.get();
                final int type = bb.get() & 0xFF;
                final ECayenneItem ct = ECayenneItem.parse(type);
                if (ct == null) {
                    throw new CayenneException("Invalid cayenne type " + type);
                }
                final String[] formatted = ct.format(bb);
                message.map.put(channel, formatted);
            }
        } catch (BufferUnderflowException e) {
            throw new CayenneException(e);
        }
        return message;
    }
    
    /**
     * @return a map from a channel number to a formatted String[].
     */
    public Map<Integer, String[]> getItems() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        map.forEach((k,v) -> {
          sb.append(String.format(Locale.US, "[%d]='%s',", k, Arrays.toString(v)));  
        });
        return sb.toString();
    }
    
}
