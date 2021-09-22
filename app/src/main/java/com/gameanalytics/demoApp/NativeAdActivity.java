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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.nativead.api.HBNative;
import com.hyperbid.nativead.api.HBNativeAdView;
import com.hyperbid.nativead.api.HBNativeDislikeListener;
import com.hyperbid.nativead.api.HBNativeEventExListener;
import com.hyperbid.nativead.api.HBNativeNetworkListener;
import com.hyperbid.nativead.api.NativeAd;

import java.util.HashMap;
import java.util.Map;

public class NativeAdActivity extends Activity {

    private static final String TAG = NativeAdActivity.class.getSimpleName();

    HBNative atNative;
    HBNativeAdView hyperbidNativeAdView;
    NativeAd mNativeAd;

    ImageView mCloseView;

    int mCurrentSelectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native);

        Spinner spinner = (Spinner) findViewById(R.id.native_spinner);

        initCloseView();

        int padding = CommonUtil.dip2px(this, 10);
        final int containerHeight = CommonUtil.dip2px(this, 340);
        final int adViewWidth = getResources().getDisplayMetrics().widthPixels - 2 * padding;
        final int adViewHeight = containerHeight - 2 * padding;

        final NativeDemoRender hyperbidRender = new NativeDemoRender(this);
        hyperbidRender.setCloseView(mCloseView);

        atNative = new HBNative(this, PlacementId.NATIVE_BANNER_ID, new HBNativeNetworkListener() {
                @Override
                public void onNativeAdLoaded() {
                    Log.i(TAG, "onNativeAdLoaded");
                    Toast.makeText(NativeAdActivity.this, "load success..."
                            , Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNativeAdLoadFail(AdError adError) {
                    Log.i(TAG, "onNativeAdLoadFail, " + adError.getFullErrorInfo());
                    Toast.makeText(NativeAdActivity.this, "load fail...：" + adError.getFullErrorInfo(), Toast.LENGTH_LONG).show();

                }
            });

        if (hyperbidNativeAdView == null) {
            hyperbidNativeAdView = new HBNativeAdView(this);
        }


        findViewById(R.id.loadAd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hyperbidNativeAdView != null) {
                    hyperbidNativeAdView.removeAllViews();

                    if (hyperbidNativeAdView.getParent() == null) {
                        ((FrameLayout) findViewById(R.id.ad_container)).addView(hyperbidNativeAdView, new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, containerHeight));
                    }
                }

                Map<String, Object> localMap = new HashMap<>();

                // since v5.6.4
                localMap.put(HBAdConst.KEY.AD_WIDTH, adViewWidth);
                localMap.put(HBAdConst.KEY.AD_HEIGHT, adViewHeight);

                atNative.setLocalExtra(localMap);
                atNative.loadAd();
            }
        });

        findViewById(R.id.loadcache_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeAd nativeAd = atNative.getAd();
                if (nativeAd != null) {

                    if (hyperbidNativeAdView != null) {
                        hyperbidNativeAdView.removeAllViews();

                        if (hyperbidNativeAdView.getParent() == null) {
                            ((FrameLayout) findViewById(R.id.ad_container)).addView(hyperbidNativeAdView, new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, containerHeight));
                        }
                    }

                    if (mNativeAd != null) {
                        mNativeAd.destory();
                    }
                    mNativeAd = nativeAd;
                    mNativeAd.setNativeEventListener(new HBNativeEventExListener() {
                        @Override
                        public void onDeeplinkCallback(HBNativeAdView view, HBAdInfo adInfo, boolean isSuccess) {
                            Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
                        }

                        @Override
                        public void onAdImpressed(HBNativeAdView view, HBAdInfo entity) {
                            Log.i(TAG, "native ad onAdImpressed:\n" + entity.toString());
                        }

                        @Override
                        public void onAdClicked(HBNativeAdView view, HBAdInfo entity) {
                            Log.i(TAG, "native ad onAdClicked:\n" + entity.toString());
                        }

                        @Override
                        public void onAdVideoStart(HBNativeAdView view) {
                            Log.i(TAG, "native ad onAdVideoStart");
                        }

                        @Override
                        public void onAdVideoEnd(HBNativeAdView view) {
                            Log.i(TAG, "native ad onAdVideoEnd");
                        }

                        @Override
                        public void onAdVideoProgress(HBNativeAdView view, int progress) {
                            Log.i(TAG, "native ad onAdVideoProgress:" + progress);
                        }
                    });
                    mNativeAd.setDislikeCallbackListener(new HBNativeDislikeListener() {
                        @Override
                        public void onAdCloseButtonClick(HBNativeAdView view, HBAdInfo entity) {
                            Log.i(TAG, "native ad onAdCloseButtonClick");
                            if (view.getParent() != null) {
                                ((ViewGroup) view.getParent()).removeView(view);
                                view.removeAllViews();
                            }
                        }
                    });
                    try {
                        mNativeAd.renderAdView(hyperbidNativeAdView, hyperbidRender);
                    } catch (Exception e) {

                    }

                    hyperbidNativeAdView.addView(mCloseView);

                    hyperbidNativeAdView.setVisibility(View.VISIBLE);
                    mNativeAd.prepare(hyperbidNativeAdView, hyperbidRender.getClickView(), null);
                } else {
                    Toast.makeText(NativeAdActivity.this, "this placement no cache!", Toast.LENGTH_LONG).show();

                }

            }
        });
        hyperbidNativeAdView.setPadding(padding, padding, padding, padding);

    }

    private void initCloseView() {
        if (mCloseView == null) {
            mCloseView = new ImageView(this);
            mCloseView.setImageResource(R.drawable.ad_close);

            int padding = CommonUtil.dip2px(this, 5);
            mCloseView.setPadding(padding, padding, padding, padding);

            int size = CommonUtil.dip2px(this, 30);
            int margin = CommonUtil.dip2px(this, 2);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
            layoutParams.topMargin = margin;
            layoutParams.rightMargin = margin;
            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

            mCloseView.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNativeAd != null) {
            mNativeAd.destory();
        }
    }

    @Override
    protected void onPause() {
        if (mNativeAd != null) {
            mNativeAd.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mNativeAd != null) {
            mNativeAd.onResume();
        }
        super.onResume();
    }
}
