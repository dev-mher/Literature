package com.literature.android.literature.innerFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.literature.android.literature.Constants;
import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.CaptionRecyclerAdapter;
import com.literature.android.literature.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mher on 3/26/17.
 */

public class Caption extends Fragment implements SearchView.OnQueryTextListener {
    private int mAuthorId;
    private TextView toolBarText;
    private List<String> captionList;
    private CaptionRecyclerAdapter adapter;
    private FloatingActionButton fab;
    private InterstitialAd interstitial;

    public static Caption newInstance(String title, int authorId) {
        Caption captionFragment = new Caption();
        Bundle args = new Bundle();
        args.putString(Constants.TAB_FRAGMENT_PAGE_TITLE, title);
        args.putInt(Constants.CLICKED_ITEM_ID, authorId);
        captionFragment.setArguments(args);
        return captionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (null != getArguments()) {
            mAuthorId = getArguments().getInt(Constants.CLICKED_ITEM_ID);
        }
        if (getActivity() == null) {
            return;
        }
        Toolbar toolbar = getActivity().findViewById(R.id.caption_activity_toolbar);
        toolBarText = toolbar.findViewById(R.id.caption_activity_title);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            fab = getActivity().findViewById(R.id.fab);
            fab.show();
        }
        loadAdBanners();
        View view = inflater.inflate(R.layout.caption_fragment_layout, container, false);
        RecyclerView captionRecyclerView = view.findViewById(R.id.caption_recycler_view);
        captionRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        List<Model> authorInfo = allInfo.get(mAuthorId);
        String authorName = authorInfo.get(0).getAuthorName();
        toolBarText.setText(authorName);
        captionList = new ArrayList<>();
        for (int i = 0; i < authorInfo.size(); ++i) {
            captionList.add(authorInfo.get(i).getCaption().get(Constants.CAPTION_KEY));
        }
        adapter = new CaptionRecyclerAdapter(captionList, getFragmentManager(), mAuthorId, getContext(), interstitial);
        captionRecyclerView.setAdapter(adapter);
        return view;
    }

    private void loadAdBanners() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        interstitial = Utils.loadInterstitialAd(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.caption_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu_item);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fab.hide();
            } else {
                fab.show();
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
        if (TextUtils.isEmpty(newText)) {
            adapter.setFilterDefaultValue();
            return true;
        }
        List<String> searchCaptions = new ArrayList<>();
        for (String caption : captionList) {
            if (caption.toLowerCase().contains(newText.toLowerCase())) {
                searchCaptions.add(caption);
            }
        }
        if (0 < searchCaptions.size()) {
            adapter.setFilter(searchCaptions);
        } else {
            adapter.setFilterEmptyValue();
        }
        return true;
    }
}
