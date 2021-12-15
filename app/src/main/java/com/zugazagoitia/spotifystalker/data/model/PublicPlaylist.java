
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
    "owner_name",
    "owner_uri"
})
public class PublicPlaylist {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("followers_count")
    private Integer followersCount;
    @JsonProperty("owner_name")
    private String ownerName;
    @JsonProperty("owner_uri")
    private String ownerUri;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public PublicPlaylist() {
    }

    /**
     * 
     * @param ownerName
     * @param imageUrl
     * @param name
     * @param ownerUri
     * @param followersCount
     * @param uri
     */
    public PublicPlaylist(String uri, String name, String imageUrl, Integer followersCount, String ownerName, String ownerUri) {
        super();
        this.uri = uri;
        this.name = name;
        this.imageUrl = imageUrl;
        this.followersCount = followersCount;
        this.ownerName = ownerName;
        this.ownerUri = ownerUri;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public PublicPlaylist withUri(String uri) {
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

    public PublicPlaylist withName(String name) {
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

    public PublicPlaylist withImageUrl(String imageUrl) {
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

    public PublicPlaylist withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    @JsonProperty("owner_name")
    public String getOwnerName() {
        return ownerName;
    }

    @JsonProperty("owner_name")
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public PublicPlaylist withOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    @JsonProperty("owner_uri")
    public String getOwnerUri() {
        return ownerUri;
    }

    @JsonProperty("owner_uri")
    public void setOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
    }

    public PublicPlaylist withOwnerUri(String ownerUri) {
        this.ownerUri = ownerUri;
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

    public PublicPlaylist withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
