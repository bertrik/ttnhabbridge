package nl.sikken.bertrik.hab;


import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for ICSSPayload.
 */
public final class ICSSPayloadTest {
	
    byte[] data = hexStringToByteArray("7e7a5167ca1712d3f200");
    ICSSPayload payload = ICSSPayload.parse(data); 
    
    /**
     * Verifies basic parsing.
     */
    @Test
    public void testParse() {
        Assert.assertEquals(1503518401, payload.getTimeStamp());
        Assert.assertNotNull(payload.toString());
    }
    
    
    @Test
    public void testParseVoltage() {
        Assert.assertEquals(43, payload.getloadVoltage());
        Assert.assertEquals(33, payload.getnoloadVoltage());
    }
    
    @Test
    public void testParsetemp() {
        Assert.assertEquals(-23, payload.getBoardTemp());
    }
    
    @Test
    public void testParsepressure() {
        Assert.assertEquals(400, payload.getPressure());
    }
    
    @Test
    public void test_getData_received_flag() {
        Assert.assertEquals(1, payload.getData_received_flag());
    }
    
    @Test
    public void test_getNumSats() {
        Assert.assertEquals(12, payload.getNumSats());
    }
    
    @Test
    public void test_getReset_cnt() {
        Assert.assertEquals(7, payload.getReset_cnt());
    }
    
    @Test
    public void test_long_lat() {
        Assert.assertEquals(39.9121314, payload.getLatitude(),0.0001);
        //Assert.assertEquals(-75.3780105, payload.getLongitude(),0.0001);

    }
    
    @Test
    public void test_getAltitude() {
        Assert.assertEquals(61, payload.getAltitude());

    }
    
    
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
