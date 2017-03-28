package com.literature.android.literature.innerFragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.R;
import com.literature.android.literature.adapters.CaptionRecyclerAdapter;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class Caption extends Fragment {

    public static Caption newInstance(String title) {
        Caption captionFragment = new Caption();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        captionFragment.setArguments(args);
        return captionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        List<String> captionList = new ArrayList<>();
        captionList.add("------ First Caption ------");
        captionList.add("------ Second Caption ------");
        captionList.add("------ Threeth Caption ------");
        captionList.add("------ Fourth Caption ------");
        captionRecyclerView.setAdapter(new CaptionRecyclerAdapter(captionList, getFragmentManager()));
        return view;
    }
}
