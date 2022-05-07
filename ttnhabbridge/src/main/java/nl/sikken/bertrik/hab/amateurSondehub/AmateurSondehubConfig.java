package nl.sikken.bertrik.hab.amateurSondehub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class AmateurSondehubConfig {

    @JsonProperty("url")
    private final String url;

    @JsonProperty("timeout")
    private final int timeout;

    public AmateurSondehubConfig() {
        this("https://api.v2.sondehub.org", 60);
    }

    public AmateurSondehubConfig(String url, int timeout) {
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
