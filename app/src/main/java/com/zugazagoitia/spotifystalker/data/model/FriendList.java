
package com.zugazagoitia.spotifystalker.data.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "profiles"
})
public class FriendList implements Serializable
{

    @JsonProperty("profiles")
    private List<Profile> profiles = new ArrayList<Profile>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4839877586673224580L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FriendList() {
    }

    /**
     * 
     * @param profiles
     */
    public FriendList(List<Profile> profiles) {
        super();
        this.profiles = profiles;
    }

    @JsonProperty("profiles")
    public List<Profile> getProfiles() {
        return profiles;
    }

    @JsonProperty("profiles")
    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public FriendList withProfiles(List<Profile> profiles) {
        this.profiles = profiles;
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

    public FriendList withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
