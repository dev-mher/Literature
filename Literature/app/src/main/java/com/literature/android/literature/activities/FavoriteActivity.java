package com.literature.android.literature.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Favorite;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.favorite_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView backButton = (ImageView) findViewById(R.id.favorite_activity_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fragmentsCount = getSupportFragmentManager().getBackStackEntryCount();
                if (0 == fragmentsCount) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.favorite_activity_fragment_container
                , Favorite.newInstance(getString(R.string.favorite_title)))
                .commit();
    }
}
