package com.literature.android.literature.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Caption;

import java.util.List;

public class CaptionActivity extends AppCompatActivity {

    public static final String CLICKED_ITEM_ID = "clickedItemID";
    int mAuthorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.caption_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuthorId = getIntent().getIntExtra(CLICKED_ITEM_ID, 0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.caption_activity_fragment_container
                , Caption.newInstance(Caption.class.getSimpleName(), mAuthorId))
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CaptionActivity.this);
                View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_dialog, null);
                builder.setView(layout);
                final AlertDialog alert = builder.create();
                ImageView close = (ImageView) layout.findViewById(R.id.alert_dialog_close_image_view);
                TextView about = (TextView) layout.findViewById(R.id.alert_dialog_text_view);
                List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
                if (null != allInfo && !allInfo.isEmpty()) {
                    List<Model> authorInfo = allInfo.get(mAuthorId);
                    if (null != authorInfo && !authorInfo.isEmpty()) {
                        String aboutAuthor = authorInfo.get(0).getAbout();
                        about.setText(aboutAuthor);
                    }
                }
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });
                alert.show();
            }
        });
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
