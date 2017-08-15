package nl.sikken.bertrik.hab;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bertrik
 *
 */
public final class SentenceTest {

    @Test
    public void testSentence() throws UnsupportedEncodingException {
        final Sentence sentence = new Sentence("CALL", 1, new Date(0), 3.45, 6.78, 9.0);
        final String s = sentence.format();

        Assert.assertEquals("$$CALL,1,00:00:00,3.450000,6.780000,9.0*25E9\n", s);
    }

    @Test
    public void testSentenceExtras() throws UnsupportedEncodingException {
        final Sentence sentence = new Sentence("CALL", 1, new Date(), 3.45, 6.78, 9.0);
        sentence.addField("hello");
        final String s = sentence.format();

        Assert.assertTrue(s.contains("hello"));
    }

    /**
     * Verifies calculation of checksum
     * 
     * Known good string with good CRC:
     * $$hadie,181,10:42:10,54.422829,-6.741293,27799.3,1:10*002A
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testCrc() throws UnsupportedEncodingException {
        final String s = "hadie,181,10:42:10,54.422829,-6.741293,27799.3,1:10";
        final byte[] data = s.getBytes(StandardCharsets.US_ASCII);

        final CcittCrc16 crc = new CcittCrc16();
        int value = crc.calculate(data, 0xFFFF);

        Assert.assertEquals(0x002A, value);
    }

}
