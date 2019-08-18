package com.literature.android.literature.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mher on 4/4/17.
 */

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private List<Model> mFavModelList;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private InterstitialAd mInterstitial;

    public FavoriteRecyclerAdapter(List<Model> FavModels, Context ctx,
                                   FragmentManager fm, InterstitialAd interstitial) {
        mFavModelList = FavModels;
        mContext = ctx;
        mFragmentManager = fm;
        mInterstitial = interstitial;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caption_fragment_item, null);
        FavoriteViewHolder holder = new FavoriteViewHolder(view, mFavModelList, mContext,
                mFragmentManager, mInterstitial);
        holder.setRemoveItemListener(position -> {
            mFavModelList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFavModelList.size());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Model favModel = mFavModelList.get(position);
        holder.bindDrawable(favModel);
    }

    @Override
    public int getItemCount() {
        return mFavModelList.size();
    }
}
