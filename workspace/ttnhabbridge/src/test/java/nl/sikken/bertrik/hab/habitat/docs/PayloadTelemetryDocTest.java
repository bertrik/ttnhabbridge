package nl.sikken.bertrik.hab.habitat.docs;

import java.util.Date;

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
		final Date date = new Date();
		final PayloadTelemetryDoc doc = new PayloadTelemetryDoc(date, "BERTRIK", new byte[] {1, 2, 3});
		final String json = doc.format();
		
		Assert.assertNotNull(json);
	}
	
}
