
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
    "imageUrl",
    "album",
    "artist",
    "context"
})
public class Track implements Serializable
{

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("name")
    private String name;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("album")
    private Album album;
    @JsonProperty("artist")
    private Artist artist;
    @JsonProperty("context")
    private Context context;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1688473816237846524L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Track() {
    }

    /**
     * 
     * @param artist
     * @param album
     * @param imageUrl
     * @param name
     * @param context
     * @param uri
     */
    public Track(String uri, String name, String imageUrl, Album album, Artist artist, Context context) {
        super();
        this.uri = uri;
        this.name = name;
        this.imageUrl = imageUrl;
        this.album = album;
        this.artist = artist;
        this.context = context;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public Track withUri(String uri) {
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

    public Track withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Track withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @JsonProperty("album")
    public Album getAlbum() {
        return album;
    }

    @JsonProperty("album")
    public void setAlbum(Album album) {
        this.album = album;
    }

    public Track withAlbum(Album album) {
        this.album = album;
        return this;
    }

    @JsonProperty("artist")
    public Artist getArtist() {
        return artist;
    }

    @JsonProperty("artist")
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Track withArtist(Artist artist) {
        this.artist = artist;
        return this;
    }

    @JsonProperty("context")
    public Context getContext() {
        return context;
    }

    @JsonProperty("context")
    public void setContext(Context context) {
        this.context = context;
    }

    public Track withContext(Context context) {
        this.context = context;
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

    public Track withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
