package com.literature.android.literature.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.R;

import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class CaptionRecyclerAdapter extends RecyclerView.Adapter<CaptionViewHolder> {
    private List<String> mCaptions;
    private FragmentManager mFragmentManager;

    public CaptionRecyclerAdapter(List<String> itemsCaptionList, FragmentManager fragmentManager) {
        mCaptions = itemsCaptionList;
        mFragmentManager = fragmentManager;
    }

    @Override
    public CaptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caption_fragment_item, null);
        return new CaptionViewHolder(view, mFragmentManager);
    }

    @Override
    public void onBindViewHolder(CaptionViewHolder holder, int position) {
        String caption = mCaptions.get(position);
        holder.bindDrawable(caption);
    }

    @Override
    public int getItemCount() {
        return mCaptions.size();
    }
}
