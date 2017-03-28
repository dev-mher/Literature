package com.literature.android.literature.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mher on 3/28/17.
 */

public class Database extends SQLiteOpenHelper {

    private SQLiteDatabase _db;
    private static Context sContext;

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "literatureBase.db";
    private static final String AUTHORS_TABLE_NAME = "authors";
    private static final String RELATED_TABLE_NAME = "related";

    //authors table's columns
    private static final String AUTHOR_ID = "authorId";
    private static final String AUTHOR_NAME = "authorName";

    //related table's columns
    private static final String CAPTION = "caption";
    private static final String IS_FAVORITE = "isFavorite";

    //create authors table
    private static final String CREATE_AUTHORS_TABLE
            = "CREATE TABLE IF NOT EXISTS "
            + AUTHORS_TABLE_NAME
            + "("
            + AUTHOR_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + AUTHOR_NAME
            + " TEXT DEFAULT (null)"
            + ")";

    //create related table
    private static final String CREATE_RELATED_TABLE
            = "CREATE TABLE IF NOT EXISTS "
            + RELATED_TABLE_NAME
            + "("
            + AUTHOR_ID
            + " INTEGER,"
            + CAPTION
            + " TEXT DEFAULT (null),"
            + IS_FAVORITE
            + " INTEGER"
            + ")";


    private static class SingletonHolder {
        private static final Database INSTANCE = new Database();
    }

    private Database() {
        super(sContext, DATABASE_NAME, null, VERSION);
        _db = getWritableDatabase();
    }

    public static Database getInstance(Context ctx) {
        sContext = ctx;
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_AUTHORS_TABLE);
        database.execSQL(CREATE_RELATED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
