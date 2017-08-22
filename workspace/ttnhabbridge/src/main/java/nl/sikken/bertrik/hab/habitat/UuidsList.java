package nl.sikken.bertrik.hab.habitat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author bertrik
 *
 */
public final class UuidsList {
    
    @JsonProperty("uuids")
    private List<String> uuids;

    public List<String> getUuids() {
        return uuids;
    }
    
}
