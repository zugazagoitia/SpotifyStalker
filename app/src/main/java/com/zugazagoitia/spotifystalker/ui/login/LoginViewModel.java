package com.zugazagoitia.spotifystalker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;

import com.zugazagoitia.spotifystalker.data.LoginRepository;
import com.zugazagoitia.spotifystalker.data.Result;
import com.zugazagoitia.spotifystalker.data.TaskRunner;
import com.zugazagoitia.spotifystalker.data.model.LoggedInUser;
import com.zugazagoitia.spotifystalker.R;

import java.util.concurrent.Callable;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private TaskRunner taskRunner = new TaskRunner();

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    class LoginTask implements Callable<Result<LoggedInUser>> {
        private final String username;
        private final String password;
        private final boolean rememberPassword;
        private final Context ctx;

        public LoginTask(String username,String password,boolean rememberPassword,Context ctx) {
            this.username = username;
            this.password = password;
            this.rememberPassword = rememberPassword;
            this.ctx = ctx;
        }

        @Override
        public Result<LoggedInUser> call() {
            return loginRepository.login(username,password,rememberPassword,ctx);
        }
    }

    public void login(String username, String password,boolean rememberPassword, Context ctx) {
        // can be launched in a separate asynchronous job

        taskRunner.executeAsync(new LoginTask(username,password,rememberPassword,ctx), (result) -> {

            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUsername())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }

        });

    }

    class BlobLoginTask implements Callable<Result<LoggedInUser>> {
        private final String username;
        private final String blob;
        private final String deviceId;

        public BlobLoginTask(String username,String blob,String deviceId) {
            this.username = username;
            this.blob = blob;
            this.deviceId = deviceId;
        }

        @Override
        public Result<LoggedInUser> call() {
            return loginRepository.loginBlob(username,blob,deviceId);
        }
    }

    public void loginBlob(String username, String blob,String deviceId) {
        // can be launched in a separate asynchronous job

        taskRunner.executeAsync(new BlobLoginTask(username,blob,deviceId), (result) -> {

            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUsername())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }

        });

    }



    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null;
    }
}