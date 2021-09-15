package nl.sikken.bertrik.hab.habitat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class HabitatConfig {

    @JsonProperty("url")
    private final String url; 
    
    @JsonProperty("timeout")
    private final int timeout;

    public HabitatConfig() {
        this("http://habitat.habhub.org", 60);
    }
    
    public HabitatConfig(String url, int timeout) {
        this.url = url;
        this.timeout = timeout;
    }

    public String getUrl() {
        return url;
    }

    public int getTimeout() {
        return timeout;
    }
    
}
