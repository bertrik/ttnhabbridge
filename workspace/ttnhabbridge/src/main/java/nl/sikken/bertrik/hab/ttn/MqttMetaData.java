package nl.sikken.bertrik.hab.ttn;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of meta-data part of MQTT message.
 */
public final class MqttMetaData {

	@JsonProperty("time")
	private String time;
	
	private MqttMetaData() {
		// jackson constructor
	}
	
}
