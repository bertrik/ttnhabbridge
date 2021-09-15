package nl.sikken.bertrik.hab.lorawan;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class MqttConfig {
    
    @JsonProperty("url")
    private final String url;

    @JsonProperty("user")
    private final String user;

    @JsonProperty("pass")
    private final String pass;

    @JsonProperty("topic")
    private final String topic;

    // jackson no-arg constructor
    MqttConfig() {
        this("", "", "", "");
    }
    
    public MqttConfig(String url, String user, String pass, String topic) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.topic = topic;
    }
    
    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getTopic() {
        return topic;
    }

}
