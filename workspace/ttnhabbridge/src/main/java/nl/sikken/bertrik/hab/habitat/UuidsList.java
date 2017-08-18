package nl.sikken.bertrik.hab.habitat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author bertrik
 *
 */
public final class UuidsList {
    
    @JsonProperty("uuids")
    private List<String> uuids;
    
    private UuidsList() {
        // jackson constructor
    }
    
    private UuidsList(Collection<String> uuids) {
        this();
        uuids = new ArrayList<>(uuids);
    }

    public List<String> getUuids() {
        return uuids;
    }
    
}
