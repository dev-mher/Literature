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

import com.literature.android.literature.Manager;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.PagerAdapter;
import com.literature.android.literature.tabFragments.Story;
import com.literature.android.literature.tabFragments.Writer;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mFacebookUserName;
    ImageView mFacebookUserPicture;
    public static boolean isConnectedUserToFacebook;

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
        adapterViewPager.addTabPages(Story.newInstance(Story.class.getSimpleName()));
        viewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(listener(viewPager));

        View header = navigationView.getHeaderView(0);
        mFacebookUserName = (TextView) header.findViewById(R.id.nav_facebook_user_name);
        mFacebookUserPicture = (ImageView) header.findViewById(R.id.nav_facebook_user_picture);
        setUserProfileData();
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

    public void setUserProfileData() {
        String userName;
        String picUrl;
        Map<String, String> userData = Manager.sharedManager().getFacebookUserData();
        if (null != userData) {
            isConnectedUserToFacebook = true;
            userName = userData.get("userName").toString();
            picUrl = userData.get("url").toString();
            setUserProfile(userName, picUrl);
            return;
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
        isConnectedUserToFacebook = false;
        mFacebookUserName.setText(getResources().getString(R.string.facebook_default_user));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFacebookUserPicture.setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon, getTheme()));
        } else {
            mFacebookUserPicture.setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon));
        }
    }
}
