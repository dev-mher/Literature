package com.literature.android.literature.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.Manager;
import com.literature.android.literature.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class CaptionRecyclerAdapter extends RecyclerView.Adapter<CaptionViewHolder> {
    private List<String> mCaptions;
    private List<String> mCaptionsCopy;
    private FragmentManager mFragmentManager;
    private int mAuthorId;
    private Context mContext;
    private InterstitialAd mInterstitial;

    public CaptionRecyclerAdapter(List<String> itemsCaptionList, FragmentManager fragmentManager,
                                  int authorId, Context context, InterstitialAd interstitial) {
        mCaptions = itemsCaptionList;
        mCaptionsCopy = itemsCaptionList;
        mFragmentManager = fragmentManager;
        mAuthorId = authorId;
        mContext = context;
        mInterstitial = interstitial;
    }

    @Override
    public CaptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caption_fragment_item, null);
        return new CaptionViewHolder(view, mFragmentManager, mAuthorId, mContext, mInterstitial);
    }

    @Override
    public void onBindViewHolder(CaptionViewHolder holder, int position) {
        String caption = mCaptions.get(position);
        int authorIdForDb = mAuthorId + 1;
        boolean isFavorite = Manager.sharedManager().getCaptionStatus(authorIdForDb, caption);
        int captionId = Manager.sharedManager().getCaptionId(authorIdForDb, caption);
        if (-1 == captionId) {
            return;
        }
        holder.bindDrawable(caption, captionId, isFavorite);
    }

    @Override
    public int getItemCount() {
        return mCaptions.size();
    }

    public void setFilter(List<String> searchCaptions) {
        mCaptions = new ArrayList<>();
        mCaptions.addAll(searchCaptions);
        notifyDataSetChanged();
    }

    public void setFilterDefaultValue() {
        mCaptions = mCaptionsCopy;
        notifyDataSetChanged();
    }

    public void setFilterEmptyValue() {
        mCaptions = Collections.emptyList();
        notifyDataSetChanged();
    }
}
