package nl.sikken.bertrik.hab;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests of HAB telemetry sentence.
 */
public final class SentenceTest {

    /**
     * Verifies basic formatting.
     */
    @Test
    public void testSentence() {
        Instant instant = Instant.ofEpochSecond(0);
        Sentence sentence = new Sentence("CALL", 1, instant, 3.45, 6.78, 9.0);
        String s = sentence.format();

        Assert.assertEquals("$$CALL,1,00:00:00,3.450000,6.780000,9.0*25E9\n", s);
        Assert.assertNotNull(sentence.toString());
    }

    /**
     * Verifies that extra fields are formatted too.
     */
    @Test
    public void testSentenceExtras() {
        Sentence sentence = new Sentence("CALL", 1, Instant.now(), 3.45, 6.78, 9.0);
        sentence.addField("hello");
        String s = sentence.format();

        Assert.assertTrue(s.contains("hello"));
    }

}
