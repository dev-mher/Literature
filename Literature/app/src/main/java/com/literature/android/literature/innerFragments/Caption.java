package com.literature.android.literature.innerFragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.activities.CaptionActivity;
import com.literature.android.literature.adapters.CaptionRecyclerAdapter;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class Caption extends Fragment {
    private int mAuthorId;

    public static Caption newInstance(String title, int authorId) {
        Caption captionFragment = new Caption();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        args.putInt(CaptionActivity.CLICKED_ITEM_ID, authorId);
        captionFragment.setArguments(args);
        return captionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mAuthorId = getArguments().getInt(CaptionActivity.CLICKED_ITEM_ID);
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
        View view = inflater.inflate(R.layout.caption_fragment_layout, container, false);
        RecyclerView captionRecyclerView = (RecyclerView) view.findViewById(R.id.caption_recycler_view);
        captionRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        List<Model> authorInfo = allInfo.get(mAuthorId);
        List<String> captionList = new ArrayList<>();
        for (int i = 0; i < authorInfo.size(); ++i) {
            captionList.add(authorInfo.get(i).getCaption().get("caption"));
        }
        captionRecyclerView.setAdapter(new CaptionRecyclerAdapter(captionList, getFragmentManager(), mAuthorId));
        return view;
    }
}
