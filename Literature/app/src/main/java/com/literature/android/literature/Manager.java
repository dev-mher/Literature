package com.literature.android.literature;

import android.content.Context;

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

    public String loadJson(String fileName) {
        String jsonContentToString = null;
        try {
            final int resourceId = getContext().getResources().getIdentifier(fileName, "raw", getContext().getPackageName());
            final InputStream inputStream = getContext().getResources().openRawResource(resourceId);
            final int size = inputStream.available();
            final byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonContentToString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonContentToString;
    }

    public List<Model> jsonParser() {
        List<Model> authorModels = null;
        Map<String, String> captionMap;
        Map<String, String> contentMap;
        String authorNameKey = "author";
        String listKey = "list";
        String captionKey = "caption";
        String contentKey = "content";
        try {
            JSONObject mainObject = new JSONObject(loadJson("hovhannes_tumanyan"));
            authorModels = new ArrayList<>();
            String authorName = mainObject.getString(authorNameKey);
            JSONArray authorList = mainObject.getJSONArray(listKey);
            for (int i = 0; i < authorList.length(); ++i) {
                Model model = new Model();
                captionMap = new HashMap<>();
                contentMap = new HashMap<>();
                JSONObject listObject = authorList.getJSONObject(i);
                String caption = listObject.getString(captionKey);
                String content = listObject.getString(contentKey);
                model.setAuthorName(authorName);
                captionMap.put(captionKey, caption);
                contentMap.put(contentKey, content);
                model.setCaption(captionMap);
                model.setContent(contentMap);
                authorModels.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authorModels;
    }
}
