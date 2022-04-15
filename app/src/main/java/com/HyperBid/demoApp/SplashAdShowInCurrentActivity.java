/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.HyperBid.demoApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.HBAdStatusInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.core.api.HBNetworkConfirmInfo;
import com.hyperbid.splashad.api.HBSplashAd;
import com.hyperbid.splashad.api.HBSplashExListener;
import com.hyperbid.splashad.api.IHBSplashEyeAd;

import java.util.HashMap;
import java.util.Map;

public class SplashAdShowInCurrentActivity extends Activity {

    private static final String TAG = SplashAdShowInCurrentActivity.class.getSimpleName();

    HBSplashAd splashAd;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ad_show_in_current);

        String placementId = getIntent().getStringExtra("placementId");
        container = findViewById(R.id.splash_ad_container);

        initSplash(placementId);


        findViewById(R.id.show_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (splashAd.isAdReady()) {
                    if (container != null) {
                        container.setVisibility(View.VISIBLE);
                    }
                    splashAd.show(SplashAdShowInCurrentActivity.this, container);
                } else {
                    Toast.makeText(SplashAdShowInCurrentActivity.this, "splash no cache.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.loadAd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> localMap = new HashMap<>();
                localMap.put(HBAdConst.KEY.AD_WIDTH, getResources().getDisplayMetrics().widthPixels);
                localMap.put(HBAdConst.KEY.AD_HEIGHT, getResources().getDisplayMetrics().heightPixels);
                splashAd.setLocalExtra(localMap);

                splashAd.loadAd();
            }
        });

        findViewById(R.id.is_ad_ready_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean isReady = splashAd.isAdReady();
                HBAdStatusInfo atAdStatusInfo = splashAd.getReadyAdInfo();
                Toast.makeText(SplashAdShowInCurrentActivity.this, "splash ad ready status:" + atAdStatusInfo.isReady(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSplash(String placementId) {
        splashAd = new HBSplashAd(getApplicationContext(), placementId, null, new HBSplashExListener() {

            @Override
            public void onDeeplinkCallback(HBAdInfo adInfo, boolean isSuccess) {
                Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onDownloadConfirm(Context context, HBAdInfo hbAdInfo, HBNetworkConfirmInfo hbNetworkConfirmInfo) {
                Log.i(TAG, "onDownloadConfirm");
            }

            @Override
            public void onAdLoaded(boolean b) {
                Log.i(TAG, "onAdLoaded---------");
                Toast.makeText(SplashAdShowInCurrentActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoadTimeout() {
                Log.i(TAG, "onAdLoadTimeout");
            }

            @Override
            public void onNoAdError(AdError adError) {
                Log.i(TAG, "onNoAdError---------:" + adError.getFullErrorInfo());
                Toast.makeText(SplashAdShowInCurrentActivity.this, "onNoAdError: " + adError.getFullErrorInfo(), Toast.LENGTH_SHORT).show();
                if (container != null) {
                    container.removeAllViews();
                    container.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdShow(HBAdInfo entity) {
                Log.i(TAG, "onAdShow:\n" + entity.toString());
                Toast.makeText(SplashAdShowInCurrentActivity.this, "onAdShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClick(HBAdInfo entity) {
                Log.i(TAG, "onAdClick:\n" + entity.toString());
                Toast.makeText(SplashAdShowInCurrentActivity.this, "onAdClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDismiss(HBAdInfo entity, IHBSplashEyeAd ihbSplashEyeAd) {
                Log.i(TAG, "onAdDismiss---------:" + entity.toString());
                Toast.makeText(SplashAdShowInCurrentActivity.this, "onAdDismiss", Toast.LENGTH_SHORT).show();
                if (container != null) {
                    container.removeAllViews();
                    container.setVisibility(View.GONE);
                }
            }

        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        splashAd = null;
        container = null;
    }
}