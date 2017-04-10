package com.literature.android.literature.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.literature.android.literature.Manager;
import com.literature.android.literature.R;

import org.json.JSONObject;

import static com.literature.android.literature.Manager.getContext;

public class LoginActivity extends AppCompatActivity {

    LoginButton mLoginButton;
    TextView mLoginStatus;
    CallbackManager mCallbackManager;
    ImageView mNavImageView;
    AccessTokenTracker fbTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        mLoginButton.setReadPermissions("email");
        mLoginStatus = (TextView) findViewById(R.id.fb_login_status_text_view);
        mNavImageView = (ImageView) findViewById(R.id.fb_owner_image_view);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest(loginResult.getAccessToken());
                mLoginStatus.setText("Login success");
            }

            @Override
            public void onCancel() {
                mLoginStatus.setText("Cancelled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Manager.sharedManager().removeFacebookUserData();
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    getSharedPreferences(HomeActivity.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                            .edit().putBoolean(HomeActivity.FACEBOOK_USER_ISCONNECTED, false).commit();
                    startActivity(intent);
                }
            }
        };
    }

    public void graphRequest(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    JSONObject userData = new JSONObject(object.toString());
                    String userName = userData.getString("name");
                    JSONObject userPicData = new JSONObject(userData.get("picture").toString());
                    JSONObject userProfilePicUrl = new JSONObject(userPicData.getString("data"));
                    String url = userProfilePicUrl.getString("url");
                    Manager.sharedManager().saveFacebookUserData(userName, url);
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    getSharedPreferences(HomeActivity.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                            .edit().putBoolean(HomeActivity.FACEBOOK_USER_ISCONNECTED, true).commit();
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR! an error occurred while getting user data");
                }
            }
        });
        Bundle b = new Bundle();
        b.putString("fields", "id,name,email,picture.width(100).height(100)");
        request.setParameters(b);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
