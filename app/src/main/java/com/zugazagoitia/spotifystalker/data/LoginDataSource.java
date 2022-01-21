package com.zugazagoitia.spotifystalker.data;

import static androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.protobuf.ByteString;
import com.spotify.Authentication;
import com.spotify.connectstate.Connect;
import com.zugazagoitia.spotifystalker.SharedPropertiesConstants;
import com.zugazagoitia.spotifystalker.data.model.LoggedInUser;
import com.zugazagoitia.spotifystalker.data.model.RichProfile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.gianlu.librespot.common.Utils;
import xyz.gianlu.librespot.core.Session;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, boolean rememberPassword, Context ctx) {

        Session.Configuration conf = new Session.Configuration.Builder()
                .setStoreCredentials(false)
                .setCacheEnabled(false)
                .build();

        try (Session spotifySession = new Session.Builder(conf)
                .userPass(username,password)
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .create()){

            String token = spotifySession.tokens().get("user-read-currently-playing");
            LoggedInUser user = new LoggedInUser(token, username, getProfile(token,username));

            if(rememberPassword){
                try {
                    KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                            SharedPropertiesConstants.MASTER_KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                            .build();

                    MasterKey masterKey = new MasterKey.Builder(ctx)
                            .setKeyGenParameterSpec(spec)
                            .build();

                    SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                            ctx,
                            SharedPropertiesConstants.SP_FILE_NAME,
                            masterKey, // masterKey created above
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                    System.out.println("Valid: "+spotifySession.isValid());
                    String credentials = Utils.toBase64(spotifySession.apWelcome().getReusableAuthCredentials().toByteArray());
                    String deviceId = spotifySession.deviceId();
                    sharedPreferences.edit().clear()
                            .putString(SharedPropertiesConstants.SP_USERNAME_KEY, username)
                            .putString(SharedPropertiesConstants.SP_CREDENTIALS_KEY, credentials)
                            .putString(SharedPropertiesConstants.SP_DEVICE_ID,deviceId)
                            .apply();
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }

            }
            return new Result.Success<>(user);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> loginBlob(String username, String blob, String deviceId) {

        Session.Configuration conf = new Session.Configuration.Builder()
                .setStoreCredentials(false)
                .setCacheEnabled(false)
                .build();

        Authentication.LoginCredentials credentials = Authentication.LoginCredentials.newBuilder()
                .setUsername(username)
                .setAuthData(ByteString.copyFrom(Utils.fromBase64(blob)))
                .setTyp(Authentication.AuthenticationType.AUTHENTICATION_STORED_SPOTIFY_CREDENTIALS)
                .build();

        try (Session spotifySession = new Session.Builder(conf)
                .setDeviceId(deviceId)
                .credentials(credentials)
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .create()){

            String token = spotifySession.tokens().get("user-read-currently-playing");
            LoggedInUser user = new LoggedInUser(token, username, getProfile(token,username));


            return new Result.Success<>(user);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
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
            String responseBody = Objects.requireNonNull(response.body()).string();
            ObjectMapper objMapper = new ObjectMapper();
            return objMapper.readValue(responseBody, RichProfile.class);
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
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