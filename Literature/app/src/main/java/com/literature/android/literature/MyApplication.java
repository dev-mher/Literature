package com.literature.android.literature;

import android.app.Application;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by MherDavid on 28.05.2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        SharedPreferences prefs = getSharedPreferences(Constants.RATE_US_PREFS, MODE_PRIVATE);
        prefs.edit().putLong(Constants.FIRST_LAUNCH_KEY, System.currentTimeMillis()).apply();
        prefs.edit().putInt(Constants.DAYS_COUNT_KEY, 2).apply();
    }
}
