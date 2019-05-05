package nl.sikken.bertrik.hab;

import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for SodaqOnePayload.
 */
public final class SodaqOnePayloadTest {
    
    /**
     * Verifies basic parsing.
     */
    @Test
    public void testParse() {
        String encoded = "wd6dWXEOiQMCH9ELzAIXAAAAAAUB";
        byte[] data = Base64.getDecoder().decode(encoded);
        SodaqOnePayload payload = SodaqOnePayload.parse(data); 
        
        Assert.assertEquals(1503518401, payload.getTimeStamp());
        Assert.assertNotNull(payload.toString());
    }

}
