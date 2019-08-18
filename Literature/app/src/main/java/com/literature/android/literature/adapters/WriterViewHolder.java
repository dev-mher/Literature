package com.literature.android.literature.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.literature.android.literature.Constants;
import com.literature.android.literature.R;
import com.literature.android.literature.activities.CaptionActivity;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mher on 3/24/17.
 */

public class WriterViewHolder extends RecyclerView.ViewHolder {
    private ImageView mItemImageView;
    private TextView mItemTextView;
    private Context mContext;

    public WriterViewHolder(View itemView, final Context context) {
        super(itemView);
        mItemImageView = itemView.findViewById(R.id.author_item_image_view);
        mItemTextView = itemView.findViewById(R.id.author_item_text_view);
        mContext = context;
        itemView.setOnClickListener(view -> {
            int authorId = getAdapterPosition();
            Intent intent = new Intent(mContext, CaptionActivity.class);
            intent.putExtra(Constants.CLICKED_ITEM_ID, authorId);
            mContext.startActivity(intent);
        });
    }

    public void bindDrawable(Drawable authorImage, String aboutShort) {
        mItemImageView.setImageDrawable(authorImage);
        mItemTextView.setText(aboutShort);
    }
}