package nl.sikken.bertrik.hab;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple expiring cache.
 */
public final class ExpiringCache {

    private final Map<String, Instant> map = new ConcurrentHashMap<>();
    private final long expiryTimeSec;

    /**
     * Constructor.
     * 
     * @param expiryTimeSec the expiry time (seconds)
     */
    public ExpiringCache(int expiryTimeSec) {
        this.expiryTimeSec = expiryTimeSec;
    }
    
    /**
     * Adds the item to the cache. 
     * 
     * @param id the id of the item
     * @param instant the current time
     * @return true if the item was newly added
     */
    public boolean add(String id, Instant instant) {
        cleanUp(instant);
        final Instant previous = map.putIfAbsent(id, instant);
        return previous == null;
    }

    /**
     * Cleans up the cache, removing all items from before the current date minus the expiration timeout.
     * 
     * @param now the current date
     */
    private void cleanUp(Instant now) {
        final Instant limit = now.minusSeconds(expiryTimeSec);
        map.forEach((k,v) -> {
            if (v.isBefore(limit)) {
                map.remove(k, v);
            }
        });
    }
    
}
