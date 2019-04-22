package nl.sikken.bertrik.cayenne;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for SimpleCayenne.
 */
public final class SimpleCayenneTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleCayenneTest.class);
    
    /**
     * Verifies basic functionality by adding some items and encoding it into a message
     * @throws CayenneException in case of a problem encoding/decoding
     */
    @Test
    public void testEncode() throws CayenneException {
        SimpleCayenne cayenne = new SimpleCayenne();
        cayenne.addGps(1, 52.0, 4.0, -1.0);
        cayenne.addAnalogInput(2, 3.82);
        cayenne.addTemperature(3, 19.0);
        LOG.info("Encoded message: {}", cayenne);
        
        // encode it
        byte[] data = cayenne.encode(500);
        Assert.assertNotNull(data);
        
        // decode it
        CayenneMessage message = CayenneMessage.parse(data);
        List<CayenneItem> items = message.getItems();
        Assert.assertEquals(52.0, items.get(0).getValues()[0], 0.1);
        Assert.assertEquals(3.82, items.get(1).getValue(), 0.01);
        Assert.assertEquals(19.0, items.get(2).getValues()[0], 0.1);
    }
    
    /**
     * Verifies that a simple cayenne message with non-unique channels is rejected.
     * 
     * @throws CayenneException in case of a problem encoding/decoding
     */
    @Test(expected = CayenneException.class)
    public void testNonUniqueChannel() throws CayenneException {
        SimpleCayenne cayenne = new SimpleCayenne();
        cayenne.addTemperature(1, 19.0);
        cayenne.addAnalogInput(1, 3.90);
    }

}
