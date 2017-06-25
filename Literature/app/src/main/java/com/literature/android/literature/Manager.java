package com.literature.android.literature;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Toast;

import com.literature.android.literature.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by mher on 3/28/17.
 */

public class Manager {

    private List<List<Model>> allAuthorsInfo;
    private Database mDb;
    private static Context context;
    public static boolean isNeedToUpdate;

    private static class SingletonHolder {
        private static final Manager INSTANCE = new Manager();
    }

    private Manager() {
        mDb = Database.getInstance(context);
        allAuthorsInfo = runParserThread();
        addAllInfoToDB();
    }

    public static Manager sharedManager(Context ctx) {
        context = ctx;
        return SingletonHolder.INSTANCE;
    }

    public static Manager sharedManager() {
        return SingletonHolder.INSTANCE;
    }

    public static Context getContext() {
        return context;
    }

    public List<List<Model>> getAllAuthorsInfo() {
        return allAuthorsInfo;
    }

    private List<List<Model>> runParserThread() {
        try {
            return new JsonParser().execute(Constants.BASE_FILE_NAME).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAllInfoToDB() {
        boolean result = mDb.saveAllInfo(getAllAuthorsInfo());
        if (!result) {
            System.out.println("ERROR: an error occurred while data saving");
        }
    }

    private class JsonParser extends AsyncTask<String, Void, List<List<Model>>> {

        @Override
        protected List<List<Model>> doInBackground(String... strings) {
            String baseFileName = strings[0];
            List<List<Model>> allAuthorsInfo = new ArrayList<>();
            List<String> authorsFilesNames = getAuthorsFilesNames(baseFileName);
            if (null != authorsFilesNames) {
                for (int i = 0; i < authorsFilesNames.size(); ++i) {
                    List<Model> authorInfo = parseAuthorJson(authorsFilesNames.get(i));
                    allAuthorsInfo.add(authorInfo);
                }
            }
            return allAuthorsInfo;
        }

        @Override
        protected void onPostExecute(List<List<Model>> allInfo) {
            super.onPostExecute(allInfo);
        }

        List<String> getAuthorsFilesNames(String fileName) {
            List<String> namesList = new ArrayList<>();
            String allNames = loadJson(fileName);
            try {
                JSONObject mainObject = new JSONObject(allNames);
                JSONArray names = mainObject.getJSONArray(Constants.ALL_AUTHORS_NAMES_KEY);
                for (int i = 0; i < names.length(); ++i) {
                    namesList.add(names.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return namesList;
        }

        List<Model> parseAuthorJson(String fileName) {
            List<Model> authorModels = null;
            Map<String, String> captionMap;
            Map<String, String> contentMap;
            try {
                JSONObject mainObject = new JSONObject(loadJson(fileName));
                authorModels = new ArrayList<>();
                Model model;
                final String authorFileName = mainObject.getString(Constants.AUTHOR_FILE_NAME_KEY);
                final String authorName = mainObject.getString(Constants.AUTHOR_NAME_KEY);
                final String aboutShort = mainObject.getString(Constants.ABOUT_SHORT_KEY);
                final String about = mainObject.getString(Constants.ABOUT_KEY);
                final JSONArray authorList = mainObject.getJSONArray(Constants.LIST_KEY);
                for (int i = 0; i < authorList.length(); ++i) {
                    model = new Model();
                    captionMap = new HashMap<>();
                    contentMap = new HashMap<>();
                    final JSONObject listObject = authorList.getJSONObject(i);
                    final String caption = listObject.getString(Constants.CAPTION_KEY);
                    final String content = listObject.getString(Constants.CONTENT_KEY);
                    model.setAuthorFileName(authorFileName);
                    model.setAuthorName(authorName);
                    model.setAboutShort(aboutShort);
                    model.setAbout(about);
                    captionMap.put(Constants.CAPTION_KEY, caption);
                    contentMap.put(Constants.CONTENT_KEY, content);
                    model.setCaption(captionMap);
                    model.setContent(contentMap);
                    authorModels.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return authorModels;
        }

        String loadJson(String fileName) {
            String resourceContentToString = null;
            try {
                final int resourceId = getContext().getResources().getIdentifier(fileName, "raw", getContext().getPackageName());
                final InputStream inputStream = getContext().getResources().openRawResource(resourceId);
                final int size = inputStream.available();
                final byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                resourceContentToString = new String(buffer, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resourceContentToString;
        }
    }

    public List<String> getAuthorsFilesNames() {
        return mDb.getAuthorsFileNames();
    }

    public void changeFavoriteStatus(int authorId, String caption, boolean isFavorite, Context ctx) {
        int numberOfUpdatedRows = mDb.changeFavoriteStatus(authorId, caption, isFavorite);
        if (0 < numberOfUpdatedRows) {
            if (isFavorite) {
                Toast.makeText(ctx, String.format(ctx.getString(R.string.added_into_favorites),
                        caption), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctx, String.format(ctx.getString(R.string.removed_from_favorites),
                        caption), Toast.LENGTH_SHORT).show();
            }
        } else {
            System.out.println("ERROR! Favorite status update is failed");
            Toast.makeText(ctx, String.format(ctx.getString(R.string.err_adding_favorites), caption),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getCaptionStatus(int authorId, String caption) {
        return mDb.checkCaptionStatus(authorId, caption);
    }

    public int getCaptionId(int authorId, String caption) {
        return mDb.getCaptionIdByAuthorIdCaption(authorId, caption);
    }

    public Drawable getFavoriteDrawable(boolean isFavorite) {
        Drawable favImage;
        if (isFavorite) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                favImage = getContext().getResources().getDrawable(R.drawable.favorite, getContext().getTheme());
            } else {
                favImage = getContext().getResources().getDrawable(R.drawable.favorite);
            }
            return favImage;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            favImage = getContext().getResources().getDrawable(R.drawable.no_favorite, getContext().getTheme());
        } else {
            favImage = getContext().getResources().getDrawable(R.drawable.no_favorite);
        }
        return favImage;
    }

    public List<Model> getFavoriteList() {
        return mDb.getFavoriteList();
    }
}
