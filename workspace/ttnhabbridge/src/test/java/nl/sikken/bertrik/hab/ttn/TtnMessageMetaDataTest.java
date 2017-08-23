package nl.sikken.bertrik.hab.ttn;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.Sentence;

/**
 * Unit test for TtnMessageMetaData.
 */
public final class TtnMessageMetaDataTest {

    /**
     * Verifies time parsing.
     */
    @Test
    public void testTime() {
        final String time = "2017-08-23T17:18:02.509425571Z";
        final List<TtnMessageGateway> gws = new ArrayList<>();
        final TtnMessageMetaData data = new TtnMessageMetaData(time, gws);
        final Instant instant = data.getTime();
        Assert.assertNotNull(instant);
        
        final Sentence sentence = new Sentence("call", 0, Date.from(instant), 0.0, 0.0, 0.0);
        final String line = sentence.format();
        Assert.assertTrue(line.contains("17:18:02"));
    }
    
}
