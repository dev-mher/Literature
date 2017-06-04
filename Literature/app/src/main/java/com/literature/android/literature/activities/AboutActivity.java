package com.literature.android.literature.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.literature.android.literature.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button rateBtn = (Button) findViewById(R.id.about_rate_btn);
        rateBtn.setOnClickListener(this);
    }

    private void rateUs() {
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.about_rate_btn:
                rateUs();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
