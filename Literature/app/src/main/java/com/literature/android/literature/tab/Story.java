package com.literature.android.literature.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.R;
import com.literature.android.literature.adapter.LiteratureRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mher on 3/24/17.
 */

public class Story extends Fragment {


    public static Story newInstance(int page, String title) {
        Story fragmentFirst = new Story();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
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
        View view = inflater.inflate(R.layout.story_tab_layout, container, false);
        RecyclerView literatureRecyclerView = (RecyclerView) view.findViewById(R.id.story_recycler_view);
        literatureRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<String> authors = new ArrayList<>();
        authors.add("Grigoryan Mher");
        authors.add("Grigoryan David");
        literatureRecyclerView.setAdapter(new LiteratureRecyclerAdapter(authors));
        return view;
    }
}
