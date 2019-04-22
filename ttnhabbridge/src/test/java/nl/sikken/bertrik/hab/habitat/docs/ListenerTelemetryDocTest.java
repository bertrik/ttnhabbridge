package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.habitat.HabReceiver;
import nl.sikken.bertrik.hab.habitat.Location;

/**
 * Unit tests for ListenerTelemetryDoc.
 */
public final class ListenerTelemetryDocTest {

    /**
     * Verifies basic formatting.
     */
    @Test
    public void testFormat() {
        final Instant instant = Instant.now();
        final HabReceiver receiver = new HabReceiver("BERTRIK", new Location(1.23, 4.56, 7.8));
        final ListenerTelemetryDoc doc = new ListenerTelemetryDoc(instant, receiver);
        final String json = doc.format();

        Assert.assertNotNull(json);
    }

}
