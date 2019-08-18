package com.literature.android.literature.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.Constants;
import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.FavDescription;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mher on 4/4/17.
 */

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    public interface RemoveItemCallBack {
        void removeItem(int position);
    }

    private TextView mItemTextView;
    private ImageButton mItemFavImageButton;
    private boolean mIsFavorite = true;
    private String mCaption;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private List<Model> mFavModelList;
    private RemoveItemCallBack removeItemListener;
    private InterstitialAd mInterstitial;


    public FavoriteViewHolder(View itemView, List<Model> FavModels, Context context,
                              FragmentManager fm, InterstitialAd interstitial) {
        super(itemView);
        mContext = context;
        mFragmentManager = fm;
        mFavModelList = FavModels;
        mInterstitial = interstitial;
        mItemTextView = itemView.findViewById(R.id.caption_item_text_view);
        mItemFavImageButton = itemView.findViewById(R.id.caption_item_favorite_button);
        itemView.setOnClickListener(view -> {
            int selectedItemId = getAdapterPosition();
            Model chosenModel = mFavModelList.get(selectedItemId);
            int authorId = chosenModel.getAuthorId();
            int authorIdForList = authorId - 1;
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (null != mFragmentManager.findFragmentById(R.id.favorite_activity_fragment_container)) {
                fragmentTransaction.replace(R.id.favorite_activity_fragment_container
                        , FavDescription.newInstance(FavDescription.class.getSimpleName()
                                , authorIdForList, mCaption, mIsFavorite)
                        , FavDescription.class.getSimpleName());
            } else {
                fragmentTransaction.add(R.id.favorite_activity_fragment_container
                        , FavDescription.newInstance(FavDescription.class.getSimpleName()
                                , authorIdForList, mCaption, mIsFavorite)
                        , FavDescription.class.getSimpleName());
            }
            fragmentTransaction.addToBackStack(null).commit();
        });
    }

    public void bindDrawable(final Model favModel) {
        mCaption = favModel.getCaption().get(Constants.CAPTION_KEY);
        final int[] authorIdForDb = new int[1];
        mItemTextView.setText(mCaption);
        setFavImage();
        mItemFavImageButton.setOnClickListener(view -> {
            mIsFavorite = !mIsFavorite;
            List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
            for (int i = 0; i < allInfo.size(); ++i) {
                if (allInfo.get(i).get(0).getAuthorName().equals(favModel.getAuthorName())) {
                    authorIdForDb[0] = i + 1;
                    break;
                }
            }
            Manager.sharedManager().changeFavoriteStatus(authorIdForDb[0], mCaption, mIsFavorite, mContext);
            removeItemListener.removeItem(getAdapterPosition());
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

    public void setRemoveItemListener(RemoveItemCallBack deleteItemListener) {
        removeItemListener = deleteItemListener;
    }
}
