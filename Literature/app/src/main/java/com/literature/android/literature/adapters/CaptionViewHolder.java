package com.literature.android.literature.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Description;

import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class CaptionViewHolder extends RecyclerView.ViewHolder {

    private TextView mItemTextView;
    private ImageButton mItemFavImageButton;
    private FragmentManager mFragmentManager;
    private int mAuthorId;
    private Context mContext;
    private boolean mIsFavorite;
    private String mCaption;


    public CaptionViewHolder(View itemView, FragmentManager fragmentManager, final int authorId, Context context) {
        super(itemView);
        mContext = context;
        mItemTextView = (TextView) itemView.findViewById(R.id.caption_item_text_view);
        mItemFavImageButton = (ImageButton) itemView.findViewById(R.id.caption_item_favorite_button);
        mFragmentManager = fragmentManager;
        mAuthorId = authorId;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int captionId = getAdapterPosition();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if (null != mFragmentManager.findFragmentById(R.id.caption_activity_fragment_container)) {
                    fragmentTransaction.replace(R.id.caption_activity_fragment_container
                            , Description.newInstance(Description.class.getSimpleName(), mAuthorId, captionId, mIsFavorite)
                            , Description.class.getSimpleName());
                } else {
                    fragmentTransaction.add(R.id.caption_activity_fragment_container
                            , Description.newInstance(Description.class.getSimpleName(), mAuthorId, captionId, mIsFavorite)
                            , Description.class.getSimpleName());
                }
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
    }

    public void bindDrawable(String captionText, final int captionId, boolean isFavorite) {
        mIsFavorite = isFavorite;
        mItemTextView.setText(captionText);
        setFavImage();
        mItemFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<List<Model>> authorModels = Manager.sharedManager().getAllAuthorsInfo();
                List<Model> authorList = authorModels.get(mAuthorId);
                Model authorModel = authorList.get(captionId);
                mCaption = authorModel.getCaption().get("caption");
                mIsFavorite = !mIsFavorite;
                Manager.sharedManager().changeFavoriteStatus(mAuthorId + 1, mCaption, mIsFavorite, mContext);
                setFavImage();
            }
        });
    }

    private void setFavImage() {
        Drawable favImage = Manager.sharedManager().getFavoriteDrawable(mIsFavorite);
        mItemFavImageButton.setBackground(favImage);
    }
}
