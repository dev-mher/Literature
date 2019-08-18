package com.literature.android.literature.activities;

import android.os.Bundle;

import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.favorite_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.favorite_activity_fragment_container
                , Favorite.newInstance(getString(R.string.favorite_title)))
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        int fragmentsCount = getSupportFragmentManager().getBackStackEntryCount();
        if (0 == fragmentsCount) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        return true;
    }

}
