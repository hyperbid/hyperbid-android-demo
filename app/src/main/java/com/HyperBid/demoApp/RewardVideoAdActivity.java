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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.HyperBid.demoApp.Util.PlacementIdUtil;
import com.HyperBid.demoApp.Util.ViewUtil;
import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.HBAdStatusInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.core.api.HBNetworkConfirmInfo;
import com.hyperbid.rewardvideo.api.HBRewardVideoAd;
import com.hyperbid.rewardvideo.api.HBRewardVideoAutoAd;
import com.hyperbid.rewardvideo.api.HBRewardVideoAutoEventListener;
import com.hyperbid.rewardvideo.api.HBRewardVideoAutoLoadListener;
import com.hyperbid.rewardvideo.api.HBRewardVideoExListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardVideoAdActivity extends Activity {

    private static final String TAG = RewardVideoAdActivity.class.getSimpleName();
    int mCurrentSelectIndex;

    HBRewardVideoAd mRewardVideoAd;


    private String mCurrentNetworkName;
    private Map<String, String> mPlacementIdMap;
    private Map<String, Boolean> mAutoLoadPlacementIdMap = new HashMap<>();
    private boolean isAutoLoad;

    private TextView tvLoadAdBtn;
    private TextView tvIsAdReadyBtn;
    private TextView tvShowAdBtn;
    private TextView tvShowLog;
    private CheckBox ckAutoLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_ad_layout);


        HBRewardVideoAutoAd.init(this, null, autoLoadListener);

        findViewById(R.id.rl_type).setSelected(true);

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.hyperbid_title_rewarded_video);
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

        mPlacementIdMap = PlacementIdUtil.getRewardedVideoPlacements(this);
        List<String> placementNameList = new ArrayList<>(mPlacementIdMap.keySet());
        mCurrentNetworkName = placementNameList.get(0);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                placementNameList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mCurrentNetworkName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(RewardVideoAdActivity.this,
//                        mCurrentNetworkName,
//                        Toast.LENGTH_SHORT).show();

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
                    HBRewardVideoAutoAd.addPlacementId(mPlacementIdMap.get(mCurrentNetworkName));
                    tvLoadAdBtn.setVisibility(View.GONE);
                } else {
                    isAutoLoad = false;
                    mAutoLoadPlacementIdMap.put(mPlacementIdMap.get(mCurrentNetworkName),false);
                    HBRewardVideoAutoAd.removePlacementId(mPlacementIdMap.get(mCurrentNetworkName));
                    tvLoadAdBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void loadAd() {
        String userid = "test_userid_001";
        String userdata = "test_userdata_001";
        Map<String, Object> localMap = new HashMap<>();
        localMap.put(HBAdConst.KEY.USER_ID, userid);
        localMap.put(HBAdConst.KEY.USER_CUSTOM_DATA, userdata);

        // Only for GDT (true: open download dialog, false: download directly)
        localMap.put(HBAdConst.KEY.AD_CLICK_CONFIRM_STATUS, true);

        mRewardVideoAd.setLocalExtra(localMap);
        mRewardVideoAd.load();
    }

    private void isAdReady() {

        if (isAutoLoad) {
            ViewUtil.printLog(tvShowLog, "video auto load ad ready status:" + HBRewardVideoAutoAd.isAdReady(mPlacementIdMap.get(mCurrentNetworkName)));
        } else {

            HBAdStatusInfo atAdStatusInfo =     mRewardVideoAd.getReadyAdInfo();

            ViewUtil.printLog(tvShowLog, "video ad ready status:" + atAdStatusInfo.isReady());

            List<HBAdInfo> atAdInfoList = mRewardVideoAd.checkValidAdCaches();
            Log.i(TAG, "Valid Cahce size:" + (atAdInfoList != null ? atAdInfoList.size() : 0));
            if (atAdInfoList != null) {
                for (HBAdInfo adInfo : atAdInfoList) {
                    Log.i(TAG, "\nCahce detail:" + adInfo.toString());
                }
            }
        }
    }

    private void showAd() {
        if (isAutoLoad) {
            HBRewardVideoAutoAd.show(this, mPlacementIdMap.get(mCurrentNetworkName), autoEventListener);
        } else {
            mRewardVideoAd.show(RewardVideoAdActivity.this);
//        mRewardVideoAd.show(RewardVideoAdActivity.this, "f5e5492eca9668");
        }
    }


    private void init(String placementId) {

        mRewardVideoAd = new HBRewardVideoAd(this, placementId);
        HBRewardVideoAd.entryAdScenario(placementId, "f5e5492eca9668");

        mRewardVideoAd.setAdListener(new HBRewardVideoExListener() {
            @Override
            public void onDeeplinkCallback(HBAdInfo hbAdInfo, boolean b) {

            }

            @Override
            public void onDownloadConfirm(Context context, HBAdInfo hbAdInfo, HBNetworkConfirmInfo hbNetworkConfirmInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayStart(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayEnd(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayFailed(AdError adError, HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayClicked(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onAgainReward(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdLoaded() {
                ViewUtil.printLog(tvShowLog,"onRewardVideoAdLoaded");
            }

            @Override
            public void onRewardedVideoAdFailed(AdError adError) {

            }

            @Override
            public void onRewardedVideoAdPlayStart(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdPlayEnd(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError adError, HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdClosed(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onRewardedVideoAdPlayClicked(HBAdInfo hbAdInfo) {

            }

            @Override
            public void onReward(HBAdInfo hbAdInfo) {

            }
        });

    }

    private HBRewardVideoAutoLoadListener autoLoadListener = new HBRewardVideoAutoLoadListener() {
        @Override
        public void onRewardVideoAutoLoaded(String placementId) {
            initPlacementIdLocalExtra(placementId);
            Log.i(TAG,"PlacementId:" + placementId + ": onRewardVideoAutoLoaded");
            ViewUtil.printLog(tvShowLog,"PlacementId:" + placementId + ": onRewardVideoAutoLoaded");
        }

        @Override
        public void onRewardVideoAutoLoadFail(String placementId, AdError adError) {
            Log.i(TAG,"PlacementId:" + placementId + ": onRewardVideoAutoLoadFail:\n" + adError.getFullErrorInfo());
            ViewUtil.printLog(tvShowLog,"PlacementId:" + placementId + ": onRewardVideoAutoLoadFail:\n" + adError.getFullErrorInfo());
        }
    };

    private void initPlacementIdLocalExtra(String placementId) {
        String userid = "test_userid_001";
        String userdata = "test_userdata_001_" + placementId + "_" + System.currentTimeMillis();
        Map<String, Object> localMap = new HashMap<>();
        localMap.put(HBAdConst.KEY.USER_ID, userid);
        localMap.put(HBAdConst.KEY.USER_CUSTOM_DATA, userdata);
        Log.i(TAG,"Set PlacementId:" + placementId + ": UserId:" + userid + "| userdata:" + userdata);
        HBRewardVideoAutoAd.setLocalExtra(placementId, localMap);
    }

    HBRewardVideoAutoEventListener autoEventListener = new HBRewardVideoAutoEventListener() {


        @Override
        public void onRewardedVideoAdPlayStart(HBAdInfo hbAdInfo) {

        }

        @Override
        public void onRewardedVideoAdPlayEnd(HBAdInfo hbAdInfo) {

        }

        @Override
        public void onRewardedVideoAdPlayFailed(AdError adError, HBAdInfo hbAdInfo) {

        }

        @Override
        public void onRewardedVideoAdClosed(HBAdInfo hbAdInfo) {

        }

        @Override
        public void onRewardedVideoAdPlayClicked(HBAdInfo hbAdInfo) {

        }

        @Override
        public void onReward(HBAdInfo hbAdInfo) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoLoadListener != null) {
            autoLoadListener = null;
        }
        for (Map.Entry<String, Boolean> entry : mAutoLoadPlacementIdMap.entrySet()) {
            HBRewardVideoAutoAd.removePlacementId(entry.getKey());
        }
    }


}

