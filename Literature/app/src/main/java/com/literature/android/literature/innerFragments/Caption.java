package com.literature.android.literature.innerFragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class Caption extends Fragment implements SearchView.OnQueryTextListener {
    private int mAuthorId;
    private TextView toolBarText;
    private List<String> captionList;
    private CaptionRecyclerAdapter adapter;
    private FloatingActionButton fab;

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
        setHasOptionsMenu(true);
        if (null != getArguments()) {
            mAuthorId = getArguments().getInt(CaptionActivity.CLICKED_ITEM_ID);
        }
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.caption_activity_toolbar);
        toolBarText = (TextView) toolbar.findViewById(R.id.caption_activity_title);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
        View view = inflater.inflate(R.layout.caption_fragment_layout, container, false);
        RecyclerView captionRecyclerView = (RecyclerView) view.findViewById(R.id.caption_recycler_view);
        captionRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        List<Model> authorInfo = allInfo.get(mAuthorId);
        String authorName = authorInfo.get(0).getAuthorName();
        toolBarText.setText(authorName);
        captionList = new ArrayList<>();
        for (int i = 0; i < authorInfo.size(); ++i) {
            captionList.add(authorInfo.get(i).getCaption().get("caption"));
        }
        adapter = new CaptionRecyclerAdapter(captionList, getFragmentManager(), mAuthorId, getContext());
        captionRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.caption_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu_item);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem favItem = menu.findItem(R.id.favorite_menu_item);
        favItem.setVisible(false);
        MenuItem shareItem = menu.findItem(R.id.share_menu_item);
        shareItem.setVisible(false);
        MenuItem shareFbItem = menu.findItem(R.id.share_facebook_menu_item);
        shareFbItem.setVisible(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<String> searchCaptions = new ArrayList<>();
        for (String caption : captionList) {
            if (0 < newText.length()) {
                if (caption.toLowerCase().contains(newText.toLowerCase())) {
                    searchCaptions.add(caption);
                }
            }
        }
        if (0 < searchCaptions.size()) {
            adapter.setFilter(searchCaptions);
        } else {
            adapter.setFilterDefaultValue();
        }
        return true;
    }
}
