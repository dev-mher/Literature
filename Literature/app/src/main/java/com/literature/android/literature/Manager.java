package com.literature.android.literature;

import android.content.Context;

import com.literature.android.literature.database.Database;

import java.util.List;

/**
 * Created by mher on 3/28/17.
 */

public class Manager {

    private List<Model> mModel;
    private Database mDb;
    private static Context context;

    private static class SingletonHolder {
        private static final Manager INSTANCE = new Manager();
    }

    private Manager() {
        mDb = Database.getInstance(context);
    }

    public static Manager sharedManager(Context ctx) {
        context = ctx;
        return SingletonHolder.INSTANCE;
    }

    public static Context getContext() {
        return context;
    }
}
