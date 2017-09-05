package nl.sikken.bertrik.hab;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.Sentence;
import nl.sikken.bertrik.hab.ttn.TtnMessage;

/**
 * Unit tests for PayloadDecoder.
 */
public final class PayloadDecoderTest {
    
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies decoding of some actual MQTT data, in JSON format.
     * 
     * @throws IOException in case of a parse exception
     */
    @Test
    public void testDecodeJson() throws IOException {
        final String data = "{\"app_id\":\"spaceballoon\",\"dev_id\":\"devtrack\","
                + "\"hardware_serial\":\"00490B7EB25521E6\",\"port\":1,\"counter\":1707,"
                + "\"payload_raw\":\"AAsm1AxMAUEAY/wCNh68EwKaihEClAEAAwAF\",\"payload_fields\":{\"baralt\":321,"
                + "\"gpsalt\":660,\"hacc\":3,\"hpa\":994,\"lat\":51.5642112,\"lon\":4.3682304,\"mode\":0,\"rssi\":-99,"
                + "\"sats\":17,\"seq\":565,\"slot\":1,\"snr\":-4,\"temp\":1.1,\"type\":\"stat\",\"vacc\":5,"
                + "\"vcc\":3.148},\"metadata\":{\"time\":\"2017-03-24T19:02:46.316523288Z\",\"frequency\":867.1,"
                + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\","
                + "\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":1250904307,"
                + "\"time\":\"2017-03-24T19:02:46.338171Z\",\"channel\":3,\"rssi\":-120,\"snr\":-8.5,"
                + "\"latitude\":52.0182,\"longitude\":4.7084384},{\"gtw_id\":\"eui-008000000000b706\","
                + "\"timestamp\":3407032963,\"time\":\"\",\"channel\":3,\"rssi\":-120,\"snr\":-3,"
                + "\"latitude\":51.57847,\"longitude\":4.4564,\"altitude\":4},{\"gtw_id\":\"eui-aa555a00080e0096\","
                + "\"timestamp\":422749595,\"time\":\"2017-03-24T19:02:45.89182Z\",\"channel\":3,\"rssi\":-118,"
                + "\"snr\":-0.8}]}}";
        final TtnMessage message = mapper.readValue(data, TtnMessage.class);
        Assert.assertEquals(3, message.getMetaData().getMqttGateways().size());
        
        final PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.JSON.getName());
        final Sentence sentence = decoder.decode(message);

        Assert.assertEquals("$$devtrack,1707,19:02:46,51.564211,4.368230,660.0,1.1,3.148*B35B",
                sentence.format().trim());
    }

    /**
     * Verifies decoding of some actual MQTT data, in RAW format.
     * 
     * @throws IOException in case of a parse exception
     */
    @Test
    public void testDecodeRaw() throws IOException {
        final String data = "{\"app_id\":\"ttnmapper\",\"dev_id\":\"mapper2\","
                + "\"hardware_serial\":\"0004A30B001ADBC5\",\"port\":1,\"counter\":4,"
                + "\"payload_raw\":\"loeaWW4T2+8BHzYZzAIeAA8A/QUS\","
                + "\"metadata\":{\"time\":\"2017-08-21T07:11:18.313946438Z\",\"frequency\":868.3,"
                + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\","
                + "\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":1409115451,"
                + "\"time\":\"2017-08-21T07:11:18.338662Z\",\"channel\":1,\"rssi\":-114,\"snr\":-0.2,"
                + "\"rf_chain\":1,\"latitude\":52.0182,\"longitude\":4.70844,\"altitude\":27}]}}";
 
        final TtnMessage message = mapper.readValue(data, TtnMessage.class);

        // check gateway field
        Assert.assertEquals(27, message.getMetaData().getMqttGateways().get(0).getAltitude(), 0.1);
        
        // decode payload
        final PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.SODAQ_ONE.getName());
        final Sentence sentence = decoder.decode(message);
        
        Assert.assertEquals("$$mapper2,4,07:11:18,52.022064,4.693023,30.0,19,4.10*81FD", sentence.format().trim());
    }
    
    /**
     * Verifies that an unknown payload encoding is detected.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidEncoding() {
        final PayloadDecoder decoder = new PayloadDecoder("unknown");
        Assert.assertNotNull(decoder);
    }
}
