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
        TtnMessageGateway gw = new TtnMessageGateway("id", true, "time", 0.0, 1.1, 2.2); 
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(gw);
        
        Assert.assertNotNull(json);
        Assert.assertFalse(json.contains("location") || json.contains("Location"));
    }
    
    /**
     * Verifies that we can get a location.
     */
    @Test
    public void testLocation() {
        TtnMessageGateway gw = new TtnMessageGateway("id", true, "time", 0.0, 1.1, 2.2);
        Assert.assertTrue(gw.getLocation().isValid());

        Location location = gw.getLocation();
        Assert.assertNotNull(location);
        Assert.assertEquals(1.1, location.getLon(), 0.01);
    }

    /**
     * Verifies that absence of location is detected.
     */
    @Test
    public void testNoLocation() {
        TtnMessageGateway gw1 = new TtnMessageGateway("id", true, "time", null, null, null);
        Assert.assertFalse(gw1.getLocation().isValid());

        TtnMessageGateway gw2 = new TtnMessageGateway("id", true, "time", null, 1.1, 2.2);
        Assert.assertFalse(gw2.getLocation().isValid());
    }

}
