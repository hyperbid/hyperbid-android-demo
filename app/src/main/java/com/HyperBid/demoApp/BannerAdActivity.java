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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.HyperBid.demoApp.Util.PlacementIdUtil;
import com.HyperBid.demoApp.Util.ViewUtil;
import com.hyperbid.banner.api.HBBannerExListener;
import com.hyperbid.banner.api.HBBannerView;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.core.api.HBAdSourceStatusListener;
import com.hyperbid.core.api.HBNetworkConfirmInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BannerAdActivity extends Activity {


    private static final String TAG = BannerAdActivity.class.getSimpleName();

    HBBannerView mBannerView;

    private TextView tvLoadAdBtn;
    private TextView tvShowLog;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_banner);

        findViewById(R.id.rl_type).setSelected(true);
        tvShowLog = findViewById(R.id.tv_show_log);
        tvShowLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvShowLog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ||motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.hyperbid_title_banner);
        titleBar.setListener(new TitleBarClickListener() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });

        tvLoadAdBtn = findViewById(R.id.banner_load_ad_btn);

        Map<String, String> placementIdMap = PlacementIdUtil.getBannerPlacements(this);
        List<String> placementNameList = new ArrayList<>(placementIdMap.keySet());

        Spinner spinner = (Spinner) findViewById(R.id.spinner_1);
        final FrameLayout frameLayout = findViewById(R.id.adview_container);

        String placementName = placementNameList.get(0);
        init(placementIdMap.get(placementName));

        frameLayout.addView(mBannerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dip2px(300)));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                BannerAdActivity.this, android.R.layout.simple_spinner_dropdown_item,
                placementNameList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
                String placementName = parent.getSelectedItem().toString();
                mBannerView.setPlacementId(placementIdMap.get(placementName));
                mBannerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        scrollView = findViewById(R.id.scroll_view);

        tvLoadAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.VISIBLE);
                mBannerView.loadAd();
            }
        });

    }

    private void init(String placementId) {
        mBannerView = new HBBannerView(this);
        mBannerView.setPlacementId(placementId);

        mBannerView.setBannerAdListener(new HBBannerExListener() {

            @Override
            public void onDeeplinkCallback(boolean isRefresh, HBAdInfo adInfo, boolean isSuccess) {
                Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onDownloadConfirm(Context context, HBAdInfo adInfo, HBNetworkConfirmInfo networkConfirmInfo) {

            }

            @Override
            public void onBannerLoaded() {
                Log.i(TAG, "onBannerLoaded");
                ViewUtil.printLog(tvShowLog,"onBannerLoaded");
                if (scrollView != null) {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.i(TAG, "onBannerFailed: " + adError.getFullErrorInfo());
                ViewUtil.printLog(tvShowLog,"onBannerFailed" + adError.getFullErrorInfo());
            }

            @Override
            public void onBannerClicked(HBAdInfo entity) {
                Log.i(TAG, "onBannerClicked:" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onBannerClicked");
            }

            @Override
            public void onBannerShow(HBAdInfo entity) {
                Log.i(TAG, "onBannerShow:" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onBannerShow");
            }

            @Override
            public void onBannerClose(HBAdInfo entity) {
                Log.i(TAG, "onBannerClose:" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onBannerClose");
            }

            @Override
            public void onBannerAutoRefreshed(HBAdInfo entity) {
                Log.i(TAG, "onBannerAutoRefreshed:" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onBannerAutoRefreshed");
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.i(TAG, "onBannerAutoRefreshFail: " + adError.getFullErrorInfo());
                ViewUtil.printLog(tvShowLog,"onBannerAutoRefreshFail");
            }
        });

        mBannerView.setAdSourceStatusListener(new HBAdSourceStatusListener() {
            @Override
            public void onAdSourceBiddingAttempt(HBAdInfo adInfo) {
                Log.i(TAG, "onAdSourceBiddingAttempt: " + adInfo.toString());
            }

            @Override
            public void onAdSourceBiddingFilled(HBAdInfo adInfo) {
                Log.i(TAG, "onAdSourceBiddingFilled: " + adInfo.toString());
            }

            @Override
            public void onAdSourceBiddingFail(HBAdInfo adInfo, AdError adError) {
                Log.i(TAG, "onAdSourceBiddingFail Info: " + adInfo.toString());
                Log.i(TAG, "onAdSourceBiddingFail error: " + adError.getFullErrorInfo());
            }

            @Override
            public void onAdSourceAttempt(HBAdInfo hbAdInfo) {

            }



            @Override
            public void onAdSourceLoadFilled(HBAdInfo adInfo) {
                Log.i(TAG, "onAdSourceLoadFilled: " + adInfo.toString());
            }

            @Override
            public void onAdSourceLoadFail(HBAdInfo adInfo, AdError adError) {
                Log.i(TAG, "onAdSourceLoadFail Info: " + adInfo.toString());
                Log.i(TAG, "onAdSourceLoadFail error: " + adError.getFullErrorInfo());
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

    public int dip2px(float dipValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
