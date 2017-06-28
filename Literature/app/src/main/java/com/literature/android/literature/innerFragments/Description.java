package com.literature.android.literature.innerFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.Constants;
import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.activities.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mher on 3/26/17.
 */

public class Description extends Fragment {
    private int mAuthorId;
    private int mCaptionId;
    private boolean isFavorite;
    private String mAuthorName;
    private String mCaption;
    private String mContent;
    private TextView toolBarText;
    TextView descriptionText;
    TextView titleTextView;
    Toolbar toolbar;
    AdView mAdView;
    InterstitialAd interstitial;

    public static Description newInstance(String title, int authorId, int captionId, String caption,
                                          boolean isFavorite) {
        Description descriptionFragment = new Description();
        Bundle args = new Bundle();
        args.putString(Constants.TAB_FRAGMENT_PAGE_TITLE, title);
        args.putInt(Constants.AUTHOR_ID, authorId);
        args.putString(Constants.CAPTION_KEY, caption);
        args.putInt(Constants.CLICKED_ITEM_ID, captionId);
        args.putBoolean(Constants.IS_FAVORITE, isFavorite);
        descriptionFragment.setArguments(args);
        return descriptionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuthorId = getArguments().getInt(Constants.AUTHOR_ID);
        mCaptionId = getArguments().getInt(Constants.CLICKED_ITEM_ID);
        mCaption = getArguments().getString(Constants.CAPTION_KEY);
        isFavorite = getArguments().getBoolean(Constants.IS_FAVORITE);
        toolbar = (Toolbar) getActivity().findViewById(R.id.caption_activity_toolbar);
        toolbar.collapseActionView();
        toolBarText = (TextView) toolbar.findViewById(R.id.caption_activity_title);
        toolBarText.setSelected(true);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();
        View view = inflater.inflate(R.layout.description_fragment_layout, container, false);
        mAdView = (AdView) view.findViewById(R.id.description_adView);
        loadAdBanners();
        descriptionText = (TextView) view.findViewById(R.id.description_item_text_view);
        titleTextView = (TextView) view.findViewById(R.id.description_title_text_view);
        List<List<Model>> authorModels = Manager.sharedManager().getAllAuthorsInfo();
        if (null != authorModels) {
            Model authorModel = authorModels.get(mAuthorId).get(mCaptionId);
            mAuthorName = authorModel.getAuthorName();
            titleTextView.setText(mCaption);
            mContent = authorModel.getContent().get(Constants.CONTENT_KEY);
            descriptionText.setText(mContent);
        }
        if (null != mAuthorName) {
            toolBarText.setText(mAuthorName);
        } else {
            System.out.println("ERROR! an error occurred while getting the author name");
        }
        return view;
    }

    private void loadAdBanners() {
        AdRequest.Builder adRequest = new AdRequest.Builder();
        mAdView.loadAd(adRequest.build());
        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId(getContext().getString(R.string.banner_ad_interstitial_unit_id));
        AdRequest.Builder adRequestInterstitial = new AdRequest.Builder();
        interstitial.loadAd(adRequestInterstitial.build());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem favItem = menu.findItem(R.id.favorite_menu_item);
        favItem.setIcon(Manager.sharedManager().getFavoriteDrawable(isFavorite));
        MenuItem searchItem = menu.findItem(R.id.search_menu_item);
        searchItem.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.caption_menu, menu);
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
                showInterstitial();
                return true;
            case R.id.share_menu_item:
                share();
                showInterstitial();
                return true;
            case R.id.share_facebook_menu_item:
                shareFacebook();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void share() {
        // get available share intents
        List<Intent> targets = new ArrayList<>();
        Intent template = new Intent(Intent.ACTION_SEND);
        template.setType("text/plain");
        List<ResolveInfo> candidates = getContext().getPackageManager().
                queryIntentActivities(template, 0);

        // remove facebook which has a broken share intent
        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.facebook.katana")) {
                Intent target = new Intent(android.content.Intent.ACTION_SEND);
                target.setType("text/plain");
                String postMsg = String.format(getString(R.string.post_message), mAuthorName, mCaption, mContent);
                target.putExtra(Intent.EXTRA_TEXT, postMsg);
                target.setPackage(packageName);
                targets.add(target);
            }
        }
        Intent chooser = Intent.createChooser(targets.remove(0), getString(R.string.share_menu_title));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[]{}));
        startActivity(chooser);
    }

    private void shareFacebook() {
        boolean isconnected = getContext().getSharedPreferences(Constants.FACEBOOK_USER_CONNECTION_STATUS_SHARED_NAME,
                MODE_PRIVATE).getBoolean(Constants.FACEBOOK_USER_ISCONNECTED, false);
        if (!isconnected) {
            Toast.makeText(getContext(), getString(R.string.connect_facebook), Toast.LENGTH_LONG).show();
            Intent goToLoginPage = new Intent(getContext(), LoginActivity.class);
            startActivity(goToLoginPage);
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View alertDialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_common, null);
        TextView title = (TextView) alertDialogLayout.findViewById(R.id.msg_alert_title);
        title.setText(R.string.post_message_dialog_title);
        TextView msg = (TextView) alertDialogLayout.findViewById(R.id.msg_alert_content);
        msg.setText(String.format(getString(R.string.post_message_dialog_msg), mCaption));
        dialog.setView(alertDialogLayout);
        dialog.setPositiveButton(getString(R.string.post_message_dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle postContent = new Bundle();
                String postMsg = String.format(getString(R.string.post_message), mAuthorName, mCaption, mContent);
                postContent.putString(Constants.FACEBOOK_MESSAGE, postMsg);
                GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "me/feed", postContent, HttpMethod.POST, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (null == response.getError()) {
                            Toast.makeText(getContext(), String.format(getString(R.string.post_message_success), mCaption),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), String.format(getString(R.string.post_message_error), mCaption),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                request.executeAsync();
                showInterstitial();
            }
        }).setNegativeButton(getString(R.string.post_message_dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setCancelable(false);
        AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public void onResume() {
        mAdView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
