package com.literature.android.literature.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.R;

import java.util.List;

/**
 * Created by mher on 3/24/17.
 */

public class WriterRecyclerAdapter extends RecyclerView.Adapter<WriterViewHolder> {
    private List<Drawable> mAuthorsImages;
    private Context mContext;

    public WriterRecyclerAdapter(List<Drawable> itemsImageList, Context context) {
        mAuthorsImages = itemsImageList;
        mContext = context;
    }

    @Override
    public WriterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.writer_tab_item, null);
        return new WriterViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(WriterViewHolder holder, int position) {
        Drawable authorImage = mAuthorsImages.get(position);
        holder.bindDrawable(authorImage);
    }

    @Override
    public int getItemCount() {
        return mAuthorsImages.size();
    }
}
