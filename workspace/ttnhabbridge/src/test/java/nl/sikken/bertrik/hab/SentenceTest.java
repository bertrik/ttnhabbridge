package nl.sikken.bertrik.hab;

import java.util.Date;

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
        final Sentence sentence = new Sentence("CALL", 1, new Date(0), 3.45, 6.78, 9.0);
        final String s = sentence.format();

        Assert.assertEquals("$$CALL,1,00:00:00,3.450000,6.780000,9.0*25E9\n", s);
    }

    /**
     * Verifies that extra fields are formatted too.
     */
    @Test
    public void testSentenceExtras() {
        final Sentence sentence = new Sentence("CALL", 1, new Date(), 3.45, 6.78, 9.0);
        sentence.addField("hello");
        final String s = sentence.format();

        Assert.assertTrue(s.contains("hello"));
    }

}
