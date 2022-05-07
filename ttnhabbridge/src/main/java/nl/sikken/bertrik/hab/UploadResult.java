package nl.sikken.bertrik.hab;

import java.util.Locale;

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
    
    /**
     * Constructor.
     * 
     * @param ok whether upload was ok
     * @param id the doc id
     * @param rev the doc rev
     */
    public UploadResult(boolean ok, String id, String rev) {
        this();
        this.ok = ok;
        this.id = id;
        this.rev = rev;
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
        return String.format(Locale.ROOT, "ok=%s,id=%s,rev=%s", ok, id, rev);
    }
    
}
