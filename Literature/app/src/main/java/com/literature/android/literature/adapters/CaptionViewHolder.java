package com.literature.android.literature.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.Manager;
import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Description;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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
    private int mCaptionId;
    private String mCaption;
    private InterstitialAd mInterstitial;


    public CaptionViewHolder(View itemView, FragmentManager fragmentManager,
                             final int authorId, Context context, InterstitialAd interstitial) {
        super(itemView);
        mContext = context;
        mItemTextView = itemView.findViewById(R.id.caption_item_text_view);
        mItemFavImageButton = itemView.findViewById(R.id.caption_item_favorite_button);
        mFragmentManager = fragmentManager;
        mAuthorId = authorId;
        mInterstitial = interstitial;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if (null != mFragmentManager.findFragmentById(R.id.caption_activity_fragment_container)) {
                    fragmentTransaction.replace(R.id.caption_activity_fragment_container
                            , Description.newInstance(Description.class.getSimpleName(),
                                    mAuthorId, mCaptionId, mCaption, mIsFavorite)
                            , Description.class.getSimpleName());
                } else {
                    fragmentTransaction.add(R.id.caption_activity_fragment_container
                            , Description.newInstance(Description.class.getSimpleName(),
                                    mAuthorId, mCaptionId, mCaption, mIsFavorite)
                            , Description.class.getSimpleName());
                }
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
    }

    public void bindDrawable(final String captionText, int captionId, boolean isFavorite) {
        mIsFavorite = isFavorite;
        mItemTextView.setText(captionText);
        setFavImage();
        mCaptionId = captionId;
        mCaption = captionText;
        mItemFavImageButton.setOnClickListener(v -> {
            mIsFavorite = !mIsFavorite;
            Manager.sharedManager().changeFavoriteStatus(mAuthorId + 1, captionText, mIsFavorite, mContext);
            setFavImage();
            showInterstitial();
        });
    }

    private void showInterstitial() {
        if (mInterstitial != null && mInterstitial.isLoaded()) {
            mInterstitial.show();
        }
    }

    private void setFavImage() {
        Drawable favImage = Manager.sharedManager().getFavoriteDrawable(mIsFavorite);
        mItemFavImageButton.setBackground(favImage);

    }
}
