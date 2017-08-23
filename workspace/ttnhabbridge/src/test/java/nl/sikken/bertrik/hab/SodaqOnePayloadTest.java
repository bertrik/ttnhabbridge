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
        final String encoded = "wd6dWXEOiQMCH9ELzAIXAAAAAAUB";
        final byte[] data = Base64.getDecoder().decode(encoded);
        final SodaqOnePayload payload = SodaqOnePayload.parse(data); 
        
        Assert.assertEquals(1503518401, payload.getTimeStamp());
        Assert.assertNotNull(payload.toString());
    }

}
