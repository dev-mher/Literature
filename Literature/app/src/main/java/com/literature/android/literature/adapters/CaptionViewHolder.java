package com.literature.android.literature.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.literature.android.literature.R;
import com.literature.android.literature.innerFragments.Description;

/**
 * Created by mher on 3/26/17.
 */

public class CaptionViewHolder extends RecyclerView.ViewHolder {

    private TextView mItemTextView;
    private FragmentManager mFragmentManager;
    private int mAuthorId;


    public CaptionViewHolder(View itemView, FragmentManager fragmentManager, int authorId) {
        super(itemView);
        mItemTextView = (TextView) itemView.findViewById(R.id.caption_item_text_view);
        mFragmentManager = fragmentManager;
        mAuthorId = authorId;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int captionId = getAdapterPosition();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if (null != mFragmentManager.findFragmentById(R.id.caption_activity_fragment_container)) {
                    fragmentTransaction.replace(R.id.caption_activity_fragment_container
                            , Description.newInstance(Description.class.getSimpleName(), mAuthorId, captionId)
                            , Description.class.getSimpleName());
                } else {
                    fragmentTransaction.add(R.id.caption_activity_fragment_container
                            , Description.newInstance(Description.class.getSimpleName(), mAuthorId, captionId)
                            , Description.class.getSimpleName());
                }
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
    }

    public void bindDrawable(String captionText) {
        mItemTextView.setText(captionText);
    }
}
