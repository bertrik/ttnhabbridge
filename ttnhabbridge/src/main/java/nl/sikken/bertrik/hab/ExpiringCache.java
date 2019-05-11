package nl.sikken.bertrik.hab;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple expiring cache.
 */
public final class ExpiringCache {

    private final Map<String, Instant> map = new ConcurrentHashMap<>();
    private final Duration expiryTime;

    /**
     * Constructor.
     * 
     * @param expiryTime the expiry time
     */
    public ExpiringCache(Duration expiryTime) {
        this.expiryTime = expiryTime;
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
        Instant previous = map.putIfAbsent(id, instant);
        return previous == null;
    }

    /**
     * Cleans up the cache, removing all items from before the current date minus the expiration timeout.
     * 
     * @param now the current date
     */
    private void cleanUp(Instant now) {
        Instant limit = now.minus(expiryTime);
        map.forEach((k,v) -> {
            if (v.isBefore(limit)) {
                map.remove(k, v);
            }
        });
    }
    
}
