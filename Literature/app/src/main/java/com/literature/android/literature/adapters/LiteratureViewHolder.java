package com.literature.android.literature.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.literature.android.literature.R;
import com.literature.android.literature.activities.CaptionActivity;

/**
 * Created by mher on 3/24/17.
 */

public class LiteratureViewHolder extends RecyclerView.ViewHolder {
    private ImageView mItemImageView;
    private Context mContext;

    public LiteratureViewHolder(View itemView, final Context context) {
        super(itemView);
        mItemImageView = (ImageView) itemView.findViewById(R.id.author_item_image_view);
        mContext = context;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int authorId = getAdapterPosition();
                Intent intent = new Intent(mContext, CaptionActivity.class);
                intent.putExtra(CaptionActivity.CLICKED_ITEM_ID, authorId);
                mContext.startActivity(intent);
            }
        });
    }

    public void bindDrawable(Drawable authorImage) {
        mItemImageView.setImageDrawable(authorImage);
    }
}