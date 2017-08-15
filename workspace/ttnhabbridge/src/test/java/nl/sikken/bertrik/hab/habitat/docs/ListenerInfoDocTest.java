package nl.sikken.bertrik.hab.habitat.docs;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for ListenerInfoDoc
 */
public final class ListenerInfoDocTest {
	
	/**
	 * Verifies basic formatting.
	 */
	@Test
	public void testFormat() {
		final Date date = new Date();
		final ListenerInformationDoc doc = new ListenerInformationDoc(date, "BERTRIK");
		final String json = doc.format();
		
		Assert.assertNotNull(json);
	}

}
