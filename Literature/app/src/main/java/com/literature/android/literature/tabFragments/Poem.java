package com.literature.android.literature.tabFragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.R;
import com.literature.android.literature.adapters.LiteratureRecyclerAdapter;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mher on 3/24/17.
 */

public class Poem extends Fragment {

    // newInstance constructor for creating fragment with arguments
    public static Poem newInstance(String title) {
        Poem poemFragment = new Poem();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        poemFragment.setArguments(args);
        return poemFragment;
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
        View view = inflater.inflate(R.layout.poem_tab_layout, container, false);
        RecyclerView literatureRecyclerView = (RecyclerView) view.findViewById(R.id.poem_recycler_view);
        literatureRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<Drawable> autorsImages = new ArrayList<>();
        Drawable hovhannesTumanyanImage;
        Drawable paruyrSevakImage;
        Drawable hovhannesShirazImage;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            hovhannesTumanyanImage = getResources().getDrawable(R.drawable.hovhannes_tumanian, getContext().getTheme());
            paruyrSevakImage = getResources().getDrawable(R.drawable.paruyr_sevak, getContext().getTheme());
            hovhannesShirazImage = getResources().getDrawable(R.drawable.hovhannes_shiraz, getContext().getTheme());
        } else {
            hovhannesTumanyanImage = getResources().getDrawable(R.drawable.hovhannes_tumanian);
            paruyrSevakImage = getResources().getDrawable(R.drawable.paruyr_sevak);
            hovhannesShirazImage = getResources().getDrawable(R.drawable.hovhannes_shiraz);
        }
        autorsImages.add(hovhannesTumanyanImage);
        autorsImages.add(paruyrSevakImage);
        autorsImages.add(hovhannesShirazImage);
        literatureRecyclerView.setAdapter(new LiteratureRecyclerAdapter(autorsImages, getContext()));
        return view;
    }
}
