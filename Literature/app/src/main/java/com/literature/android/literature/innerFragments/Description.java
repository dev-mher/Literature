package com.literature.android.literature.innerFragments;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.activities.CaptionActivity;
import com.literature.android.literature.activities.HomeActivity;
import com.literature.android.literature.adapters.PagerAdapter;

import java.util.List;

/**
 * Created by mher on 3/26/17.
 */

public class Description extends Fragment {
    public static final String AUTHOR_ID = "authorId";
    private int mAuthorId;
    private int mCaptionId;
    private boolean isFavorite;
    private String mCaption;
    private String mContent;
    private TextView toolBarText;
    ShareDialog mShareDialog;
    TextView descriptionText;


    private static final String IS_FAVORITE = "isFavorite";

    public static Description newInstance(String title, int authorId, int captionId, boolean isFavorite) {
        Description descriptionFragment = new Description();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE, title);
        args.putInt(AUTHOR_ID, authorId);
        args.putInt(CaptionActivity.CLICKED_ITEM_ID, captionId);
        args.putBoolean(IS_FAVORITE, isFavorite);
        descriptionFragment.setArguments(args);
        return descriptionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();
        mAuthorId = getArguments().getInt(AUTHOR_ID);
        mCaptionId = getArguments().getInt(CaptionActivity.CLICKED_ITEM_ID);
        isFavorite = getArguments().getBoolean(IS_FAVORITE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.caption_activity_toolbar);
        toolBarText = (TextView) toolbar.findViewById(R.id.caption_activity_title);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_fragment_layout, container, false);
        descriptionText = (TextView) view.findViewById(R.id.description_item_text_view);
        descriptionText.setMovementMethod(new ScrollingMovementMethod());
        List<List<Model>> authorModels = Manager.sharedManager().getAllAuthorsInfo();
        if (null != authorModels) {
            Model authorModel = authorModels.get(mAuthorId).get(mCaptionId);
            mContent = authorModel.getContent().get("content");
            descriptionText.setText(mContent);
            mCaption = authorModel.getCaption().get("caption");
        }
        toolBarText.setText(mCaption);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem favItem = menu.findItem(R.id.favorite_menu_item);
        favItem.setIcon(Manager.sharedManager().getFavoriteDrawable(isFavorite));
        MenuItem shareItem = menu.findItem(R.id.facebook_share_menu_item);
        if (!HomeActivity.isConnectedUserToFacebook) {
            shareItem.setVisible(false);
        }
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
            case R.id.facebook_share_menu_item:
                shareContent();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareContent() {
        mShareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(mCaption)
                    .setImageUrl(Uri.parse("https://web.facebook.com/photo.php?fbid=1330581636973230&l=fb4358ce02"))
                    .setContentDescription(mContent)
                    .setContentUrl(Uri.parse("https://web.facebook.com/mhergrigoryan92"))
                    .build();
            mShareDialog.show(linkContent);
        }
    }
}
