package nl.sikken.bertrik.hab.habitat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class AmatuerSondehubConfig {

    @JsonProperty("url")
    private final String url; 
    
    @JsonProperty("timeout")
    private final int timeout;

    public AmatuerSondehubConfig() {
        this("https://api.v2.sondehub.org/amateur/telemetry", 60);
    }
    
    public AmatuerSondehubConfig(String url, int timeout) {
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
