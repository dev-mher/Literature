package com.literature.android.literature.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.literature.android.literature.Constants;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.PagerAdapter;
import com.literature.android.literature.tabFragments.Map;
import com.literature.android.literature.tabFragments.Writer;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mFacebookUserName;
    ImageView mFacebookUserPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_text_view);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ArmBook.ttf");
        textView.setTypeface(font);
        textView.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewpager);
        PagerAdapter adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        adapterViewPager.addTabPages(Writer.newInstance(getString(R.string.writer_title)));
        adapterViewPager.addTabPages(Map.newInstance(getString(R.string.map_title)));
        viewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(listener(viewPager));

        View header = navigationView.getHeaderView(0);
        mFacebookUserName = header.findViewById(R.id.nav_facebook_user_name);
        mFacebookUserPicture = header.findViewById(R.id.nav_facebook_user_picture);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
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
            case R.id.nav_fb_login:
                Intent fbIntent = new Intent(this, LoginActivity.class);
                startActivity(fbIntent);
                break;
            case R.id.nav_favorite:
                Intent favIntent = new Intent(this, FavoriteActivity.class);
                startActivity(favIntent);
                break;
            case R.id.nav_rate:
                openRatePage();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openRatePage() {
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
    }

    public void initializeSharedPreferences() {
        if (getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                .contains(Constants.FACEBOOK_USER_ISCONNECTED)) {
            boolean isconnected = getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                    .getBoolean(Constants.FACEBOOK_USER_ISCONNECTED, false);
            if (isconnected) {
                setUserProfileData();
            } else {
                setUserProfile(null, null);
            }
        } else {
            getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                    .edit().putBoolean(Constants.FACEBOOK_USER_ISCONNECTED, false).apply();
            setUserProfile(null, null);
        }
    }

    public void setUserProfileData() {
        setUserProfile(null, null);
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (null != token) {
            GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
                try {
                    JSONObject userData = new JSONObject(object.toString());
                    String userName = userData.getString(Constants.FACEBOOK_USER_NAME);
                    JSONObject userPicData = new JSONObject(userData.get(Constants.FACEBOOK_USER_PICTURE).toString());
                    JSONObject userProfilePicUrl = new JSONObject(userPicData.getString(Constants.FACEBOOK_USER_DATA));
                    String url = userProfilePicUrl.getString(Constants.FACEBOOK_USER_PICTURE_URL);
                    getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME, MODE_PRIVATE)
                            .edit().putBoolean(Constants.FACEBOOK_USER_ISCONNECTED, true).apply();
                    setUserProfile(userName, url);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR! an error occurred while getting user data");
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FACEBOOK_FIELDS, "id,name,email,picture.width(600).height(600)");
            request.setParameters(bundle);
            request.executeAsync();
        }
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
        SharedPreferences prefs = getSharedPreferences(Constants.RATE_US_PREFS, MODE_PRIVATE);
        if (null != prefs) {
            SharedPreferences.Editor editor = prefs.edit();
            if (prefs.getBoolean(Constants.DONT_SHOW_KEY, false)) {
                return;
            }
            int launchCount = prefs.getInt(Constants.LAUNCH_COUNT_KEY, 0) + 1;
            editor.putInt(Constants.LAUNCH_COUNT_KEY, launchCount).apply();
            if (launchCount > 3) {
                editor.putInt(Constants.LAUNCH_COUNT_KEY, 0).apply();
                long firsLaunch = prefs.getLong(Constants.FIRST_LAUNCH_KEY, 0);
                int daysCount = prefs.getInt(Constants.DAYS_COUNT_KEY, 2);
                if (System.currentTimeMillis() > firsLaunch + (daysCount * 24 * 60 * 60 * 1000)) {
                    showAlertRate(prefs);
                }
            }
        }
    }

    private void showAlertRate(final SharedPreferences prefs) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View alertView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_common, null);
        TextView title = alertView.findViewById(R.id.msg_alert_title);
        title.setText(R.string.rate_dialog_title);
        TextView msg = (TextView) alertView.findViewById(R.id.msg_alert_content);
        msg.setText(R.string.rate_dialog_content);
        builder.setView(alertView);
        builder.setCancelable(false)
                .setPositiveButton(R.string.rate_dialog_rate_btn, (dialog, which) -> {
                    //turn off reminder
                    prefs.edit().putBoolean(Constants.DONT_SHOW_KEY, true).apply();
                    openRatePage();
                    dialog.dismiss();
                }).setNeutralButton(R.string.rate_dialog_no_btn, (dialog, which) -> {
            //turn off reminder
            prefs.edit().putBoolean(Constants.DONT_SHOW_KEY, true).apply();
            dialog.dismiss();
        }).setNegativeButton(R.string.rate_dialog_later_btn, (dialog, which) -> {
            int daysCount = prefs.getInt(Constants.DAYS_COUNT_KEY, 2);
            //remind after 2 days
            prefs.edit().putInt(Constants.DAYS_COUNT_KEY, daysCount + 2).apply();
            dialog.dismiss();
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
