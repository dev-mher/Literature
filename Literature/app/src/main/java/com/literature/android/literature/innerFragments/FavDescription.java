package com.literature.android.literature.innerFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.Constants;
import com.literature.android.literature.Manager;
import com.literature.android.literature.Model;
import com.literature.android.literature.R;
import com.literature.android.literature.activities.LoginActivity;
import com.literature.android.literature.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mher on 4/5/17.
 */

public class FavDescription extends Fragment {

    private int mAuthorId;
    private boolean isFavorite;
    private String mCaption;
    private String mAuthorName;
    private TextView toolBarText;
    private String mContent;
    private AdView mAdView;
    private InterstitialAd interstitial;

    public static FavDescription newInstance(String title, int authorId, String caption, boolean isFavorite) {
        FavDescription favDescFragment = new FavDescription();
        Bundle args = new Bundle();
        args.putString(Constants.TAB_FRAGMENT_PAGE_TITLE, title);
        args.putInt(Constants.AUTHOR_ID, authorId);
        args.putString(Constants.CAPTION_KEY, caption);
        args.putBoolean(Constants.IS_FAVORITE, isFavorite);
        favDescFragment.setArguments(args);
        return favDescFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() == null) {
            return;
        }
        mAuthorId = getArguments().getInt(Constants.AUTHOR_ID);
        mCaption = getArguments().getString(Constants.CAPTION_KEY);
        isFavorite = getArguments().getBoolean(Constants.IS_FAVORITE);
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.favorite_toolbar);
            toolBarText = toolbar.findViewById(R.id.favorite_activity_title);
            toolBarText.setSelected(true);
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_fragment_layout, container, false);
        mAdView = view.findViewById(R.id.description_adView);
        loadAdBanners();
        TextView descriptionText = view.findViewById(R.id.description_item_text_view);
        TextView titleText = view.findViewById(R.id.description_title_text_view);
        List<List<Model>> allInfo = Manager.sharedManager().getAllAuthorsInfo();
        List<Model> authorModels = allInfo.get(mAuthorId);
        for (int i = 0; i < authorModels.size(); ++i) {
            if (authorModels.get(i).getCaption().get(Constants.CAPTION_KEY).equals(mCaption)) {
                mAuthorName = authorModels.get(i).getAuthorName();
                titleText.setText(mCaption);
                mContent = authorModels.get(i).getContent().get(Constants.CONTENT_KEY);
                descriptionText.setText(mContent);
            }
        }
        if (null != mAuthorName) {
            toolBarText.setText(mAuthorName);
        } else {
            System.out.println("ERROR! an error occurred while getting the author name");
        }
        return view;
    }

    private void loadAdBanners() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        Utils.loadAdView(activity, mAdView);
        interstitial = Utils.loadInterstitialAd(activity);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem favItem = menu.findItem(R.id.favorite_menu_item);
        favItem.setIcon(Manager.sharedManager().getFavoriteDrawable(isFavorite));
        super.onPrepareOptionsMenu(menu);
        MenuItem search = menu.findItem(R.id.search_menu_item);
        search.setVisible(false);
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
        if (interstitial != null && interstitial.isLoaded()) {
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
        dialog.setPositiveButton(getString(R.string.post_message_dialog_yes), (dialog1, which) -> {
            Bundle postContent = new Bundle();
            String postMsg = String.format(getString(R.string.post_message), mAuthorName, mCaption, mContent);
            postContent.putString(Constants.FACEBOOK_MESSAGE, postMsg);
            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "me/feed", postContent, HttpMethod.POST, response -> {
                        if (null == response.getError()) {
                            Toast.makeText(getContext(), String.format(getString(R.string.post_message_success), mCaption),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), String.format(getString(R.string.post_message_error), mCaption),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            request.executeAsync();
            showInterstitial();
        }).setNegativeButton(getString(R.string.post_message_dialog_no), (cancelDialog, which) -> cancelDialog.cancel()).setCancelable(false);
        AlertDialog alert = dialog.create();
        alert.show();
    }
}
