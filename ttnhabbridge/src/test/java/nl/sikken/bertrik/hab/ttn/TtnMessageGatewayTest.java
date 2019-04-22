package nl.sikken.bertrik.hab.ttn;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.habitat.Location;

/**
 * Unit tests of TtnMessageGateway.
 */
public final class TtnMessageGatewayTest {
    
    /**
     * Verifies serialization to JSON.
     * 
     * @throws JsonProcessingException in case of a JSON error
     */
    @Test
    public void testJson() throws JsonProcessingException {
        final TtnMessageGateway gw = new TtnMessageGateway("id", true, "time", 0.0, 1.1, 2.2); 
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(gw);
        
        Assert.assertNotNull(json);
        Assert.assertFalse(json.contains("location") || json.contains("Location"));
    }
    
    /**
     * Verifies that we can get a location.
     */
    @Test
    public void testLocation() {
        final TtnMessageGateway gw = new TtnMessageGateway("id", true, "time", 0.0, 1.1, 2.2);
        Assert.assertTrue(gw.getLocation().isValid());

        final Location location = gw.getLocation();
        Assert.assertNotNull(location);
        Assert.assertEquals(1.1, location.getLon(), 0.01);
    }

    /**
     * Verifies that absence of location is detected.
     */
    @Test
    public void testNoLocation() {
        final TtnMessageGateway gw1 = new TtnMessageGateway("id", true, "time", null, null, null);
        Assert.assertFalse(gw1.getLocation().isValid());

        final TtnMessageGateway gw2 = new TtnMessageGateway("id", true, "time", null, 1.1, 2.2);
        Assert.assertFalse(gw2.getLocation().isValid());
    }

}
