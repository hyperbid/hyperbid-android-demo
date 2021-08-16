/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.hyperbid_android_demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.HBAdStatusInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.interstitial.api.HBInterstitial;
import com.hyperbid.interstitial.api.HBInterstitialExListener;

public class InterstitialAdActivity extends Activity {

    private static final String TAG = InterstitialAdActivity.class.getSimpleName();

    String placementIds[] = new String[]{
            DemoApplication.mPlacementId_interstitial_all
            , DemoApplication.mPlacementId_interstitial_adcolony
            , DemoApplication.mPlacementId_interstitial_admob
            , DemoApplication.mPlacementId_interstitial_adx
            , DemoApplication.mPlacementId_interstitial_applovin
            , DemoApplication.mPlacementId_interstitial_appnext
            , DemoApplication.mPlacementId_interstitial_CHARTBOOST
            , DemoApplication.mPlacementId_interstitial_facebook
            , DemoApplication.mPlacementId_interstitial_fyber
            , DemoApplication.mPlacementId_interstitial_googleAdManager
            , DemoApplication.mPlacementId_interstitial_huawei
            , DemoApplication.mPlacementId_interstitial_inmobi
            , DemoApplication.mPlacementId_interstitial_IRONSOURCE
            , DemoApplication.mPlacementId_interstitial_kidoz
            , DemoApplication.mPlacementId_interstitial_mintegral
            , DemoApplication.mPlacementId_interstitial_video_mintegral
            , DemoApplication.mPlacementId_interstitia_maio
            , DemoApplication.mPlacementId_interstitial_mopub
            , DemoApplication.mPlacementId_interstitial_myoffer
            , DemoApplication.mPlacementId_interstitial_mytarget
            , DemoApplication.mPlacementId_interstitial_nend
            , DemoApplication.mPlacementId_interstitial_ogury
            , DemoApplication.mPlacementId_interstitial_online
            , DemoApplication.mPlacementId_interstitial_toutiao
            , DemoApplication.mPlacementId_interstitial_toutiao_video
            , DemoApplication.mPlacementId_interstitia_startapp
            , DemoApplication.mPlacementId_interstitial_TAPJOY
            , DemoApplication.mPlacementId_interstitial_UNITYAD
            , DemoApplication.mPlacementId_interstitial_vungle
    };

    String unitGroupName[] = new String[]{
            "All network",
            "Adcolony",
            "Admob",
            "Adx",
            "Applovin",
            "Appnext",
            "Chartboost",
            "Facebook",
            "Fyber",
            "Google Ad Manager",
            "Huawei",
            "Inmobi",
            "Ironsource",
            "Kidoz",
            "Maio",
            "Mintegral",
            "Mintegral video",
            "Mopub",
            "Myoffer",
            "MyTarget",
            "Nend",
            "Ogury",
            "OnlineApi",
            "Pangle",
            "Pangle FullVideo",
            "Startapp",
            "Tapjoy",
            "Unity3d",
            "Vungle"
    };


    HBInterstitial mInterstitialAd;
    int mCurrentSelectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Spinner spinner = findViewById(R.id.placementSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                InterstitialAdActivity.this, android.R.layout.simple_spinner_dropdown_item,
                unitGroupName);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(InterstitialAdActivity.this,
                        parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                mCurrentSelectIndex = position;
                init();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        init();

        findViewById(R.id.is_ad_ready_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HBAdStatusInfo atAdStatusInfo = mInterstitialAd.getReadyAdInfo();
                Toast.makeText(InterstitialAdActivity.this, "interstitial ad ready status:" + atAdStatusInfo.isReady(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.loadAd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.load();
            }
        });

        findViewById(R.id.show_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.show(InterstitialAdActivity.this);
            }
        });

    }


    private void init() {
        mInterstitialAd = new HBInterstitial(this, placementIds[mCurrentSelectIndex]);
        mInterstitialAd.setAdListener(new HBInterstitialExListener() {

            @Override
            public void onDeeplinkCallback(HBAdInfo adInfo, boolean isSuccess) {
                Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onInterstitialAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded");
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdLoadFail(AdError adError) {
                Log.i(TAG, "onInterstitialAdLoadFail:\n" + adError.getFullErrorInfo());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdLoadFail:" + adError.getFullErrorInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdClicked(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdClicked:\n" + entity.toString());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdShow(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdShow:\n" + entity.toString());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdClose(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdClose:\n" + entity.toString());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdClose", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdVideoStart(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdVideoStart:\n" + entity.toString());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdVideoStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdVideoEnd(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdVideoEnd:\n" + entity.toString());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdVideoEnd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {
                Log.i(TAG, "onInterstitialAdVideoError:\n" + adError.getFullErrorInfo());
                Toast.makeText(InterstitialAdActivity.this, "onInterstitialAdVideoError", Toast.LENGTH_SHORT).show();
            }

        });
    }

}

