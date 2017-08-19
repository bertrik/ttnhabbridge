package nl.sikken.bertrik.hab.ttn;

import java.io.IOException;

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

}
