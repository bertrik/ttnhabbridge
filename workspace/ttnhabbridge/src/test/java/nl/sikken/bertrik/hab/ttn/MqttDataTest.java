package nl.sikken.bertrik.hab.ttn;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for MqttData.
 */
public final class MqttDataTest {

    @Test
    public void testDeserialization() throws JsonParseException, JsonMappingException, IOException {
        final String s = "{\"app_id\":\"ttnmapper\",\"dev_id\":\"mapper2\","
                + "\"hardware_serial\":\"0004A30B001ADBC5\",\"port\":1,\"counter\":0,"
                + "\"payload_raw\":\"AA==\",\"metadata\":{\"time\":\"2017-08-19T15:23:39.288816687Z\"}}";

        final ObjectMapper mapper = new ObjectMapper();
        final MqttData mqttData = mapper.readValue(s, MqttData.class);

        Assert.assertEquals(0, mqttData.getCounter());
    }

    /**
     * Verify de-serializing some actual MQTT received from TTN.
     * 
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void testGateways() throws JsonParseException, JsonMappingException, IOException {
        final String s = "{ \"app_id\": \"ttn-rfm95-ardumin1\", \"dev_id\": \"ttn-rfm95-2\", \"hardware_serial\": \"0034DA568C2A7566\", \"port\": 1, \"counter\": 11641, \"payload_raw\": \"EHs=\", \"payload_fields\": { \"value\": \"2500\" }, \"metadata\": { \"time\": \"2017-08-20T15:28:19.454240399Z\", \"frequency\": 868.3, \"modulation\": \"LORA\", \"data_rate\": \"SF9BW125\", \"coding_rate\": \"4\\/5\", \"gateways\": [ { \"gtw_id\": \"ttn-cgn_philippshof_1\", \"gtw_trusted\": true, \"timestamp\": 3953316716, \"time\": \"2017-08-20T15:28:18Z\", \"channel\": 1, \"rssi\": -63, \"snr\": 11.5, \"rf_chain\": 1, \"latitude\": 50.950233, \"longitude\": 6.9196253, \"altitude\": 18 } ] } }";
        final ObjectMapper mapper = new ObjectMapper();
        final MqttData mqttData = mapper.readValue(s, MqttData.class);
        Assert.assertEquals(1, mqttData.getPort());
        
        final List<MqttGateway> gws = mqttData.getMetaData().getMqttGateways();
        final MqttGateway gw1 = gws.get(0);
        
        Assert.assertEquals(18, gw1.getAltitude(), 0.1);
    }
    
    @Test
    public void testDecode() throws JsonParseException, JsonMappingException, IOException {
        final String s = "{\"app_id\":\"ttnmapper\",\"dev_id\":\"mapper2\",\"hardware_serial\":\"0004A30B001ADBC5\",\"port\":1,\"counter\":4,\"payload_raw\":\"loeaWW4T2+8BHzYZzAIeAA8A/QUS\",\"metadata\":{\"time\":\"2017-08-21T07:11:18.313946438Z\",\"frequency\":868.3,\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"coding_rate\":\"4/5\",\"gateways\":[{\"gtw_id\":\"eui-008000000000b8b6\",\"timestamp\":1409115451,\"time\":\"2017-08-21T07:11:18.338662Z\",\"channel\":1,\"rssi\":-114,\"snr\":-0.2,\"rf_chain\":1,\"latitude\":52.0182,\"longitude\":4.70844,\"altitude\":27}]}}";
        final ObjectMapper mapper = new ObjectMapper();
        final MqttData mqttData = mapper.readValue(s, MqttData.class);
        
        Assert.assertEquals(27, mqttData.getMetaData().getMqttGateways().get(0).getAltitude(), 0.1);
    }

}
