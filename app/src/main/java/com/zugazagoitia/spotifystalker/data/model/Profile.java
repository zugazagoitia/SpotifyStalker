
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
    "uri",
    "name",
    "image_url",
    "followers_count",
    "following_count"
})
public class Profile implements Serializable
{

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("followers_count")
    private int followersCount;
    @JsonProperty("following_count")
    private int followingCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5387054277563748043L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Profile() {
    }

    /**
     * 
     * @param imageUrl
     * @param name
     * @param followersCount
     * @param uri
     * @param followingCount
     */
    public Profile(String uri, String name, String imageUrl, int followersCount, int followingCount) {
        super();
        this.uri = uri;
        this.name = name;
        this.imageUrl = imageUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public Profile withUri(String uri) {
        this.uri = uri;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Profile withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Profile withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @JsonProperty("followers_count")
    public int getFollowersCount() {
        return followersCount;
    }

    @JsonProperty("followers_count")
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public Profile withFollowersCount(int followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    @JsonProperty("following_count")
    public int getFollowingCount() {
        return followingCount;
    }

    @JsonProperty("following_count")
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public Profile withFollowingCount(int followingCount) {
        this.followingCount = followingCount;
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

    public Profile withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
