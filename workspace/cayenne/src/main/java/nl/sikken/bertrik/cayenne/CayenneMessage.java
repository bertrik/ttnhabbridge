package nl.sikken.bertrik.cayenne;

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
        final ByteBuffer bb = ByteBuffer.wrap(data);
        while (bb.hasRemaining()) {
            final CayenneItem item = CayenneItem.parse(bb);
            message.items.add(item);
        }
        return message;
    }
    
    /**
     * Adds a cayenne measurement item to the message.
     * 
     * @param item the item to add
     */
    public void add(CayenneItem item) {
        items.add(item);
    }
    
    /**
     * Encodes the cayenne message into a byte array.
     * 
     * @return the byte array.
     * @throws CayenneException in case something went wrong during encoding (e.g. message too big)
     */
    public byte[] encode() throws CayenneException {
        final byte[] bytes = new byte[500];
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        for (CayenneItem i : items) {
            i.encode(bb);
        }
        return Arrays.copyOfRange(bytes, 0, bb.position());
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
