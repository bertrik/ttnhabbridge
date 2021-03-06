package nl.sikken.bertrik.hab;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sikken.bertrik.hab.ttn.TtnUplinkMessage;
import nl.sikken.bertrik.hab.ttn.Ttnv2UplinkMessage;

/**
 * Unit tests for PayloadDecoder.
 */
public final class PayloadDecoderTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies decoding of some actual MQTT data, in RAW format.
     * 
     * @throws IOException     in case of a parse exception
     * @throws DecodeException in case of a decode exception
     */
    @Test
    public void testDecodeSodaqOne() throws IOException, DecodeException {
        String data = "{\"app_id\":\"ttnmapper\",\"dev_id\":\"mapper2\","
                + "\"hardware_serial\":\"0004A30B001ADBC5\",\"port\":1,\"counter\":4,"
                + "\"payload_raw\":\"loeaWW4T2+8BHzYZzAIeAA8A/QUS\","
                + "\"metadata\":{\"time\":\"2017-08-21T07:11:18.313946438Z\",\"frequency\":868.3,"
                + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\","
                + "\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":1409115451,"
                + "\"time\":\"2017-08-21T07:11:18.338662Z\",\"channel\":1,\"rssi\":-114,\"snr\":-0.2,"
                + "\"rf_chain\":1,\"latitude\":52.0182,\"longitude\":4.70844,\"altitude\":27}]}}";

        Ttnv2UplinkMessage message = mapper.readValue(data, Ttnv2UplinkMessage.class);
        TtnUplinkMessage uplink = message.toUplinkMessage();

        // check gateway field
        Assert.assertEquals(27, message.getMetaData().getMqttGateways().get(0).getAltitude(), 0.1);

        // decode payload
        PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.SODAQ_ONE);
        Sentence sentence = decoder.decode(uplink);

        Assert.assertEquals("$$mapper2,4,07:11:18,52.022064,4.693023,30.0,19,4.10*81FD", sentence.format().trim());
    }

    /**
     * Verifies decoding of some actual MQTT data, in cayenne format (fix applied to
     * analog input).
     * 
     * @throws IOException     in case of a parse exception
     * @throws DecodeException in case of a decode exception
     */
    @Test
    public void testDecodeCayenne() throws IOException, DecodeException {
        String data = "{\"app_id\":\"habhub\",\"dev_id\":\"ttntest1\",\"hardware_serial\":\"0004A30B001ADBC5\","
                + "\"port\":1,\"counter\":9,\"payload_raw\":\"AYgH8BwAt08AETACAgGlA2cBIg==\","
                + "\"metadata\":{\"time\":\"2017-09-08T16:53:10.446526987Z\",\"frequency\":868.1,"
                + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\","
                + "\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":2382048707,"
                + "\"time\":\"2017-09-08T16:53:10.342388Z\",\"channel\":0,\"rssi\":-119,\"snr\":-2,\"rf_chain\":1,"
                + "\"latitude\":52.0182,\"longitude\":4.70844,\"altitude\":27}]}}";
        Ttnv2UplinkMessage message = mapper.readValue(data, Ttnv2UplinkMessage.class);
        TtnUplinkMessage uplink = message.toUplinkMessage();

        // decode payload
        PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.CAYENNE);
        Sentence sentence = decoder.decode(uplink);

        Assert.assertEquals("$$ttntest1,9,16:53:10,52.0220,4.6927,44.00,4.21,29.0*383E\n", sentence.format());
    }

    /**
     * Verifies that an unknown payload encoding is detected.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidEncoding() {
        PayloadDecoder decoder = new PayloadDecoder(null);
        Assert.assertNotNull(decoder);
    }

    /**
     * Verifies decoding of another set of actual payload data.
     * 
     * @throws DecodeException
     */
    @Test
    public void testCayenne2() throws DecodeException {
        TtnUplinkMessage message = new TtnUplinkMessage(Instant.parse("2020-02-05T22:00:58.930936Z"), "test", "test",
                123, 1, Base64.getDecoder().decode("AYgH1ecAzV4AC7gCZwArAwIBhg=="), false);
        // decode payload
        PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.CAYENNE);
        Sentence sentence = decoder.decode(message);
        Assert.assertEquals("$$test,123,22:00:58,51.3511,5.2574,30.00,4.3,3.90*A07E\n", sentence.format());
    }

    @Test
    public void testCayenneWithFields() throws DecodeException, JsonMappingException, JsonProcessingException {
        String json = "{\"app_id\":\"ttn-arduino-tracker-swallow\",\"dev_id\":\"ttnwiv2n\",\"hardware_serial\":\"003B0C6BF8C3B76E\",\"port\":1,\"counter\":170,\"payload_raw\":\"AYgHr8T/Yr4AIaI=\",\r\n"
                + "\"payload_fields\":{\"gps_1\":{\"altitude\":86.1,\"latitude\":50.3748,\"longitude\":-4.0258}},\"metadata\":{\"time\":\"2020-08-17T17:51:05.573485776Z\",\"frequency\":868.5,\"modulation\":\r\n"
                + "\"LORA\",\"data_rate\":\"SF7BW125\",\"airtime\":61696000,\"coding_rate\":\"4/5\",\"gateways\":[{\"gtw_id\":\"eui-58a0cbfffe801f61\",\"timestamp\":141113011,\"time\":\"2020-08-17T17:51:05.438510894Z\",\r\n"
                + "\"channel\":0,\"rssi\":-34,\"snr\":7.75,\"rf_chain\":0}]}}";
        ObjectMapper mapper = new ObjectMapper();
        Ttnv2UplinkMessage message = mapper.readValue(json, Ttnv2UplinkMessage.class);
        TtnUplinkMessage uplink = message.toUplinkMessage();
        PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.CAYENNE);
        Sentence sentence = decoder.decode(uplink);
        Assert.assertNotNull(sentence);
        Assert.assertEquals("$$ttnwiv2n,170,17:51:05,50.3748,-4.0258,86.10*6647\n", sentence.format());
    }

}
