package com.literature.android.literature;

import java.util.Map;

/**
 * Created by mher on 3/28/17.
 */

public class Model {

    private String mAuthorName;
    private Map<String, String> mArticle;

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public Map<String, String> getArticle() {
        return mArticle;
    }

    public void setArticle(Map<String, String> article) {
        mArticle = article;
    }
}
