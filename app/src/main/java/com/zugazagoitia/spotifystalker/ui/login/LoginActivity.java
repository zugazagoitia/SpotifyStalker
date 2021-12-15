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
import androidx.security.crypto.MasterKeys;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zugazagoitia.spotifystalker.MainActivity;
import com.zugazagoitia.spotifystalker.R;
import com.zugazagoitia.spotifystalker.databinding.ActivityLoginBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class LoginActivity extends AppCompatActivity {

    private static final String SP_FILE_NAME = "app_secrets";
    private static final String MASTER_KEY_ALIAS = "_androidx_security_master_key_";
    private static final String SP_USERNAME_KEY = "ZG9uJ3QgbG9vayBtZSB1cCwgaSdtIHVzZWxlc3Mu";
    private static final String SP_PASSWORD_KEY = "aSdtIGFsc28gdXNlbGVzcyBkdW1iYXNz";

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final SwitchCompat rememberPassword = binding.rememberPassword;

        rememberPassword.setChecked(true);

        try {
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    MASTER_KEY_ALIAS,
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
                    SP_FILE_NAME,
                    masterKey, // masterKey created above
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            if (sharedPreferences.contains(SP_USERNAME_KEY) && sharedPreferences.contains(SP_PASSWORD_KEY)){
                usernameEditText.setText(sharedPreferences.getString(SP_USERNAME_KEY,null));
                passwordEditText.setText(sharedPreferences.getString(SP_PASSWORD_KEY,null));
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(sharedPreferences.getString(SP_USERNAME_KEY,null),
                        sharedPreferences.getString(SP_PASSWORD_KEY,null));
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
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
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
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

                    if(rememberPassword.isChecked()){
                        try {
                            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                                    MASTER_KEY_ALIAS,
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
                                    SP_FILE_NAME,
                                    masterKey, // masterKey created above
                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                            sharedPreferences.edit().clear()
                                    .putString(SP_USERNAME_KEY,usernameEditText.getText().toString())
                                    .putString(SP_PASSWORD_KEY,passwordEditText.getText().toString())
                                    .apply();
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                        }

                    }

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }

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
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}