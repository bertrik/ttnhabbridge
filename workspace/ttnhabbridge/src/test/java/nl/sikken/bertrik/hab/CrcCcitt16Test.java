package nl.sikken.bertrik.hab;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for CCITT CRC.
 */
public final class CrcCcitt16Test {

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

        final CrcCcitt16 crc = new CrcCcitt16();
        int value = crc.calculate(data, 0xFFFF);

        Assert.assertEquals(0x002A, value);
    }

}
