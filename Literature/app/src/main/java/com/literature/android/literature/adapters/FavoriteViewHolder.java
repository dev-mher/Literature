package com.literature.android.literature.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;

import java.util.List;

/**
 * Created by mher on 4/4/17.
 */

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    private TextView mItemTextView;
    private ImageButton mItemFavImageButton;
    private boolean mIsFavorite = true;
    private boolean mAuthorId;
    private String mCaption;
    private Context mContext;
    private FragmentManager mFragmentManager;


    public FavoriteViewHolder(View itemView, Context context, FragmentManager fm) {
        super(itemView);
        mContext = context;
        mFragmentManager = fm;
        mItemTextView = (TextView) itemView.findViewById(R.id.favorite_item_text_view);
        mItemFavImageButton = (ImageButton) itemView.findViewById(R.id.favorite_item_favorite_button);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void bindDrawable(final Model favModel) {
        mCaption = favModel.getCaption().get("caption");
        final int[] authorId = new int[1];
        mItemTextView.setText(mCaption);
        setFavImage();
        mItemFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFavorite = !mIsFavorite;
                List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
                for (int i = 0; i < allInfo.size(); ++i) {
                    if (allInfo.get(i).get(0).getAuthorName().equals(favModel.getAuthorName())) {
                        authorId[0] = i;
                        break;
                    }
                }
                Manager.sharedManager().changeFavoriteStatus(authorId[0], mCaption, mIsFavorite, mContext);
                setFavImage();
            }
        });
    }

    private void setFavImage() {
        Drawable favImage = Manager.sharedManager().getFavoriteDrawable(mIsFavorite);
        mItemFavImageButton.setBackground(favImage);
    }
}
