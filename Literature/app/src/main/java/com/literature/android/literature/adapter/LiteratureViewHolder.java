package com.literature.android.literature.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.literature.android.literature.R;

/**
 * Created by mher on 3/24/17.
 */

public class LiteratureViewHolder extends  RecyclerView.ViewHolder {
    private TextView mItemTextView;

    public LiteratureViewHolder(View itemView) {
        super(itemView);
        mItemTextView = (TextView) itemView.findViewById(R.id.item_author_text_view);
    }

    public void bindDrawable(String text) {
        mItemTextView.setText(text);
    }
}