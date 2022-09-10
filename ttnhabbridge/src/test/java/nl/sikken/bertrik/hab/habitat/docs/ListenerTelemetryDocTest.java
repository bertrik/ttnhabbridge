package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.HabReceiver;
import nl.sikken.bertrik.hab.Location;

/**
 * Unit tests for ListenerTelemetryDoc.
 */
public final class ListenerTelemetryDocTest {

    /**
     * Verifies basic formatting.
     */
    @Test
    public void testFormat() {
        Instant instant = Instant.now();
        HabReceiver receiver = new HabReceiver("BERTRIK", new Location(1.23, 4.56, 7.8));
        ListenerTelemetryDoc doc = new ListenerTelemetryDoc(instant, receiver);
        String json = doc.format();

        Assert.assertNotNull(json);
    }

}
