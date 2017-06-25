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
        checkForUpdate();
        Manager.sharedManager(this);
    }

    private void checkForUpdate() {
        if (getSharedPreferences(Constants.APP_VERSION_SHARED_PREFS_NAME, MODE_PRIVATE)
                .contains(Constants.APP_VERSION_SHARED_KEY)) {
            String appVersion = getSharedPreferences(Constants.APP_VERSION_SHARED_PREFS_NAME, MODE_PRIVATE)
                    .getString(Constants.APP_VERSION_SHARED_KEY, "");
            if (!appVersion.isEmpty()) {
                if (!appVersion.equals(Constants.APP_VERSION)) {
                    setAppVersion();
                } else {
                    Manager.isNeedToUpdate = false;
                }
            } else {
                setAppVersion();
            }
        } else {
            setAppVersion();
        }
    }

    private void setAppVersion() {
        Manager.isNeedToUpdate = true;
        getSharedPreferences(Constants.APP_VERSION_SHARED_PREFS_NAME, MODE_PRIVATE).edit()
                .putString(Constants.APP_VERSION_SHARED_KEY, Constants.APP_VERSION).apply();
    }
}
