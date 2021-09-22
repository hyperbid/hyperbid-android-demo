/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.demoApp;

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
    int mCurrentSelectIndex;

    HBRewardVideoAd mRewardVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

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
        mRewardVideoAd = new HBRewardVideoAd(this, PlacementId.REWARDED_VIDEO_ID);
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

