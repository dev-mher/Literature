package com.literature.android.literature.innerFragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.share.widget.ShareDialog;
import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.List;

/**
 * Created by mher on 4/5/17.
 */

public class FavDescription extends Fragment {
    public static final String AUTHOR_ID = "authorId";
    public static final String CAPTION = "caption";
    private int mAuthorId;
    private boolean isFavorite;
    private String mCaption;
    private TextView toolBarText;
    private String mContent;
    ShareDialog mShareDialog;


    private static final String IS_FAVORITE = "isFavorite";

    public static FavDescription newInstance(String title, int authorId, String caption, boolean isFavorite) {
        FavDescription favDescFragment = new FavDescription();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        args.putInt(AUTHOR_ID, authorId);
        args.putString(CAPTION, caption);
        args.putBoolean(IS_FAVORITE, isFavorite);
        favDescFragment.setArguments(args);
        return favDescFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuthorId = getArguments().getInt(AUTHOR_ID);
        mCaption = getArguments().getString(CAPTION);
        isFavorite = getArguments().getBoolean(IS_FAVORITE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.favorite_toolbar);
        toolBarText = (TextView) toolbar.findViewById(R.id.favorite_activity_title);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_fragment_layout, container, false);
        TextView descriptionText = (TextView) view.findViewById(R.id.description_item_text_view);
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        List<Model> authorModels = allInfo.get(mAuthorId);
        for (int i = 0; i < authorModels.size(); ++i) {
            if (authorModels.get(i).getCaption().get("caption").equals(mCaption)) {
                mContent = authorModels.get(i).getContent().get("content");
                descriptionText.setText(mContent);
            }
        }
        toolBarText.setText(mCaption);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem favItem = menu.findItem(R.id.favorite_menu_item);
        favItem.setIcon(Manager.sharedManager().getFavoriteDrawable(isFavorite));
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.caption_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favorite_menu_item:
                isFavorite = !isFavorite;
                Drawable favImage = Manager.sharedManager().getFavoriteDrawable(isFavorite);
                item.setIcon(favImage);
                int authorIdForDb = mAuthorId + 1;
                Manager.sharedManager().changeFavoriteStatus(authorIdForDb, mCaption, isFavorite, getActivity());
                return true;
            case R.id.share_menu_item:
                share();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mCaption);
        String shareMessage = mContent;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Choose the messenger to share this App"));
    }
}
