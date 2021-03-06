package com.zugazagoitia.spotifystalker.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String token;
    private final String username;
    private final RichProfile user;

    public LoggedInUser(String token, String username, RichProfile user) {
        this.token = token;
        this.username = username;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public RichProfile getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

}