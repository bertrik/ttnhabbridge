package nl.sikken.bertrik.cayenne;

import java.util.Base64;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for CayenneMessage.
 */
public final class CayenneMessageTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(CayenneMessageTest.class);
    
    /**
     * Verifies example from specification.
     * @throws CayenneException in case of a parsing exception
     */
    @Test
    public void testTwoTemperatureSensors() throws CayenneException {
        final byte[] data = {0x03, 0x67, 0x01, 0x10, 0x05, 0x67, 0x00, (byte) 0xFF};
        final CayenneMessage payload = CayenneMessage.parse(data);
        LOG.info("payload = {}", payload);
        
        final Map<Integer, String[]> items = payload.getItems();
        Assert.assertArrayEquals(new String[] {"27.2"}, items.get(3));
        Assert.assertArrayEquals(new String[] {"25.5"}, items.get(5));
    }
    
    /**
     * Verifies example from specification.
     * @throws CayenneException in case of a parsing exception
     */
    @Test
    public void testTemperaturePlusAccel() throws CayenneException {
        final byte[] data = 
            {0x01, 0x67, (byte) 0xFF, (byte) 0xD7, 0x06, 0x71, 0x04, (byte) 0xD2, (byte) 0xFB, 0x2E, 0x00, 0x00};
        final CayenneMessage payload = CayenneMessage.parse(data);

        final Map<Integer, String[]> items = payload.getItems();
        Assert.assertArrayEquals(new String[] {"-4.1"}, items.get(1));
        Assert.assertArrayEquals(new String[] {"1.234", "-1.234", "0.000"}, items.get(6));
    }

    /**
     * Verifies example from specification.
     * @throws CayenneException in case of a parsing exception
     */
    @Test
    public void testGps() throws CayenneException {
        final byte[] data = 
            {0x01, (byte) 0x88, 0x06, 0x076, 0x5f, (byte) 0xf2, (byte) 0x96, 0x0a, 0x00, 0x03, (byte) 0xe8};
        final CayenneMessage payload = CayenneMessage.parse(data);

        final Map<Integer, String[]> items = payload.getItems();
        Assert.assertArrayEquals(new String[] {"42.3519", "-87.9094", "10.00"}, items.get(1));
    }

    /**
     * Verifies parsing of humidity value.
     * @throws CayenneException in case of a parsing exception
     */
    @Test
    public void testHumidity() throws CayenneException {
        final byte[] data = {1, 104, 100};
        final CayenneMessage payload = CayenneMessage.parse(data);
        
        final Map<Integer, String[]> items = payload.getItems();
        Assert.assertArrayEquals(new String[] {"50.0"}, items.get(1));
    }
    
    /**
     * Verifies parsing of some actual data from a sodaq one.
     * @throws CayenneException in case of a parsing exception
     */
    @Test
    public void testActualData() throws CayenneException {
        final String base64 = "AYgH8CEAt1D//zgCAmDQA2cBDg==";
        final byte[] data = Base64.getDecoder().decode(base64);
        final CayenneMessage payload = CayenneMessage.parse(data);

        final Map<Integer, String[]> items = payload.getItems();
        Assert.assertArrayEquals(new String[] {"52.0225", "4.6928", "-2.00"}, items.get(1));
        Assert.assertArrayEquals(new String[] {"247.84"}, items.get(2));
        Assert.assertArrayEquals(new String[] {"27.0"}, items.get(3));
    }

    /**
     * Verifies parsing an empty string.
     * @throws CayenneException in case of a parsing exception
     */
    @Test
    public void testParseEmpty() throws CayenneException {
        final CayenneMessage payload = CayenneMessage.parse(new byte[0]);
        Assert.assertTrue(payload.getItems().isEmpty());
    }
 
    /**
     * Verifies parsing a short buffer
     * @throws CayenneException in case of a parsing exception
     */
    @Test(expected = CayenneException.class)
    public void testShortBuffer() throws CayenneException {
        CayenneMessage.parse(new byte[] {0});
    }

    /**
     * Verifies parsing of a buffer containing a non-supported data type.
     * @throws CayenneException in case of a parsing exception
     */
    @Test(expected = CayenneException.class)
    public void testInvalidType() throws CayenneException {
        CayenneMessage.parse(new byte[] {0, 100});
    }
    
    /**
     * Verifies parsing of a buffer containing insufficient data during parsing.
     * @throws CayenneException in case of a parsing exception
     */
    @Test(expected = CayenneException.class)
    public void testShortData() throws CayenneException {
        CayenneMessage.parse(new byte[] {2, 1});
    }
    
}

