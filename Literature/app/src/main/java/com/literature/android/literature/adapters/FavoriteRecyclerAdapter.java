package com.literature.android.literature.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.Model;
import com.literature.android.literature.R;

import java.util.List;

/**
 * Created by mher on 4/4/17.
 */

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private List<Model> mFavModelList;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public FavoriteRecyclerAdapter(List<Model> FavModels, Context ctx, FragmentManager fm) {
        mFavModelList = FavModels;
        mContext = ctx;
        mFragmentManager = fm;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caption_fragment_item, null);
        FavoriteViewHolder holder = new FavoriteViewHolder(view, mFavModelList, mContext, mFragmentManager);
        holder.setRemoveItemListener(new FavoriteViewHolder.RemoveItemCallBack() {
            @Override
            public void removeItem(int position) {
                mFavModelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mFavModelList.size());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Model favModel = mFavModelList.get(position);
        holder.bindDrawable(favModel);
    }

    @Override
    public int getItemCount() {
        return mFavModelList.size();
    }
}
