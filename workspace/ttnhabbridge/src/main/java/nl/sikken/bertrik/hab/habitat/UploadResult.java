package nl.sikken.bertrik.hab.habitat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UploadResult object for uploaded documents.
 */
public final class UploadResult {
    
    @JsonProperty("ok")
    private boolean ok;
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("rev")
    private String rev;
    
    private UploadResult() {
        // jackson constructor
    }

    public boolean isOk() {
        return ok;
    }

    public String getId() {
        return id;
    }

    public String getRev() {
        return rev;
    }
    
    @Override
    public String toString() {
        return String.format("ok=%s,id=%s,rev=%s", ok, id, rev);
    }
    
}
