package com.literature.android.literature.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.literature.android.literature.Model;

/**
 * Created by mher on 3/28/17.
 */

public class Database extends SQLiteOpenHelper {

    private SQLiteDatabase _db;
    private static Context sContext;

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "literatureBase.sqlite";
    private static final String AUTHORS_TABLE_NAME = "authors";
    private static final String RELATED_TABLE_NAME = "related";

    //authors table's columns
    private static final String AUTHOR_ID = "authorId";
    private static final String AUTHOR_FILE_NAME = "authorFileName";
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
            + AUTHOR_FILE_NAME
            + " TEXT DEFAULT (null),"
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

    public boolean saveAllInfo(List<List<Model>> allInfo) {
        for (int i = 0; i < allInfo.size(); ++i) {
            List<Model> author = allInfo.get(i);
            if (null != author.get(0) && null != author.get(0).getAuthorName()) {
                String authorName = (author.get(0).getAuthorName());
                String authorFileName = (author.get(0).getAuthorFileName());
                boolean result = saveAuthorNameToDB(authorFileName, authorName);
                if (!result) {
                    System.out.println("ERROR: an error occurred while "
                            + authorName + " author name saving");
                    return false;
                }
                for (int j = 0; j < author.size(); ++j) {
                    String caption = author.get(j).getCaption().get("caption");
                    int authorId = i + 1;
                    boolean saved = saveRelatedDataToDB(authorId, caption, 0);
                    if (!saved) {
                        System.out.println("ERROR: an error occurred while "
                                + authorName + " author's "
                                + caption + " saving");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean saveAuthorNameToDB(String authorFileName, String authorName) {
        Cursor cursor = null;
        try {
            cursor = _db.rawQuery("SELECT * FROM "
                    + AUTHORS_TABLE_NAME
                    + " WHERE "
                    + AUTHOR_NAME
                    + " = ? ", new String[]{authorName});
            if (0 < cursor.getCount()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put(AUTHOR_FILE_NAME, authorFileName);
        contentValues.put(AUTHOR_NAME, authorName);
        long result = _db.insert(AUTHORS_TABLE_NAME, null, contentValues);
        boolean isOK = (-1 != result);
        return isOK;
    }

    private boolean saveRelatedDataToDB(int authorId, String caption, int isFavorite) {
        Cursor cursor = null;
        try {
            cursor = _db.rawQuery("SELECT * FROM "
                    + RELATED_TABLE_NAME
                    + " WHERE "
                    + AUTHOR_ID
                    + " = ? AND " + CAPTION + " = ? ", new String[]{String.valueOf(authorId), caption});
            if (0 < cursor.getCount()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put(AUTHOR_ID, authorId);
        contentValues.put(CAPTION, caption);
        contentValues.put(IS_FAVORITE, isFavorite);
        long result = _db.insert(RELATED_TABLE_NAME, null, contentValues);
        boolean isOk = (-1 != result);
        return isOk;
    }

    public List<String> getAuthorsFileNames() {
        Cursor cursor = null;
        final List<String> authorsFileNames = new ArrayList<>();
        try {
            cursor = _db.rawQuery("SELECT "
                    + AUTHOR_FILE_NAME
                    + " FROM "
                    + AUTHORS_TABLE_NAME, new String[]{});
            while (cursor.moveToNext()) {
                authorsFileNames.add(cursor.getString(cursor.getColumnIndex(AUTHOR_FILE_NAME)));
            }
        } finally {
            cursor.close();
        }
        return authorsFileNames;
    }
}
