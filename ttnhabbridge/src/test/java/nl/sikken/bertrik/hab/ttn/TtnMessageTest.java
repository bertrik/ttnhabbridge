package nl.sikken.bertrik.hab.ttn;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TtnMessageTest {

    private ObjectMapper mapper;
    
    @Before
    public void before() {
        mapper = new ObjectMapper();
    }

    /**
     * Verifies that a nominal valid uplink message can be parsed.
     */
    @Test
    public void testUplink() throws JsonParseException, JsonMappingException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("uplink_nominal.json");
        Ttnv2UplinkMessage message = mapper.readValue(is, Ttnv2UplinkMessage.class);
        Assert.assertEquals(false, message.isRetry());
    }
    
    /**
     * Verifies that an uplink message with "is_retry" can be parsed.
     */
    @Test
    public void testUplinkWithRetry() throws JsonParseException, JsonMappingException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("uplink_with_retry.json");
        Ttnv2UplinkMessage message = mapper.readValue(is, Ttnv2UplinkMessage.class);
        Assert.assertEquals(true, message.isRetry());
    }
    
}
