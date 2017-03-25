package com.literature.android.literature.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.literature.android.literature.ListActivity;
import com.literature.android.literature.R;

/**
 * Created by mher on 3/24/17.
 */

public class LiteratureViewHolder extends RecyclerView.ViewHolder {
    private ImageView mItemImageView;
    private Context mContext;

    public LiteratureViewHolder(View itemView, final Context context) {
        super(itemView);
        mItemImageView = (ImageView) itemView.findViewById(R.id.item_author_image_view);
        mContext = context;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ListActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    public void bindDrawable(Drawable authorImage) {
        mItemImageView.setImageDrawable(authorImage);
    }
}