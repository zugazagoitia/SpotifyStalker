package com.zugazagoitia.spotifystalker.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.connectstate.Connect;
import com.zugazagoitia.spotifystalker.data.model.LoggedInUser;
import com.zugazagoitia.spotifystalker.data.model.RichProfile;

import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.gianlu.librespot.core.Session;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        Session.Configuration conf = new Session.Configuration.Builder()
                .setStoreCredentials(false)
                .setCacheEnabled(false)
                .build();

        try (Session spotifySession = new Session.Builder(conf)
                .userPass(username,password)
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .create();){

            String token = spotifySession.tokens().get("user-read-currently-playing");

            LoggedInUser user = new LoggedInUser(token, username, getProfile(token,username));

            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public static RichProfile getProfile(String token, String username){
        String url = MessageFormat.format("https://spclient.wg.spotify.com/user-profile-view/v3/profile/{0}", username);

        OkHttpClient client = new OkHttpClient();

        //Call api using okhttp
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();


        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            ObjectMapper objMapper = new ObjectMapper();
            RichProfile profile = objMapper.readValue(responseBody, RichProfile.class);
            return profile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public enum HttpProtocol{
        HTTP,HTTPS;

        @Override
        public String toString(){
            return super.toString().toLowerCase();
        }
    }
}