package com.literature.android.literature.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.literature.android.literature.Manager;
import com.literature.android.literature.MyApplication;
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
        rateUs();
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
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
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
                    .edit().putBoolean(FACEBOOK_USER_ISCONNECTED, false).apply();
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
                                .edit().putBoolean(HomeActivity.FACEBOOK_USER_ISCONNECTED, true).apply();
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
            mFacebookUserPicture.setImageDrawable(getResources().getDrawable(R.drawable.header_icon, getTheme()));
        } else {
            mFacebookUserPicture.setImageDrawable(getResources().getDrawable(R.drawable.header_icon));
        }
    }

    private void rateUs() {
        SharedPreferences prefs = getSharedPreferences(MyApplication.RATE_US_PREFS, MODE_PRIVATE);
        if (null != prefs) {
            SharedPreferences.Editor editor = prefs.edit();
            if (prefs.getBoolean(MyApplication.DONT_SHOW_KEY, false)) {
                return;
            }
            int launchCount = prefs.getInt(MyApplication.LAUNCH_COUNT_KEY, 0) + 1;
            editor.putInt(MyApplication.LAUNCH_COUNT_KEY, launchCount).apply();
            if (launchCount > 3) {
                editor.putInt(MyApplication.LAUNCH_COUNT_KEY, 0).apply();
                long firsLaunch = prefs.getLong(MyApplication.FIRST_LAUNCH_KEY, 0);
                int daysCount = prefs.getInt(MyApplication.DAYS_COUNT_KEY, 2);
                if (System.currentTimeMillis() > firsLaunch + (daysCount * 24 * 60 * 60 * 1000)) {
                    showAlertRate(prefs);
                }
            }
        }
    }

    private void showAlertRate(final SharedPreferences prefs) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View alertView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_common, null);
        TextView title = (TextView) alertView.findViewById(R.id.msg_alert_title);
        title.setText(R.string.rate_dialog_title);
        TextView msg = (TextView) alertView.findViewById(R.id.msg_alert_content);
        msg.setText(R.string.rate_dialog_content);
        builder.setView(alertView);
        builder.setCancelable(false)
                .setPositiveButton(R.string.rate_dialog_rate_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //turn off reminder
                        prefs.edit().putBoolean(MyApplication.DONT_SHOW_KEY, true).apply();
                        try {
                            //open through Play Market app
                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent market = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(market);
                        } catch (final ActivityNotFoundException ex) {
                            //open through browser
                            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
                            Intent market = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(market);
                        }
                        dialog.dismiss();
                    }
                }).setNeutralButton(R.string.rate_dialog_no_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //turn off reminder
                prefs.edit().putBoolean(MyApplication.DONT_SHOW_KEY, true).apply();
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.rate_dialog_later_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int daysCount = prefs.getInt(MyApplication.DAYS_COUNT_KEY, 2);
                //remind after 2 days
                prefs.edit().putInt(MyApplication.DAYS_COUNT_KEY, daysCount + 2).apply();
                dialog.dismiss();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
