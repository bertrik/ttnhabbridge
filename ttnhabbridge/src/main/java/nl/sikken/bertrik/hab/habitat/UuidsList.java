package nl.sikken.bertrik.hab.habitat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * List of UUIDs.
 */
public final class UuidsList {
    
    @JsonProperty("uuids")
    private List<String> uuids;
    
    private UuidsList() {
        // jackson constructor
    }
    
    /**
     * Constructor.
     * 
     * @param list the items to initialize with
     */
    public UuidsList(Collection<String> list) {
        this();
        uuids = new ArrayList<>(list);
    }

    public List<String> getUuids() {
        return Collections.unmodifiableList(uuids);
    }
    
}
