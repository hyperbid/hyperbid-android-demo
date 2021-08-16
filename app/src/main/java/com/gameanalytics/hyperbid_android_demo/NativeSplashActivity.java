/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 *
 */

package com.gameanalytics.hyperbid_android_demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.nativead.splash.api.HBNativeSplash;
import com.hyperbid.nativead.splash.api.HBNativeSplashListener;

import java.util.HashMap;
import java.util.Map;

public class NativeSplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_splash);
        FrameLayout splashView = findViewById(R.id.native_splash_view);
        Map<String, Object> localMap = new HashMap<>();
        HBNativeSplash splash = new HBNativeSplash(this, splashView, null, DemoApplication.mPlacementId_native_all, localMap, new HBNativeSplashListener() {
            @Override
            public void onAdLoaded() {
                Log.i("SplashActivity", "Develop callback loaded");
            }

            @Override
            public void onNoAdError(String msg) {
                Log.i("NativeSplashActivity", "Develop callback onNoAdError :" + msg);
                Toast.makeText(NativeSplashActivity.this, "load ad error: " + msg, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onAdShow(HBAdInfo entity) {
                Log.i("NativeSplashActivity", "Develop callback onAdShow:" + entity.toString());
            }

            @Override
            public void onAdClick(HBAdInfo entity) {
                Log.i("NativeSplashActivity", "Develop callback onAdClick:" + entity.toString());
            }

            @Override
            public void onAdSkip() {
                Log.i("NativeSplashActivity", "Develop callback onAdSkip");
                finish();
            }

            @Override
            public void onAdTimeOver() {
                Log.i("NativeSplashActivity", "Develop callback onAdTimeOver");
                finish();
            }

            @Override
            public void onAdTick(long millisUtilFinished) {
                Log.i("NativeSplashActivity", "Develop callback onAdTick:" + millisUtilFinished);
            }
        });

    }
}
