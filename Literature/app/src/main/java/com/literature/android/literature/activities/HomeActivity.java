package com.literature.android.literature.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView mNavigationView;
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
        if (null != getIntent() && null != getIntent().getStringExtra(LoginActivity.FACEBOOK_USER_DATA)) {
            String imageUrl = getIntent().getStringExtra(LoginActivity.FACEBOOK_USER_DATA);
            setNavigationHeader();
            setUserProfile(imageUrl);
        }
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
            case R.id.nav_fb_login:
                Intent fbIntent = new Intent(this, LoginActivity.class);
                startActivity(fbIntent);
            case R.id.nav_manage:
                //TODO some action
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setNavigationHeader() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_home, null);
        mNavigationView.addHeaderView(header);
        mFacebookUserName = (TextView) header.findViewById(R.id.nav_facebook_user_name);
        mFacebookUserPicture = (ImageView) header.findViewById(R.id.nav_facebook_user_picture);
    }

    /*
       Set User Profile Information in Navigation Bar.
     */
    public void setUserProfile(String userData) {
        //TODO move the parsing mechanism into the LoginActivity and here get data from intent
        try {
            JSONObject response = new JSONObject(userData);
            mFacebookUserName.setText(response.get("name").toString());
            JSONObject userPicData = new JSONObject(response.get("picture").toString());
            JSONObject userProfilePicUrl = new JSONObject(userPicData.getString("data"));
            Picasso.with(this).load(userProfilePicUrl.getString("url"))
                    .into(mFacebookUserPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
