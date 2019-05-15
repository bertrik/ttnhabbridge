package nl.sikken.bertrik.hab.ttn;

import java.time.Instant;
import java.util.ArrayList;
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
        String time = "2017-08-23T17:18:02.509425571Z";
        List<TtnMessageGateway> gws = new ArrayList<>();
        TtnMessageMetaData data = new TtnMessageMetaData(time, gws);
        Instant instant = data.getTime();
        Assert.assertNotNull(instant);
        
        Sentence sentence = new Sentence("call", 0, instant);
        String line = sentence.format();
        Assert.assertTrue(line.contains("17:18:02"));
    }
    
}
