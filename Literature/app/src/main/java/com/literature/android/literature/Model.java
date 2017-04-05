package com.literature.android.literature;

import java.util.Map;

/**
 * Created by mher on 3/28/17.
 */

public class Model {

    private String mAuthorName;
    private String mAuthorFileName;
    private int mAuthorId;
    private Map<String, String> mCaption;
    private Map<String, String> mContent;

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getAuthorFileName() {
        return mAuthorFileName;
    }

    public void setAuthorFileName(String authorFileName) {
        mAuthorFileName = authorFileName;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int authorId) {
        mAuthorId = authorId;
    }

    public Map<String, String> getCaption() {
        return mCaption;
    }

    public void setCaption(Map<String, String> caption) {
        mCaption = caption;
    }

    public Map<String, String> getContent() {
        return mContent;
    }

    public void setContent(Map<String, String> content) {
        this.mContent = content;
    }
}
