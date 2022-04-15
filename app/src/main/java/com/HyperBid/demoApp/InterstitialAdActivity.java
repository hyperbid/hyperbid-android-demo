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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.HyperBid.demoApp.Util.PlacementIdUtil;
import com.HyperBid.demoApp.Util.ViewUtil;
import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.HBAdSourceStatusListener;
import com.hyperbid.core.api.HBAdStatusInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.core.api.HBNetworkConfirmInfo;
import com.hyperbid.interstitial.api.HBInterstitial;
import com.hyperbid.interstitial.api.HBInterstitialAutoAd;
import com.hyperbid.interstitial.api.HBInterstitialAutoEventListener;
import com.hyperbid.interstitial.api.HBInterstitialAutoLoadListener;
import com.hyperbid.interstitial.api.HBInterstitialExListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterstitialAdActivity extends Activity {

    private static final String TAG = InterstitialAdActivity.class.getSimpleName();

    HBInterstitial mInterstitialAd;
    private String mCurrentNetworkName;
    private Map<String, String> mPlacementIdMap;
    private Map<String, Boolean> mAutoLoadPlacementIdMap = new HashMap<>();
    private boolean isAutoLoad;

    private Spinner mSpinner;
    private CheckBox ckAutoLoad;
    private TextView tvLoadAdBtn;
    private TextView tvIsAdReadyBtn;
    private TextView tvShowAdBtn;
    private TextView tvShowLog;

    private RelativeLayout rlInterstitial;
    private RelativeLayout rlfullscreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_interstitial);

       HBInterstitialAutoAd.init(this, null, autoLoadListener);

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.hyperbid_title_interstitial);
        titleBar.setListener(new TitleBarClickListener() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });

        tvShowLog = findViewById(R.id.tv_show_log);
        tvShowLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvLoadAdBtn = findViewById(R.id.load_ad_btn);
        tvIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        tvShowAdBtn = findViewById(R.id.show_ad_btn);
        rlInterstitial = findViewById(R.id.rl_interstitial);
        rlfullscreen = findViewById(R.id.rl_fullscreen);


        mPlacementIdMap = PlacementIdUtil.getInterstitialPlacements(this);
        List<String> placementNameList = new ArrayList<>(mPlacementIdMap.keySet());
        mCurrentNetworkName = placementNameList.get(0);

        mSpinner = (Spinner) findViewById(R.id.spinner_1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                placementNameList);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

//                Toast.makeText(InterstitialAdActivity.this,
//                        parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();

                mCurrentNetworkName = parent.getSelectedItem().toString();
                String placementId = mPlacementIdMap.get(mCurrentNetworkName);
                init(placementId);
                if (mAutoLoadPlacementIdMap.get(placementId) != null && mAutoLoadPlacementIdMap.get(placementId)) {
                    ckAutoLoad.setChecked(true);
                } else {
                    ckAutoLoad.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        initAutoLoad();

        String placementId = mPlacementIdMap.get(mCurrentNetworkName);
        init(placementId);

        tvIsAdReadyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdReady();
            }
        });

        tvLoadAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        tvShowAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });


    }

    private void initAutoLoad() {
        ckAutoLoad = findViewById(R.id.ck_auto_load);
        ckAutoLoad.setVisibility(View.VISIBLE);
        ckAutoLoad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAutoLoad = true;
                    mAutoLoadPlacementIdMap.put(mPlacementIdMap.get(mCurrentNetworkName),true);
                    HBInterstitialAutoAd.addPlacementId(mPlacementIdMap.get(mCurrentNetworkName));
                    tvLoadAdBtn.setVisibility(View.GONE);
                } else {
                    isAutoLoad = false;
                    mAutoLoadPlacementIdMap.put(mPlacementIdMap.get(mCurrentNetworkName),false);
                    HBInterstitialAutoAd.removePlacementId(mPlacementIdMap.get(mCurrentNetworkName));
                    tvLoadAdBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void loadAd() {
        Map<String, Object> localMap = new HashMap<>();

        // Only for GDT (true: open download dialog, false: download directly)
        localMap.put(HBAdConst.KEY.AD_CLICK_CONFIRM_STATUS, true);

        mInterstitialAd.setLocalExtra(localMap);
        mInterstitialAd.load();
    }

    private void isAdReady() {
        if (isAutoLoad) {
            ViewUtil.printLog(tvShowLog, "interstitial auto load ad ready status:" +   HBInterstitialAutoAd.isAdReady(mPlacementIdMap.get(mCurrentNetworkName)));
        } else {
//         boolean isReady = mInterstitialAd.isAdReady();
            HBAdStatusInfo atAdStatusInfo = mInterstitialAd.getReadyAdInfo();
            ViewUtil.printLog(tvShowLog, "interstitial ad ready status:" + atAdStatusInfo.isReady());
            List<  HBAdInfo> atAdInfoList = mInterstitialAd.checkValidAdCaches();
            Log.i(TAG, "Valid Cahce size:" + (atAdInfoList != null ? atAdInfoList.size() : 0));
            if (atAdInfoList != null) {
                for (  HBAdInfo adInfo : atAdInfoList) {
                    Log.i(TAG, "\nCahce detail:" + adInfo.toString());
                }
            }
        }
    }

    private void showAd() {
        if (isAutoLoad) {
            HBInterstitialAutoAd.show(this, mPlacementIdMap.get(mCurrentNetworkName), autoEventListener);
        } else {
            mInterstitialAd.show(InterstitialAdActivity.this);
//        mInterstitialAd.show(InterstitialAdActivity.this, "f5e54937b0483d");
        }
    }

    private void init(String placementId) {
        mInterstitialAd = new HBInterstitial(this, placementId);
        HBInterstitial.entryAdScenario(placementId, "f5e54937b0483d");

        mInterstitialAd.setAdListener(new  HBInterstitialExListener() {

            @Override
            public void onDeeplinkCallback( HBAdInfo adInfo, boolean isSuccess) {
                Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onDownloadConfirm(Context context,  HBAdInfo adInfo, HBNetworkConfirmInfo networkConfirmInfo) {

            }

            @Override
            public void onInterstitialAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded");
                ViewUtil.printLog(tvShowLog,"onInterstitialAdLoaded");
            }

            @Override
            public void onInterstitialAdLoadFail(AdError adError) {
                Log.i(TAG, "onInterstitialAdLoadFail:\n" + adError.getFullErrorInfo());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdLoadFail:" + adError.getFullErrorInfo());
            }

            @Override
            public void onInterstitialAdClicked( HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdClicked:\n" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdClicked");
            }

            @Override
            public void onInterstitialAdShow( HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdShow:\n" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdShow");
            }

            @Override
            public void onInterstitialAdClose( HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdClose:\n" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdClose");
            }

            @Override
            public void onInterstitialAdVideoStart( HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdVideoStart:\n" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdVideoStart");
            }

            @Override
            public void onInterstitialAdVideoEnd( HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdVideoEnd:\n" + entity.toString());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdVideoEnd");
            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {
                Log.i(TAG, "onInterstitialAdVideoError:\n" + adError.getFullErrorInfo());
                ViewUtil.printLog(tvShowLog,"onInterstitialAdVideoError");
            }

        });



        mInterstitialAd.setAdSourceStatusListener(new HBAdSourceStatusListener() {
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
            public void onAdSourceAttemp(HBAdInfo hbAdInfo) {

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

    private HBInterstitialAutoLoadListener autoLoadListener = new HBInterstitialAutoLoadListener() {
        @Override
        public void onInterstitialAutoLoaded(String placementId) {
            Log.i(TAG,"PlacementId:" + placementId + ": onInterstitialAutoLoaded");
            ViewUtil.printLog(tvShowLog,"PlacementId:" + placementId + ": onInterstitialAutoLoaded");
        }

        @Override
        public void onInterstitialAutoLoadFail(String placementId, AdError adError) {
            Log.i(TAG,"PlacementId:" + placementId + ": onInterstitialAutoLoadFail:\n" + adError.getFullErrorInfo());
            ViewUtil.printLog(tvShowLog,"PlacementId:" + placementId + ": onInterstitialAutoLoadFail:\n" + adError.getFullErrorInfo());
        }

    };

    HBInterstitialAutoEventListener autoEventListener = new HBInterstitialAutoEventListener() {
        @Override
        public void onInterstitialAdClicked(HBAdInfo hbAdInfo) {
            ViewUtil.printLog(tvShowLog,"onInterstitialAdClicked:");
        }

        @Override
        public void onInterstitialAdShow(HBAdInfo hbAdInfo) {
            ViewUtil.printLog(tvShowLog,"onInterstitialAdShow");
        }

        @Override
        public void onInterstitialAdClose(HBAdInfo hbAdInfo) {
            ViewUtil.printLog(tvShowLog,"onInterstitialAdClose");
        }

        @Override
        public void onInterstitialAdVideoStart(HBAdInfo hbAdInfo) {
            ViewUtil.printLog(tvShowLog,"onInterstitialAdVideoStart");
        }

        @Override
        public void onInterstitialAdVideoEnd(HBAdInfo hbAdInfo) {
            ViewUtil.printLog(tvShowLog,"onInterstitialAdVideoEnd");
        }

        @Override
        public void onInterstitialAdVideoError(AdError adError) {
            ViewUtil.printLog(tvShowLog,"onInterstitialAdVideoError" + adError.getFullErrorInfo());
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoLoadListener != null) {
            autoLoadListener = null;
        }
        for (Map.Entry<String, Boolean> entry : mAutoLoadPlacementIdMap.entrySet()) {
            HBInterstitialAutoAd.removePlacementId(entry.getKey());
        }
    }

}

