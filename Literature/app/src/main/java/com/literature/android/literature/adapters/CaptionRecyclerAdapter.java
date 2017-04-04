package com.literature.android.literature.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.Manager;
import com.literature.android.literature.R;

import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class CaptionRecyclerAdapter extends RecyclerView.Adapter<CaptionViewHolder> {
    private List<String> mCaptions;
    private FragmentManager mFragmentManager;
    private int mAuthorId;
    private Context mContext;

    public CaptionRecyclerAdapter(List<String> itemsCaptionList, FragmentManager fragmentManager, int authorId, Context context) {
        mCaptions = itemsCaptionList;
        mFragmentManager = fragmentManager;
        mAuthorId = authorId;
        mContext = context;
    }

    @Override
    public CaptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caption_fragment_item, null);
        return new CaptionViewHolder(view, mFragmentManager, mAuthorId, mContext);
    }

    @Override
    public void onBindViewHolder(CaptionViewHolder holder, int position) {
        String caption = mCaptions.get(position);
        int authorIdForDb = mAuthorId + 1;
        boolean isFavorite = Manager.sharedManager().getCaptionStatus(authorIdForDb, caption);
        holder.bindDrawable(caption, position, isFavorite);
    }

    @Override
    public int getItemCount() {
        return mCaptions.size();
    }
}
