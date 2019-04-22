package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.habitat.HabReceiver;
import nl.sikken.bertrik.hab.habitat.Location;

/**
 * Unit tests for ListenerInfoDoc
 */
public final class ListenerInfoDocTest {

    /**
     * Verifies basic formatting.
     */
    @Test
    public void testFormat() {
        final Instant instant = Instant.now();
        final HabReceiver receiver = new HabReceiver("BERTRIK", new Location(52.0162, 4.4753, 0.0));
        final ListenerInformationDoc doc = new ListenerInformationDoc(instant, receiver);
        final String json = doc.format();

        Assert.assertNotNull(json);
    }

}
