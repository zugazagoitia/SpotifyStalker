
package com.zugazagoitia.spotifystalker.data.model;

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
    "uri",
    "name",
    "image_url",
    "followers_count",
    "following_count",
    "is_following",
    "recently_played_artists",
    "public_playlists",
    "total_public_playlists_count",
    "has_spotify_image",
    "color"
})
public class RichProfile {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("followers_count")
    private Integer followersCount;
    @JsonProperty("following_count")
    private Integer followingCount;
    @JsonProperty("is_following")
    private Boolean isFollowing;
    @JsonProperty("recently_played_artists")
    private List<RecentlyPlayedArtist> recentlyPlayedArtists = null;
    @JsonProperty("public_playlists")
    private List<PublicPlaylist> publicPlaylists = null;
    @JsonProperty("total_public_playlists_count")
    private Integer totalPublicPlaylistsCount;
    @JsonProperty("has_spotify_image")
    private Boolean hasSpotifyImage;
    @JsonProperty("color")
    private Integer color;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public RichProfile() {
    }

    /**
     * 
     * @param isFollowing
     * @param color
     * @param imageUrl
     * @param name
     * @param hasSpotifyImage
     * @param followersCount
     * @param uri
     * @param followingCount
     * @param recentlyPlayedArtists
     * @param publicPlaylists
     * @param totalPublicPlaylistsCount
     */
    public RichProfile(String uri, String name, String imageUrl, Integer followersCount, Integer followingCount, Boolean isFollowing, List<RecentlyPlayedArtist> recentlyPlayedArtists, List<PublicPlaylist> publicPlaylists, Integer totalPublicPlaylistsCount, Boolean hasSpotifyImage, Integer color) {
        super();
        this.uri = uri;
        this.name = name;
        this.imageUrl = imageUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.isFollowing = isFollowing;
        this.recentlyPlayedArtists = recentlyPlayedArtists;
        this.publicPlaylists = publicPlaylists;
        this.totalPublicPlaylistsCount = totalPublicPlaylistsCount;
        this.hasSpotifyImage = hasSpotifyImage;
        this.color = color;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public RichProfile withUri(String uri) {
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

    public RichProfile withName(String name) {
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

    public RichProfile withImageUrl(String imageUrl) {
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

    public RichProfile withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    @JsonProperty("following_count")
    public Integer getFollowingCount() {
        return followingCount;
    }

    @JsonProperty("following_count")
    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public RichProfile withFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
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

    public RichProfile withIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
        return this;
    }

    @JsonProperty("recently_played_artists")
    public List<RecentlyPlayedArtist> getRecentlyPlayedArtists() {
        return recentlyPlayedArtists;
    }

    @JsonProperty("recently_played_artists")
    public void setRecentlyPlayedArtists(List<RecentlyPlayedArtist> recentlyPlayedArtists) {
        this.recentlyPlayedArtists = recentlyPlayedArtists;
    }

    public RichProfile withRecentlyPlayedArtists(List<RecentlyPlayedArtist> recentlyPlayedArtists) {
        this.recentlyPlayedArtists = recentlyPlayedArtists;
        return this;
    }

    @JsonProperty("public_playlists")
    public List<PublicPlaylist> getPublicPlaylists() {
        return publicPlaylists;
    }

    @JsonProperty("public_playlists")
    public void setPublicPlaylists(List<PublicPlaylist> publicPlaylists) {
        this.publicPlaylists = publicPlaylists;
    }

    public RichProfile withPublicPlaylists(List<PublicPlaylist> publicPlaylists) {
        this.publicPlaylists = publicPlaylists;
        return this;
    }

    @JsonProperty("total_public_playlists_count")
    public Integer getTotalPublicPlaylistsCount() {
        return totalPublicPlaylistsCount;
    }

    @JsonProperty("total_public_playlists_count")
    public void setTotalPublicPlaylistsCount(Integer totalPublicPlaylistsCount) {
        this.totalPublicPlaylistsCount = totalPublicPlaylistsCount;
    }

    public RichProfile withTotalPublicPlaylistsCount(Integer totalPublicPlaylistsCount) {
        this.totalPublicPlaylistsCount = totalPublicPlaylistsCount;
        return this;
    }

    @JsonProperty("has_spotify_image")
    public Boolean getHasSpotifyImage() {
        return hasSpotifyImage;
    }

    @JsonProperty("has_spotify_image")
    public void setHasSpotifyImage(Boolean hasSpotifyImage) {
        this.hasSpotifyImage = hasSpotifyImage;
    }

    public RichProfile withHasSpotifyImage(Boolean hasSpotifyImage) {
        this.hasSpotifyImage = hasSpotifyImage;
        return this;
    }

    @JsonProperty("color")
    public Integer getColor() {
        return color;
    }

    @JsonProperty("color")
    public void setColor(Integer color) {
        this.color = color;
    }

    public RichProfile withColor(Integer color) {
        this.color = color;
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

    public RichProfile withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
