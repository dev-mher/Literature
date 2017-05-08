package com.literature.android.literature.tabFragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.WriterRecyclerAdapter;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mher on 3/24/17.
 */

public class Story extends Fragment {


    public static Story newInstance(String title) {
        Story storyFragment = new Story();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);

        storyFragment.setArguments(args);
        return storyFragment;
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
        literatureRecyclerView.setLayoutManager((new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)));
        List<Drawable> autorsImages = new ArrayList<>();
        List<String> aboutShort = new ArrayList<>();
        List<String> authorsFileNames = Manager.sharedManager(getContext()).getAuthorsFilesNames();
        for (int i = 0; i < authorsFileNames.size(); ++i) {
            int id = getResources().getIdentifier(authorsFileNames.get(i), "drawable", getContext().getPackageName());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                autorsImages.add(getResources().getDrawable(id, getContext().getTheme()));
            } else {
                autorsImages.add(getResources().getDrawable(id));
            }
        }
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        for (int i = 0; i < allInfo.size(); ++i) {
            List<Model> authorInfo = allInfo.get(i);
            if (null != authorInfo && !authorInfo.isEmpty()) {
                String aboutAuthorShort = authorInfo.get(0).getAboutShort();
                aboutShort.add(aboutAuthorShort);
            }
        }
        literatureRecyclerView.setAdapter(new WriterRecyclerAdapter(autorsImages, aboutShort, getContext()));
        return view;
    }
}
