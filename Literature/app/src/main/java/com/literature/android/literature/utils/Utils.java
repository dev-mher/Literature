package com.literature.android.literature.utils;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.literature.android.literature.R;

public class Utils {

    public static void loadAdView(Context context, AdView adView) {
        if (context == null) {
            return;
        }
        AdRequest.Builder adRequest = getAdRequestBuilder();
        adView.loadAd(adRequest.build());
    }

    @Nullable
    public static InterstitialAd loadInterstitialAd(Context context) {
        if (context == null) {
            return null;
        }
        InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.banner_ad_interstitial_unit_id));
        AdRequest.Builder adRequestInterstitial = getAdRequestBuilder();
        interstitialAd.loadAd(adRequestInterstitial.build());
        return interstitialAd;
    }

    @NonNull
    private static AdRequest.Builder getAdRequestBuilder() {
        return new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle());
    }

    private static Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        return extras;
    }
}
