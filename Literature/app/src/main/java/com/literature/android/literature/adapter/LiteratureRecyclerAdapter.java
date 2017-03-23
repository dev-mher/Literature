package com.literature.android.literature.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.R;

import java.util.List;

/**
 * Created by mher on 3/24/17.
 */

public class LiteratureRecyclerAdapter extends RecyclerView.Adapter<LiteratureViewHolder> {
    List<String> mAuthorsList;

    public LiteratureRecyclerAdapter(List<String> itemsList) {
        mAuthorsList = itemsList;
    }

    @Override
    public LiteratureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.literature_item, null);
        return new LiteratureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiteratureViewHolder holder, int position) {
        String authorName = mAuthorsList.get(position);
        holder.bindDrawable(authorName);
    }

    @Override
    public int getItemCount() {
        return mAuthorsList.size();
    }
}
