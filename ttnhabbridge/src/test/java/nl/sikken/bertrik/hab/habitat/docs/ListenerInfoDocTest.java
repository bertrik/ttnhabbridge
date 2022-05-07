package nl.sikken.bertrik.hab.habitat.docs;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.HabReceiver;
import nl.sikken.bertrik.hab.Location;

/**
 * Unit tests for ListenerInfoDoc
 */
public final class ListenerInfoDocTest {

    /**
     * Verifies basic formatting.
     */
    @Test
    public void testFormat() {
        Instant instant = Instant.now();
        HabReceiver receiver = new HabReceiver("BERTRIK", new Location(52.0162, 4.4753, 0.0));
        ListenerInformationDoc doc = new ListenerInformationDoc(instant, receiver);
        String json = doc.format();

        Assert.assertNotNull(json);
    }

}
