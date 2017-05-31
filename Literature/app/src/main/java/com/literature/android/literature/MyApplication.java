package com.literature.android.literature;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by MherDavid on 28.05.2017.
 */

public class MyApplication extends Application {
    public static final String RATE_US_PREFS = "rate.us";
    public static final String FIRST_LAUNCH_KEY = "first.launch";
    public static final String DAYS_COUNT_KEY = "days.count";
    public static final String DONT_SHOW_KEY = "dont.show.again";
    public static final String LAUNCH_COUNT_KEY = "launch.count";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences(RATE_US_PREFS, MODE_PRIVATE);
        prefs.edit().putLong(FIRST_LAUNCH_KEY, System.currentTimeMillis()).apply();
        prefs.edit().putInt(DAYS_COUNT_KEY, 2).apply();
    }
}
