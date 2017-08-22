package nl.sikken.bertrik.hab.ttn;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.SodaqOnePayload;

/**
 * Unit tests for TTN messages.
 */
public final class TtnMessageTest {

    private static final String DATA = "{\"app_id\":\"ttnmapper\",\"dev_id\":\"mapper2\","
            + "\"hardware_serial\":\"0004A30B001ADBC5\",\"port\":1,\"counter\":4,"
            + "\"payload_raw\":\"loeaWW4T2+8BHzYZzAIeAA8A/QUS\","
            + "\"metadata\":{\"time\":\"2017-08-21T07:11:18.313946438Z\",\"frequency\":868.3,"
            + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\","
            + "\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":1409115451,"
            + "\"time\":\"2017-08-21T07:11:18.338662Z\",\"channel\":1,\"rssi\":-114,\"snr\":-0.2,"
            + "\"rf_chain\":1,\"latitude\":52.0182,\"longitude\":4.70844,\"altitude\":27}]}}";

    /**
     * Verifies decoding of some actual MQTT data.
     * 
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void testDecode() throws JsonParseException, JsonMappingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final TtnMessage mqttData = mapper.readValue(DATA, TtnMessage.class);
        final byte[] raw = mqttData.getPayload();

        // check gateway field
        Assert.assertEquals(27, mqttData.getMetaData().getMqttGateways().get(0).getAltitude(), 0.1);
        
        // decode payload
        final SodaqOnePayload payload = SodaqOnePayload.parse(raw);
        Assert.assertNotNull(payload);
    }
    
}
