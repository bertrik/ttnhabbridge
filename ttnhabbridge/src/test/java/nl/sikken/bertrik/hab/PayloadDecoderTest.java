package nl.sikken.bertrik.hab;

import java.time.Instant;
import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.lorawan.LoraWanUplinkMessage;

/**
 * Unit tests for PayloadDecoder.
 */
public final class PayloadDecoderTest {

    /**
     * Verifies that an unknown payload encoding is detected.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidEncoding() {
        PayloadDecoder decoder = new PayloadDecoder(null);
        Assert.assertNotNull(decoder);
    }

    /**
     * Verifies decoding of another set of actual payload data.
     * 
     * @throws DecodeException
     */
    @Test
    public void testCayenne2() throws DecodeException {
        LoraWanUplinkMessage message = new LoraWanUplinkMessage(Instant.parse("2020-02-05T22:00:58.930936Z"), "test", "test",
                123, 1, Base64.getDecoder().decode("AYgH1ecAzV4AC7gCZwArAwIBhg=="), false);
        // decode payload
        PayloadDecoder decoder = new PayloadDecoder(EPayloadEncoding.CAYENNE);
        Sentence sentence = decoder.decode(message);
        Assert.assertEquals("$$test,123,22:00:58,51.3511,5.2574,30.00,4.3,3.90*A07E\n", sentence.format());
    }

}
