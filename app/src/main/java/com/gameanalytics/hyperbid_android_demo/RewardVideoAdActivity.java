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

import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.HBAdStatusInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.rewardvideo.api.HBRewardVideoAd;
import com.hyperbid.rewardvideo.api.HBRewardVideoExListener;

import java.util.HashMap;
import java.util.Map;

public class RewardVideoAdActivity extends Activity {

    private static final String TAG = RewardVideoAdActivity.class.getSimpleName();

    String placementIds[] = new String[]{
            DemoApplication.mPlacementId_rewardvideo_all
            , DemoApplication.mPlacementId_rewardvideo_adcolony
            , DemoApplication.mPlacementId_rewardvideo_admob
            , DemoApplication.mPlacementId_rewardvideo_adx
            , DemoApplication.mPlacementId_rewardvideo_applovin
            , DemoApplication.mPlacementId_rewardvideo_appnext
            , DemoApplication.mPlacementId_rewardvideo_CHARTBOOST
            , DemoApplication.mPlacementId_rewardvideo_facebook
            , DemoApplication.mPlacementId_rewardvideo_fyber
            , DemoApplication.mPlacementId_rewardvideo_googleAdManager
            , DemoApplication.mPlacementId_rewardvideo_huawei
            , DemoApplication.mPlacementId_rewardvideo_inmobi
            , DemoApplication.mPlacementId_rewardvideo_IRONSOURCE
            , DemoApplication.mPlacementId_rewardvideo_kidoz
            , DemoApplication.mPlacementId_rewardvideo_maio
            , DemoApplication.mPlacementId_rewardvideo_mintegral
            , DemoApplication.mPlacementId_rewardvideo_mopub
            , DemoApplication.mPlacementId_rewardvideo_myoffer
            , DemoApplication.mPlacementId_rewardvideo_mytarget
            , DemoApplication.mPlacementId_rewardvideo_nend
            , DemoApplication.mPlacementId_rewardvideo_ogury
            , DemoApplication.mPlacementId_rewardvideo_online
            , DemoApplication.mPlacementId_rewardvideo_toutiao
            , DemoApplication.mPlacementId_rewardvideo_startapp
            , DemoApplication.mPlacementId_rewardvideo_TAPJOY
            , DemoApplication.mPlacementId_rewardvideo_UNITYAD
            , DemoApplication.mPlacementId_rewardvideo_vungle
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
            "Mopub",
            "Myoffer",
            "MyTarget",
            "Nend",
            "Ogury",
            "OnlineApi",
            "Pangle",
            "StartApp",
            "Tapjoy",
            "Unity3d",
            "Vungle"
    };

    int mCurrentSelectIndex;

    HBRewardVideoAd mRewardVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Spinner spinner = findViewById(R.id.placementSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                RewardVideoAdActivity.this, android.R.layout.simple_spinner_dropdown_item,
                unitGroupName);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(RewardVideoAdActivity.this,
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
                HBAdStatusInfo atAdStatusInfo = mRewardVideoAd.getReadyAdInfo();
                Toast.makeText(RewardVideoAdActivity.this, "video ad ready status:" + atAdStatusInfo.isReady(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.loadAd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRewardVideoAd.load();
            }
        });

        findViewById(R.id.show_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRewardVideoAd.show(RewardVideoAdActivity.this);
            }
        });

    }


    private void init() {
        mRewardVideoAd = new HBRewardVideoAd(this, placementIds[mCurrentSelectIndex]);
        String userid = "test_userid_001";
        String userdata = "test_userdata_001";
        Map<String, Object> localMap = new HashMap<>();
        localMap.put(HBAdConst.KEY.USER_ID, userid);
        localMap.put(HBAdConst.KEY.USER_CUSTOM_DATA, userdata);
        mRewardVideoAd.setLocalExtra(localMap);
        mRewardVideoAd.setAdListener(new HBRewardVideoExListener() {

            @Override
            public void onDeeplinkCallback(HBAdInfo adInfo, boolean isSuccess) {
                Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i(TAG, "onRewardedVideoAdLoaded");
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailed(AdError errorCode) {
                Log.i(TAG, "onRewardedVideoAdFailed error:" + errorCode.getFullErrorInfo());
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdFailed:" + errorCode.getFullErrorInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdPlayStart(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayStart:\n" + entity.toString());
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdPlayStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdPlayEnd(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayEnd:\n" + entity.toString());
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdPlayEnd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError errorCode, HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayFailed error:" + errorCode.getFullErrorInfo());
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdPlayFailed:" + errorCode.getFullErrorInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdClosed:\n" + entity.toString() );
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdPlayClicked(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayClicked:\n" + entity.toString());
                Toast.makeText(RewardVideoAdActivity.this, "onRewardedVideoAdPlayClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReward(HBAdInfo entity) {
                Log.e(TAG, "onReward:\n" + entity.toString());
                Toast.makeText(RewardVideoAdActivity.this, "onReward", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

