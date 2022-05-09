/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.HyperBid.demoApp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.HBMediationRequestInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.core.api.HBNetworkConfirmInfo;
import com.hyperbid.splashad.api.HBSplashAd;
import com.hyperbid.splashad.api.HBSplashAdExtraInfo;
import com.hyperbid.splashad.api.HBSplashExListener;
import com.hyperbid.splashad.api.IHBSplashEyeAd;

import java.util.HashMap;
import java.util.Map;

public class SplashAdShowActivity extends Activity implements HBSplashExListener {

    private static final String TAG = SplashAdShowActivity.class.getSimpleName();

    HBSplashAd splashAd;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_ad_show);

        String placementId = getIntent().getStringExtra("placementId");
        container = findViewById(R.id.splash_ad_container);
        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        Configuration cf = getResources().getConfiguration();

        int ori = cf.orientation;

        /**You should set size to the layout param.**/
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);
        }

        HBMediationRequestInfo atMediationRequestInfo = null;

//        atMediationRequestInfo = new MintegralHBRequestInfo("100947", "ef13ef712aeb0f6eb3d698c4c08add96", "210169", "276803");
//        atMediationRequestInfo.setAdSourceId("68188");

//        atMediationRequestInfo = new AdmobHBRequestInfo("ca-app-pub-9488501426181082~6354662111", "ca-app-pub-3940256099942544/1033173712", AdmobHBRequestInfo.ORIENTATION_PORTRAIT);
//        atMediationRequestInfo.setAdSourceId("145022");
        splashAd = new HBSplashAd(this, placementId, atMediationRequestInfo, this, 5000);

        Map<String, Object> localMap = new HashMap<>();
        localMap.put(HBAdConst.KEY.AD_WIDTH, layoutParams.width);
        localMap.put(HBAdConst.KEY.AD_HEIGHT, layoutParams.height);
        splashAd.setLocalExtra(localMap);

        if (splashAd.isAdReady()) {
            Log.i(TAG, "SplashAd is ready to show.");
            splashAd.show(this, container);
        } else {
            Log.i(TAG, "SplashAd isn't ready to show, start to request.");
            splashAd.loadAd();
        }


        HBSplashAd.requestSettings(this, placementId, null);
    }

    @Override
    public void onAdDismiss(HBAdInfo hbAdInfo, HBSplashAdExtraInfo hbSplashAdExtraInfo) {

    }

    @Override
    public void onDeeplinkCallback(HBAdInfo adInfo, boolean isSuccess) {
        Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
    }

    @Override
    public void onDownloadConfirm(Context context, HBAdInfo hbAdInfo, HBNetworkConfirmInfo hbNetworkConfirmInfo) {

    }

    @Override
    public void onAdLoaded(boolean b) {
        Log.i(TAG, "onAdLoaded---------");
        splashAd.show(this, container);
    }

    @Override
    public void onAdLoadTimeout() {

    }

    @Override
    public void onNoAdError(AdError adError) {
        Log.i(TAG, "onNoAdError---------:" + adError.getFullErrorInfo());
        jumpToMainActivity();
    }

    @Override
    public void onAdShow(HBAdInfo entity) {
        Log.i(TAG, "onAdShow:\n" + entity.toString());
    }

    @Override
    public void onAdClick(HBAdInfo entity) {
        Log.i(TAG, "onAdClick:\n" + entity.toString());
    }



    boolean hasHandleJump = false;

    public void jumpToMainActivity() {
        if (!hasHandleJump) {
            hasHandleJump = true;
            finish();
            Toast.makeText(this, "start your MainActivity.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashAd != null) {
            splashAd.onDestory();
        }

    }
}
