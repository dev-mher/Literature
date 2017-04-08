package com.literature.android.literature.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.literature.android.literature.R;

import org.json.JSONObject;

import static com.literature.android.literature.Manager.getContext;

public class LoginActivity extends AppCompatActivity {

    public static final String FACEBOOK_USER_DATA = "facebook.profile.data";
    LoginButton mLoginButton;
    TextView mLoginStatus;
    CallbackManager mCallbackManager;
    ImageView mNavImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        mLoginButton.setReadPermissions("email,publish_actions");
        mLoginStatus = (TextView) findViewById(R.id.fb_login_status_text_view);
        mNavImageView = (ImageView) findViewById(R.id.fb_owner_image_view);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest(loginResult.getAccessToken());
                mLoginStatus.setText("Login success");
                HomeActivity.isConnectedUserToFacebook = true;
            }

            @Override
            public void onCancel() {
                mLoginStatus.setText("Cancelled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    public void graphRequest(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                //TODO parse the response and put it into the intent
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.putExtra(FACEBOOK_USER_DATA, object.toString());
                startActivity(intent);
            }
        });

        Bundle b = new Bundle();
        b.putString("fields", "id,name,email,picture.width(120).height(120)");
        request.setParameters(b);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
