package com.literature.android.literature.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Caption;

public class CaptionActivity extends AppCompatActivity {

    public static final String CLICKED_ITEM_ID = "clickedItemID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.caption_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView backButton = (ImageView) findViewById(R.id.caption_activity_back_button);
        int itemId = getIntent().getIntExtra(CLICKED_ITEM_ID, 0);
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
        fragmentManager.beginTransaction().replace(R.id.caption_activity_fragment_container
                , Caption.newInstance(Caption.class.getSimpleName(), itemId))
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
