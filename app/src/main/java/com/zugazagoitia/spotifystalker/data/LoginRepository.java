package com.zugazagoitia.spotifystalker.data;

import android.content.Context;

import com.zugazagoitia.spotifystalker.data.model.LoggedInUser;
import com.zugazagoitia.spotifystalker.data.model.RichProfile;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;


    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public static boolean isLoggedIn() {
        if (instance==null) return false;
        else return instance.user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    public static String getToken(){
        if (instance==null) return null;
        else return instance.user.getToken();
    }

    public static String getUsername(){
        if (instance==null) return null;
        else return instance.user.getUsername();
    }

    public static RichProfile getUserProfile(){
        if (instance==null) return null;
        else return instance.user.getUser();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password,boolean rememberPassword, Context ctx) {

        Result<LoggedInUser> result = dataSource.login(username, password,rememberPassword,ctx);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public Result<LoggedInUser> loginBlob(String username, String blob,String deviceId){
        Result<LoggedInUser> result = dataSource.loginBlob(username,blob,deviceId);
        if (result instanceof Result.Success){
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}