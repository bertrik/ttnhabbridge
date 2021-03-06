package nl.sikken.bertrik.hab.ttn;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.ttn.TtnUplinkMessage.GatewayInfo;

public final class Ttnv2UplinkMessageTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Tests decoding of a JSON uplink message.
     */
    @Test
    public void testDecode() throws JsonMappingException, JsonProcessingException {
        String data = "{\"app_id\":\"habhub\",\"dev_id\":\"ttntest1\",\"hardware_serial\":\"0004A30B001ADBC5\","
                + "\"port\":1,\"counter\":9,\"payload_raw\":\"AYgH8BwAt08AETACAgGlA2cBIg==\","
                + "\"metadata\":{\"time\":\"2017-09-08T16:53:10.446526987Z\",\"frequency\":868.1,"
                + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\","
                + "\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":2382048707,"
                + "\"time\":\"2017-09-08T16:53:10.342388Z\",\"channel\":0,\"rssi\":-119,\"snr\":-2,\"rf_chain\":1,"
                + "\"latitude\":52.0182,\"longitude\":4.70844,\"altitude\":27}]}}";
        Ttnv2UplinkMessage message = MAPPER.readValue(data, Ttnv2UplinkMessage.class);

        Assert.assertNotNull(message);
        TtnUplinkMessage uplinkMessage = message.toUplinkMessage();

        Assert.assertEquals("habhub", uplinkMessage.getAppId());
        Assert.assertEquals("ttntest1", uplinkMessage.getDevId());
        Assert.assertEquals(1, uplinkMessage.getPort());
        Assert.assertEquals(9, uplinkMessage.getCounter());

        List<GatewayInfo> gateways = uplinkMessage.getGateways();
        GatewayInfo gw = gateways.get(0);
        Assert.assertEquals("eui-008000000000b8b6", gw.getId());
        Assert.assertEquals(52.0182, gw.getLocation().getLat(), 1E-4);
        Assert.assertEquals(4.70844, gw.getLocation().getLon(), 1E-4);
        Assert.assertEquals(27, gw.getLocation().getAlt(), 1E-1);
    }

    /**
     * Verifies that a nominal valid uplink message can be parsed.
     */
    @Test
    public void testUplink() throws JsonParseException, JsonMappingException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("uplink_nominal.json");
        Ttnv2UplinkMessage message = MAPPER.readValue(is, Ttnv2UplinkMessage.class);
        TtnUplinkMessage uplink = message.toUplinkMessage();
        Assert.assertFalse(uplink.isRetry());
    }

    /**
     * Verifies that an uplink message with "is_retry" can be parsed.
     */
    @Test
    public void testUplinkWithRetry() throws JsonParseException, JsonMappingException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("uplink_with_retry.json");
        Ttnv2UplinkMessage message = MAPPER.readValue(is, Ttnv2UplinkMessage.class);
        TtnUplinkMessage uplink = message.toUplinkMessage();
        Assert.assertTrue(uplink.isRetry());
    }
}
