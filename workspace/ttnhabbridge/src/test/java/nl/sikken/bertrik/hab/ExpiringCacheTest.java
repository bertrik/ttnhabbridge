package nl.sikken.bertrik.hab;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import nl.sikken.bertrik.hab.ExpiringCache;

/**
 * Unit test for expiring cache.
 */
public final class ExpiringCacheTest {

    /**
     * Verifies expiration behaviour.
     */
    @Test
    public void testExpiry() {
        final ExpiringCache cache = new ExpiringCache(1);

        // add two items
        final Date date1 = new Date();
        final Date date2 = new Date(date1.getTime() + 500);
        Assert.assertTrue(cache.add("1", date1));
        Assert.assertTrue(cache.add("2", date2));
        
        // verify they are cached
        final Date date = new Date(date1.getTime() + 750);
        Assert.assertFalse(cache.add("1", date));
        Assert.assertFalse(cache.add("2", date));
        
        // add another item, causing item 1 to expire, but not yet item 2 
        final Date date3 = new Date(date1.getTime() + 1250);
        Assert.assertTrue(cache.add("3", date3));
        Assert.assertTrue(cache.add("1", date3));
        
        Assert.assertFalse(cache.add("1", date3));
        Assert.assertFalse(cache.add("2", date3));
        Assert.assertFalse(cache.add("3", date3));
    }
    
}
