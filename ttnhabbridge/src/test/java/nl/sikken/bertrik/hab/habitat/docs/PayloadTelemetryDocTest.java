package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for PayloadTelemetryDoc.
 */
public final class PayloadTelemetryDocTest {

	/**
	 * Verifies basic formatting.
	 */
	@Test
	public void testFormat() {
		Instant instant = Instant.now();
		HabitatPayloadTelemetryDoc doc = new HabitatPayloadTelemetryDoc(instant, new byte[] {1, 2, 3});
		doc.addCallSign("BERTRIK");
		String json = doc.format();
		
		Assert.assertNotNull(json);
	}
	
}
