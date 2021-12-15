
package com.zugazagoitia.spotifystalker.data.model;

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
    "is_following"
})
public class RecentlyPlayedArtist {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("followers_count")
    private Integer followersCount;
    @JsonProperty("is_following")
    private Boolean isFollowing;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public RecentlyPlayedArtist() {
    }

    /**
     * 
     * @param isFollowing
     * @param imageUrl
     * @param name
     * @param followersCount
     * @param uri
     */
    public RecentlyPlayedArtist(String uri, String name, String imageUrl, Integer followersCount, Boolean isFollowing) {
        super();
        this.uri = uri;
        this.name = name;
        this.imageUrl = imageUrl;
        this.followersCount = followersCount;
        this.isFollowing = isFollowing;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public RecentlyPlayedArtist withUri(String uri) {
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

    public RecentlyPlayedArtist withName(String name) {
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

    public RecentlyPlayedArtist withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @JsonProperty("followers_count")
    public Integer getFollowersCount() {
        return followersCount;
    }

    @JsonProperty("followers_count")
    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public RecentlyPlayedArtist withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    @JsonProperty("is_following")
    public Boolean getIsFollowing() {
        return isFollowing;
    }

    @JsonProperty("is_following")
    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public RecentlyPlayedArtist withIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
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

    public RecentlyPlayedArtist withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
