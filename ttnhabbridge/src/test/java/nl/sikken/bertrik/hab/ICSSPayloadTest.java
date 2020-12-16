package nl.sikken.bertrik.hab;


import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for ICSSPayload.
 */
public final class ICSSPayloadTest {
	
    byte[] data = hexStringToByteArray("6b4dc82915f91ece004601fa1ed100e300426107fa1ed1000901666107fa1ed1008602117d07fa1ed100e90004ae07fa1ed100e300426107fa1ed1000901666107fa1ed1008602117d07fa1ed100e90004ae07fa1ed100e300426107fa1ed1000901666107fa1ed1008602117d07fa1ed100e90004ae07");
    ICSSPayload payload = ICSSPayload.parse(data); 
    
    /**
     * Verifies basic parsing.
     */
    @Test
    public void testParse() {
        Assert.assertNotNull(payload.toString());
    }
    
    
    @Test
    public void testParseVoltage() {
        Assert.assertEquals(31, payload.getloadVoltage());
        Assert.assertEquals(31, payload.getnoloadVoltage());
    }
    
    @Test
    public void testParsetemp() {
        Assert.assertEquals(21, payload.getBoardTemp());
    }
    
    @Test
    public void testParsepressure() {
        Assert.assertEquals(1000, payload.getPressure());
    }
    
    @Test
    public void test_getData_received_flag() {
        Assert.assertEquals(0, payload.getData_received_flag());
    }
    
    @Test
    public void test_getNumSats() {
        Assert.assertEquals(5, payload.getNumSats());
    }
    
    @Test
    public void test_getReset_cnt() {
        Assert.assertEquals(1, payload.getReset_cnt());
    }
    
    @Test
    public void test_long_lat() {
        Assert.assertEquals(1.350021004, payload.getLongitude(),0.00001);
        Assert.assertEquals(51.96269989, payload.getLatitude(),0.00001);

    }
    
    @Test
    public void test_getAltitude() {
        Assert.assertEquals(83, payload.getAltitude());

    }
    
    @Test
    public void test_get_playback_days() {
        Assert.assertEquals(13, payload.getDays_of_playback());

    }
    
    @Test
    public void test_getPast_position_times_size() {
    	Assert.assertEquals(12, payload.getPast_position_times().size());
    }
    
    
    @Test
    public void test_getPast_lat_0() {
    	Assert.assertEquals(51.9692535400, payload.getPast_position_times().get(0).getLatitude(),0.00001);
    }
    
    
    @Test
    public void test_getPast_long_0() {
    	Assert.assertEquals(1.3696814775, payload.getPast_position_times().get(0).getLongitude(),0.00001);
    }
    
    @Test
    public void test_getPast_alt_0() {
    	Assert.assertEquals(57, payload.getPast_position_times().get(0).getAltitude());
    }
    
    @Test
    public void test_getPast_unix_time_0() {
    	Assert.assertEquals(1606859461, payload.getPast_position_times().get(0).getUnix_time());
    }
 
    
    
    @Test
    public void test_getPast_lat_1() {
    	Assert.assertEquals(51.969253540, payload.getPast_position_times().get(1).getLatitude(),0.00001);
    }
    
    
    @Test
    public void test_getPast_long_1() {
    	Assert.assertEquals(1.3696814775, payload.getPast_position_times().get(1).getLongitude(),0.00001);
    }
    
    @Test
    public void test_getPast_alt_1() {
    	Assert.assertEquals(67, payload.getPast_position_times().get(1).getAltitude());
    }
    
    @Test
    public void test_getPast_unix_time_1() {
    	Assert.assertEquals(1606861621, payload.getPast_position_times().get(1).getUnix_time());
    }
    
    
    @Test
    public void test_getPast_lat_2() {
    	Assert.assertEquals(51.9692535400, payload.getPast_position_times().get(2).getLatitude(),0.00001);
    }
    
    
    @Test
    public void test_getPast_long_2() {
    	Assert.assertEquals(1.3696814775, payload.getPast_position_times().get(2).getLongitude(),0.00001);
    }
    
    @Test
    public void test_getPast_alt_2() {
    	Assert.assertEquals(164, payload.getPast_position_times().get(2).getAltitude());
    }
    
    @Test
    public void test_getPast_unix_time_2() {
    	Assert.assertEquals(1607286601, payload.getPast_position_times().get(2).getUnix_time());
    }
    
    @Test
    public void test_getPast_unix_time_3() {
    	Assert.assertEquals(1608038461, payload.getPast_position_times().get(3).getUnix_time());
    }
    
    @Test
    public void test_getPast_unix_time_4() {
    	Assert.assertEquals(1606859461, payload.getPast_position_times().get(4).getUnix_time());
    }
    
    @Test
    public void test_getPast_unix_time_5() {
    	Assert.assertEquals(1606861621, payload.getPast_position_times().get(5).getUnix_time());
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
