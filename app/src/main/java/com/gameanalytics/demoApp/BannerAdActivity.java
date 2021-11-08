/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.demoApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyperbid.banner.api.HBBannerExListener;
import com.hyperbid.banner.api.HBBannerView;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.core.api.HBNetworkConfirmInfo;
import com.hyperbid.network.admob.AdmobHBConst;

import java.util.HashMap;
import java.util.Map;

public class BannerAdActivity extends Activity {

    private static final String TAG = BannerAdActivity.class.getSimpleName();

    HBBannerView mBannerView;

    int mCurrentSelectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banner);
        mCurrentSelectIndex = 0;

        Log.i(TAG, "Placement id:" + PlacementId.BANNER_AD_ID);
        Spinner spinner = (Spinner) findViewById(R.id.banner_spinner);
        final FrameLayout frameLayout = findViewById(R.id.adview_container);
        mBannerView = new HBBannerView(this);
        mBannerView.setPlacementId(PlacementId.BANNER_AD_ID);
        frameLayout.addView(mBannerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(this, 300)));
        mBannerView.setBannerAdListener(new HBBannerExListener() {

            @Override
            public void onDeeplinkCallback(boolean isRefresh, HBAdInfo adInfo, boolean isSuccess) {
                Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onDownloadConfirm(Context context, HBAdInfo hbAdInfo, HBNetworkConfirmInfo hbNetworkConfirmInfo) {
                Log.i(TAG, "onDownloadConfirm");
            }

            @Override
            public void onBannerLoaded() {
                Log.i(TAG, "onBannerLoaded");
                Toast.makeText(BannerAdActivity.this,
                        "onBannerLoaded",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.i(TAG, "onBannerFailed: " + adError.getFullErrorInfo());
                Toast.makeText(BannerAdActivity.this,
                        "onBannerFailed: " + adError.getFullErrorInfo(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerClicked(HBAdInfo entity) {
                Log.i(TAG, "onBannerClicked:" + entity.toString());
                Toast.makeText(BannerAdActivity.this,
                        "onBannerClicked",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerShow(HBAdInfo entity) {
                Log.i(TAG, "onBannerShow:" + entity.toString());
                Toast.makeText(BannerAdActivity.this,
                        "onBannerShow",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerClose(HBAdInfo entity) {
                Log.i(TAG, "onBannerClose:" + entity.toString());
                Toast.makeText(BannerAdActivity.this,
                        "onBannerClose",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerAutoRefreshed(HBAdInfo entity) {
                Log.i(TAG, "onBannerAutoRefreshed:" + entity.toString());
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.i(TAG, "onBannerAutoRefreshFail: " + adError.getFullErrorInfo());

            }
        });

        findViewById(R.id.loadAd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> maps = new HashMap<>();
                //since v5.6.5
                Map<String, Object> localExtra = new HashMap<>();
                localExtra.put(AdmobHBConst.ADAPTIVE_TYPE, AdmobHBConst.ADAPTIVE_ANCHORED);
                localExtra.put(AdmobHBConst.ADAPTIVE_ORIENTATION, AdmobHBConst.ORIENTATION_CURRENT);
//                localExtra.put(AdmobHBConst.INLINE_ADAPTIVE_ORIENTATION, AdmobHBConst.ORIENTATION_PORTRAIT);
//                localExtra.put(AdmobHBConst.INLINE_ADAPTIVE_ORIENTATION, AdmobHBConst.ORIENTATION_LANDSCAPE);
                localExtra.put(AdmobHBConst.ADAPTIVE_WIDTH, getResources().getDisplayMetrics().widthPixels);
                mBannerView.setLocalExtra(localExtra);

                mBannerView.loadAd();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBannerView != null) {
            mBannerView.destroy();
        }
    }
}
