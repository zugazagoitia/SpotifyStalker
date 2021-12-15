
package com.zugazagoitia.spotifystalker.data.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timestamp",
    "user",
    "track"
})
public class UserPlayingInfo implements Serializable
{

    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("user")
    private User user;
    @JsonProperty("track")
    private Track track;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1020007978776188465L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserPlayingInfo() {
    }

    /**
     * 
     * @param track
     * @param user
     * @param timestamp
     */
    public UserPlayingInfo(long timestamp, User user, Track track) {
        super();
        this.timestamp = timestamp;
        this.user = user;
        this.track = track;
    }

    @JsonProperty("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UserPlayingInfo withTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }

    public UserPlayingInfo withUser(User user) {
        this.user = user;
        return this;
    }

    @JsonProperty("track")
    public Track getTrack() {
        return track;
    }

    @JsonProperty("track")
    public void setTrack(Track track) {
        this.track = track;
    }

    public UserPlayingInfo withTrack(Track track) {
        this.track = track;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public UserPlayingInfo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
