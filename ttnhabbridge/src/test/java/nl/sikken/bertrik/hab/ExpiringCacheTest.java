package nl.sikken.bertrik.hab;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for expiring cache.
 */
public final class ExpiringCacheTest {

    /**
     * Verifies expiration behaviour.
     */
    @Test
    public void testExpiry() {
        ExpiringCache cache = new ExpiringCache(1);

        // add two items
        Instant date1 = Instant.now();
        Instant date2 = date1.plusMillis(500);
        Assert.assertTrue(cache.add("1", date1));
        Assert.assertTrue(cache.add("2", date2));
        
        // verify they are cached
        Instant date = date1.plusMillis(750); 
        Assert.assertFalse(cache.add("1", date));
        Assert.assertFalse(cache.add("2", date));
        
        // add another item, causing item 1 to expire, but not yet item 2 
        Instant date3 = date1.plusMillis(1250); 
        Assert.assertTrue(cache.add("3", date3));
        Assert.assertTrue(cache.add("1", date3));
        
        Assert.assertFalse(cache.add("1", date3));
        Assert.assertFalse(cache.add("2", date3));
        Assert.assertFalse(cache.add("3", date3));
    }
    
}
