package nl.sikken.bertrik.cayenne;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A cayenne message containing cayenne data items.
 */
public final class CayenneMessage {
    
    private final List<CayenneItem> items = new ArrayList<>();
    
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
                final Double[] values = ct.parse(bb);
                final CayenneItem item = new CayenneItem(channel, ct, values);
                message.items.add(item);
            }
        } catch (BufferUnderflowException e) {
            throw new CayenneException(e);
        }
        return message;
    }
    
    /**
     * @return an immutable list of measurement items in the order it appears in the raw data
     */
    public List<CayenneItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    /**
     * Finds an item by type.
     * 
     * @param type the desired type
     * @return the item, or null if it does not exist
     */
    public CayenneItem ofType(ECayenneItem type) {
        return items.stream().filter(i -> (i.getType() == type)).findFirst().orElse(null);
    }
    
    /**
     * Finds an item by channel.
     * 
     * @param channel the desired channel
     * @return the item, or null if it does not exist
     */
    public CayenneItem ofChannel(int channel) {
        return items.stream().filter(i -> (i.getChannel() == channel)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return Arrays.toString(items.toArray());
    }
    
}
