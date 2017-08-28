package nl.sikken.bertrik.hab;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple expiring cache.
 */
public final class ExpiringCache {

    private final Map<String, Date> map = new ConcurrentHashMap<String, Date>();
    private final long expiryTimeMs;

    /**
     * Constructor.
     * 
     * @param expiryTimeSec the expiry time (seconds)
     */
    public ExpiringCache(int expiryTimeSec) {
        this.expiryTimeMs = expiryTimeSec * 1000L;
    }
    
    /**
     * Adds the item to the cache. 
     * 
     * @param id the id of the item
     * @param date the date
     * @return true if the item was newly added
     */
    public boolean add(String id, Date date) {
        cleanUp(date);
        final Date previous = map.putIfAbsent(id, date);
        return previous == null;
    }

    /**
     * Cleans up the cache, removing all items from before the current date minus the expiration timeout.
     * 
     * @param now the current date
     */
    private void cleanUp(Date now) {
        final Date limit = new Date(now.getTime() - expiryTimeMs);
        map.forEach((k,v) -> {
            if (v.before(limit)) {
                map.remove(k, v);
            }
        });
    }
    
}
