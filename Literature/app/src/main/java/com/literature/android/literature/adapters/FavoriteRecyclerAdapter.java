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
    private List<Model> mModelList;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public FavoriteRecyclerAdapter(List<Model> models, Context ctx, FragmentManager fm) {
        mModelList = models;
        mContext = ctx;
        mFragmentManager = fm;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_fragment_layout, null);
        return new FavoriteViewHolder(view, mContext, mFragmentManager);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Model favModel = mModelList.get(position);
        holder.bindDrawable(favModel);
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }
}
