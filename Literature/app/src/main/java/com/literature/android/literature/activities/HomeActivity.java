package com.literature.android.literature.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.literature.android.literature.Manager;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.PagerAdapter;
import com.literature.android.literature.tabFragments.Map;
import com.literature.android.literature.tabFragments.Writer;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mFacebookUserName;
    ImageView mFacebookUserPicture;
    public static final String FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME = "facebook.shared.user.connection.status";
    public static final String FACEBOOK_USER_ISCONNECTED = "facebook.user.isconnected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Manager.sharedManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        adapterViewPager.addTabPages(Writer.newInstance(getString(R.string.writer_title)));
        adapterViewPager.addTabPages(Map.newInstance(getString(R.string.map_title)));
        viewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(listener(viewPager));

        View header = navigationView.getHeaderView(0);
        mFacebookUserName = (TextView) header.findViewById(R.id.nav_facebook_user_name);
        mFacebookUserPicture = (ImageView) header.findViewById(R.id.nav_facebook_user_picture);
        initializeSharedPreferences();
    }

    private TabLayout.OnTabSelectedListener listener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_us) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_favorite:
                Intent favIntent = new Intent(this, FavoriteActivity.class);
                startActivity(favIntent);
                break;
            case R.id.nav_fb_login:
                Intent fbIntent = new Intent(this, LoginActivity.class);
                startActivity(fbIntent);
                break;
            case R.id.nav_manage:
                //TODO some action
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initializeSharedPreferences() {
        if (getSharedPreferences(FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                .contains(FACEBOOK_USER_ISCONNECTED)) {
            boolean isconnected = getSharedPreferences(FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                    .getBoolean(FACEBOOK_USER_ISCONNECTED, false);
            if (isconnected) {
                setUserProfileData();
            } else {
                setUserProfile(null, null);
            }
        } else {
            getSharedPreferences(FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                    .edit().putBoolean(FACEBOOK_USER_ISCONNECTED, false).commit();
            setUserProfile(null, null);
        }
    }

    public void setUserProfileData() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (null != token) {
            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        JSONObject userData = new JSONObject(object.toString());
                        String userName = userData.getString("name");
                        JSONObject userPicData = new JSONObject(userData.get("picture").toString());
                        JSONObject userProfilePicUrl = new JSONObject(userPicData.getString("data"));
                        String url = userProfilePicUrl.getString("url");
                        getSharedPreferences(HomeActivity.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                                .edit().putBoolean(HomeActivity.FACEBOOK_USER_ISCONNECTED, true).commit();
                        setUserProfile(userName, url);
                        return;
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
        setUserProfile(null, null);
    }

    /*
       Set User Profile Information in Navigation Bar.
     */
    public void setUserProfile(String userName, String picUrl) {
        if (null != userName && null != picUrl) {
            mFacebookUserName.setText(userName);
            Picasso.with(this).load(picUrl).into(mFacebookUserPicture);
            return;
        }
        mFacebookUserName.setText(getResources().getString(R.string.facebook_default_user));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFacebookUserPicture.setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon, getTheme()));
        } else {
            mFacebookUserPicture.setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon));
        }
    }
}
