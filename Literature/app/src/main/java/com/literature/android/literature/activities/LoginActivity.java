package com.literature.android.literature.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.literature.android.literature.Constants;
import com.literature.android.literature.R;

import androidx.appcompat.app.AppCompatActivity;

import static com.literature.android.literature.Manager.getContext;

public class LoginActivity extends AppCompatActivity {

    LoginButton mLoginButton;
    TextView mLoginStatus;
    CallbackManager mCallbackManager;
    AccessTokenTracker fbTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mLoginButton = findViewById(R.id.fb_login_button);
        mLoginButton.setPublishPermissions(Constants.FACEBOOK_PUBLISH_ACTIONS);
        mLoginStatus = findViewById(R.id.fb_login_status_text_view);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                        .edit().putBoolean(Constants.FACEBOOK_USER_ISCONNECTED, true).apply();
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                mLoginStatus.setText(getString(R.string.facebook_cancelled));
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                            .edit().putBoolean(Constants.FACEBOOK_USER_ISCONNECTED, false).apply();
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
