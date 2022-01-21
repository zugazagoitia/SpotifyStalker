package com.zugazagoitia.spotifystalker.ui.login;

import static androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE;

import android.app.Activity;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.zugazagoitia.spotifystalker.MainActivity;
import com.zugazagoitia.spotifystalker.R;
import com.zugazagoitia.spotifystalker.SharedPropertiesConstants;
import com.zugazagoitia.spotifystalker.databinding.ActivityLoginBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {


    private FirebaseAnalytics mFirebaseAnalytics;

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final SwitchCompat rememberPassword = binding.rememberPassword;

        Objects.requireNonNull(rememberPassword).setChecked(true);

        //TODO add https://developer.android.com/training/sign-in/biometric-auth
        try {
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    SharedPropertiesConstants.MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                    .build();

            MasterKey masterKey = new MasterKey.Builder(LoginActivity.this)
                    .setKeyGenParameterSpec(spec)
                    .build();

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    LoginActivity.this,
                    SharedPropertiesConstants.SP_FILE_NAME,
                    masterKey, // masterKey created above
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            if (sharedPreferences.contains(SharedPropertiesConstants.SP_USERNAME_KEY)
                    && sharedPreferences.contains(SharedPropertiesConstants.SP_CREDENTIALS_KEY)
                    && sharedPreferences.contains(SharedPropertiesConstants.SP_DEVICE_ID)){

                usernameEditText.setText(sharedPreferences.getString(SharedPropertiesConstants.SP_USERNAME_KEY,null));

                loadingProgressBar.setVisibility(View.VISIBLE);

                loginViewModel.loginBlob(sharedPreferences.getString(SharedPropertiesConstants.SP_USERNAME_KEY,null),
                        sharedPreferences.getString(SharedPropertiesConstants.SP_CREDENTIALS_KEY,null),
                        sharedPreferences.getString(SharedPropertiesConstants.SP_DEVICE_ID,null));

            }
        } catch (GeneralSecurityException | IOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            boolean success = false;
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                success = true;
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            if(success) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }

        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        rememberPassword.isChecked(),
                        LoginActivity.this);
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    rememberPassword.isChecked(),
                    LoginActivity.this);
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}