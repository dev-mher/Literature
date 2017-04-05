package com.literature.android.literature.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        boolean isAuthorsNamesExist = checkAuthorsExisting();
        if (isAuthorsNamesExist) {
            return true;
        }
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
                    boolean isSaved = saveRelatedDataToDB(authorId, caption, 0);
                    if (!isSaved) {
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


    public boolean checkAuthorsExisting() {
        Cursor cursor = null;
        try {
            cursor = _db.rawQuery("SELECT * FROM "
                    + AUTHORS_TABLE_NAME, new String[]{});
            if (0 < cursor.getCount()) {
                return true;
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return false;
    }

    public boolean saveAuthorNameToDB(String authorFileName, String authorName) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(AUTHOR_FILE_NAME, authorFileName);
        contentValues.put(AUTHOR_NAME, authorName);
        long result = _db.insert(AUTHORS_TABLE_NAME, null, contentValues);
        boolean isOK = (-1 != result);
        return isOK;
    }

    private boolean saveRelatedDataToDB(int authorId, String caption, int isFavorite) {
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
            if (null != cursor) {
                cursor.close();
            }
        }
        return authorsFileNames;
    }

    public int changeFavoriteStatus(int authorId, String caption, boolean isFavorite) {
        final String[] condition = new String[]{String.valueOf(authorId), caption};
        final ContentValues contentValues = new ContentValues();
        if (isFavorite) {
            contentValues.put(IS_FAVORITE, 1);
        } else {
            contentValues.put(IS_FAVORITE, 0);
        }
        int numberOfRows = _db.update(RELATED_TABLE_NAME, contentValues,
                AUTHOR_ID + " = ? AND " + CAPTION + " = ? ", condition);
        return numberOfRows;
    }

    public boolean checkCaptionStatus(int authorId, String caption) {
        final String[] condition = new String[]{String.valueOf(authorId), caption};
        Cursor cursor = null;
        try {
            cursor = _db.rawQuery("SELECT "
                    + IS_FAVORITE + " FROM "
                    + RELATED_TABLE_NAME
                    + " WHERE "
                    + AUTHOR_ID + " = ? AND "
                    + CAPTION + " = ? ", condition);
            while (cursor.moveToFirst()) {
                int isFavorite = cursor.getInt(cursor.getColumnIndex(IS_FAVORITE));
                if (0 == isFavorite) {
                    return false;
                }
                return true;
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        System.out.println("ERROR! an error occured while getting " + caption + " status");
        return false;
    }

    public List<Model> getFavoriteList() {
        List<Model> favoriteList = new ArrayList<>();
        Model model;
        Map<String, String> captionMap;
        Map<String, String> contentMap;
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        Cursor cursor = null;
        try {
            cursor = _db.rawQuery("SELECT "
                    + AUTHOR_ID + ","
                    + CAPTION
                    + " FROM "
                    + RELATED_TABLE_NAME
                    + " WHERE "
                    + IS_FAVORITE + " = ? ", new String[]{String.valueOf(1)});
            while (cursor.moveToNext()) {
                int authorIdForDb = cursor.getInt(cursor.getColumnIndex(AUTHOR_ID));
                int authorIdForList = authorIdForDb - 1;
                String caption = cursor.getString(cursor.getColumnIndex(CAPTION));
                List<Model> authorModel = allInfo.get(authorIdForList);
                for (int i = 0; i < authorModel.size(); ++i) {
                    if (authorModel.get(i).getCaption().get("caption").equals(caption)) {
                        String authorName = getAuthorNameById(authorIdForDb);
                        model = new Model();
                        captionMap = new HashMap<>();
                        captionMap.put("caption", caption);
                        model.setCaption(captionMap);
                        contentMap = new HashMap<>();
                        contentMap.put("content", authorModel.get(i).getContent().get("content"));
                        model.setContent(contentMap);
                        model.setAuthorName(authorName);
                        model.setAuthorId(authorIdForDb);
                        favoriteList.add(model);
                    }
                }
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return favoriteList;
    }

    private String getAuthorNameById(int authorId) {
        Cursor cursor = null;
        try {
            cursor = _db.rawQuery("SELECT "
                            + AUTHOR_NAME
                            + " FROM "
                            + AUTHORS_TABLE_NAME
                            + " WHERE "
                            + AUTHOR_ID + " = ? "
                    , new String[]{String.valueOf(authorId)});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(AUTHOR_NAME));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return null;
    }
}
