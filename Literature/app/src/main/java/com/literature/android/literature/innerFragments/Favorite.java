package com.literature.android.literature.innerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.FavoriteRecyclerAdapter;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.List;

/**
 * Created by mher on 4/4/17.
 */

public class Favorite extends Fragment {
    private int mAuthorId;
    private TextView toolBarText;

    public static Favorite newInstance(String title) {
        Favorite favoriteFragment = new Favorite();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        favoriteFragment.setArguments(args);
        return favoriteFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.favorite_toolbar);
        toolBarText = (TextView) toolbar.findViewById(R.id.favorite_activity_title);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        toolBarText.setText(getContext().getString(R.string.favorite_title));
        View view = inflater.inflate(R.layout.favorite_fragment_layout, container, false);
        RecyclerView favoriteRecyclerView = (RecyclerView) view.findViewById(R.id.favorite_recycler_view);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<Model> favoriteList = Manager.sharedManager().getFavoriteList();
        favoriteRecyclerView.setAdapter(new FavoriteRecyclerAdapter(favoriteList, getContext(), getFragmentManager()));
        return view;
    }

}
