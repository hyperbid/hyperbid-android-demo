/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.HyperBid.demoApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HyperBid.demoApp.Adapter.NativeVideoButtonAdapter;
import com.HyperBid.demoApp.Util.PlacementIdUtil;
import com.HyperBid.demoApp.Util.ViewUtil;
import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.nativead.api.HBNative;
import com.hyperbid.nativead.api.HBNativeAdView;
import com.hyperbid.nativead.api.HBNativeEventExListener;
import com.hyperbid.nativead.api.HBNativeNetworkListener;
import com.hyperbid.nativead.api.NativeAd;
import com.hyperbid.nativead.unitgroup.api.CustomNativeAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeAdActivity extends Activity {

    private static final String TAG = NativeAdActivity.class.getSimpleName();

    HBNative mATNative;
    HBNativeAdView hyperbidNativeAdView;
    NativeAd mNativeAd;

    ImageView mCloseView;

    int mCurrentSelectIndex;


//    int mCurrentSelectIndex;
//
//    CheckBox mDownloadConfimCheckBox;

    private Spinner mSpinner;
    private ViewGroup mAdContainer;

    private RelativeLayout rlNative;
    private RelativeLayout rlDraw;
    private RelativeLayout rlPatch;

    private TextView tvLoadAdBtn;
    private TextView tvIsAdReadyBtn;
    private TextView tvShowAdBtn;
    private TextView tvShowLog;

    private RecyclerView rvButtonList;

    int mType;
    final int TYPE_NATIVE = 0;
    final int TYPE_NATIVE_LIST = 1;
    private View mPanel;

    private Map<String, String> mPlacementIdMap;
    private String mCurrentNetworkName;
    private List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_native);

        tvShowLog = findViewById(R.id.tv_show_log);
        tvShowLog.setMovementMethod(ScrollingMovementMethod.getInstance());

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.hyperbid_title_native);
        titleBar.setListener(new TitleBarClickListener() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });

        rlNative = findViewById(R.id.rl_native);
        rlDraw = findViewById(R.id.rl_draw);
        rlPatch = findViewById(R.id.rl_patch);
        tvLoadAdBtn = findViewById(R.id.load_ad_btn);
        tvIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn);
        tvShowAdBtn = findViewById(R.id.show_ad_btn);

        initTypeSpinner();

        mPlacementIdMap = PlacementIdUtil.getNativePlacements(this);
        List<String> placementNameList = new ArrayList<>(mPlacementIdMap.keySet());


        mSpinner = (Spinner) findViewById(R.id.spinner_1);
//        mDownloadConfimCheckBox = findViewById(R.id.download_listener_check);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                NativeAdActivity.this, android.R.layout.simple_spinner_dropdown_item,
                placementNameList);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mCurrentNetworkName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), mCurrentNetworkName, Toast.LENGTH_SHORT).show();

                HBNative.entryAdScenario(mPlacementIdMap.get(mCurrentNetworkName), "");

//                if (TextUtils.equals(networkName, "GDT")) {
//                    mDownloadConfimCheckBox.setVisibility(View.VISIBLE);
//                } else {
//                    mDownloadConfimCheckBox.setVisibility(View.GONE);
//                }
                init(mPlacementIdMap.get(mCurrentNetworkName));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mCurrentNetworkName = placementNameList.get(0);
        init(mPlacementIdMap.get(mCurrentNetworkName));

        int padding = dip2px(10);
        final int containerHeight = dip2px(340);
        final int adViewWidth = getResources().getDisplayMetrics().widthPixels - 2 * padding;
        final int adViewHeight = containerHeight - 2 * padding;

        initCloseView();
        final NativeDemoRender hyperbidRender = new NativeDemoRender(this);
        hyperbidRender.setCloseView(mCloseView);

        tvLoadAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAd(adViewWidth, adViewHeight);
            }
        });

        tvIsAdReadyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdReady();
            }
        });

        tvShowAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mType == TYPE_NATIVE) {

                    showAd(mAdContainer, adViewWidth, adViewHeight, hyperbidRender);

                } else if (mType == TYPE_NATIVE_LIST) {

                    Intent intent = new Intent(NativeAdActivity.this, NativeListActivity.class);
                    intent.putExtra("placementId", mPlacementIdMap.get(mCurrentNetworkName));

                    startActivity(intent);
                }
            }
        });

//        findViewById(R.id.iv_close_panel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mData.clear();
//                destroyAd();
//                mPanel.setVisibility(View.GONE);
//            }
//        });

        initPanel();
    }

    private void initTypeSpinner() {
        Spinner typeSpinner = (Spinner) findViewById(R.id.spinner_2);
        typeSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NativeAdActivity.this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        "Native",
                        "Native List"
                });
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String typeName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(NativeAdActivity.this, typeName, Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0:
                        mType = TYPE_NATIVE;
                        break;
                    case 1:
                        mType = TYPE_NATIVE_LIST;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void init(String placementId) {
        mATNative = new  HBNative(this, placementId, new  HBNativeNetworkListener() {
            @Override
            public void onNativeAdLoaded() {
                Log.i(TAG, "onNativeAdLoaded");
                ViewUtil.printLog(tvShowLog,"load success...");
            }

            @Override
            public void onNativeAdLoadFail(AdError adError) {
                Log.i(TAG, "onNativeAdLoadFail, " + adError.getFullErrorInfo());
                ViewUtil.printLog(tvShowLog,"load fail...：" + adError.getFullErrorInfo());
            }
        });
    }

    private void loadAd(int adViewWidth, int adViewHeight) {
        Map<String, Object> localMap = new HashMap<>();

        // since v5.6.4
        localMap.put(HBAdConst.KEY.AD_WIDTH, adViewWidth);
        localMap.put(HBAdConst.KEY.AD_HEIGHT, adViewHeight);


        mATNative.setLocalExtra(localMap);

        mATNative.loadAd();
    }

    private void isAdReady() {
        boolean isReady = mATNative.getReadyAdInfo().isReady();
        Log.i(TAG, "isAdReady: " + isReady);
        ViewUtil.printLog(tvShowLog,"isAdReady：" + isReady);
    }

    private void showAd(ViewGroup adContainer, int adViewWidth, int adViewHeight, NativeDemoRender hyperbidRender) {
        if (adContainer == null) {
            Log.e(TAG, "showAd: adContainer = null");
            return;
        }

        NativeAd nativeAd = mATNative.getAd();
        if (nativeAd != null) {

            if (mNativeAd != null) {
                mNativeAd.destory();
            }
            mNativeAd = nativeAd;
            mNativeAd.setManualImpressionTrack(true);

            mNativeAd.setNativeEventListener(new HBNativeEventExListener() {
                @Override
                public void onDeeplinkCallback(HBNativeAdView view, HBAdInfo adInfo, boolean isSuccess) {
                    Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
                }

                @Override
                public void onAdImpressed(HBNativeAdView view, HBAdInfo entity) {
                    Log.i(TAG, "native ad onAdImpressed:\n" + entity.toString());
                    ViewUtil.printLog(tvShowLog,"onAdImpressed");
                }

                @Override
                public void onAdClicked(HBNativeAdView view, HBAdInfo entity) {
                    Log.i(TAG, "native ad onAdClicked:\n" + entity.toString());
                    ViewUtil.printLog(tvShowLog,"onAdClicked");
                }

                @Override
                public void onAdVideoStart(HBNativeAdView view) {
                    Log.i(TAG, "native ad onAdVideoStart");
                    ViewUtil.printLog(tvShowLog,"onAdVideoStart");
                }

                @Override
                public void onAdVideoEnd(HBNativeAdView view) {
                    Log.i(TAG, "native ad onAdVideoEnd");
                    ViewUtil.printLog(tvShowLog,"onAdVideoEnd");
                }

                @Override
                public void onAdVideoProgress(HBNativeAdView view, int progress) {
                    Log.i(TAG, "native ad onAdVideoProgress:" + progress);
                    ViewUtil.printLog(tvShowLog,"onAdVideoProgress");
                }
            });


            if (hyperbidNativeAdView == null) {
                hyperbidNativeAdView = new HBNativeAdView(this);
            } else {
                hyperbidNativeAdView.removeAllViews();
            }
            if (hyperbidNativeAdView.getParent() == null) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(adViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                adContainer.addView(hyperbidNativeAdView, layoutParams);
            }

            hyperbidRender.setAdWidth(adViewWidth);
            try {
                mNativeAd.renderAdView(hyperbidNativeAdView, hyperbidRender);
            } catch (Exception e) {

            }

            hyperbidNativeAdView.addView(mCloseView);

            hyperbidNativeAdView.setVisibility(View.VISIBLE);
            mNativeAd.prepare(hyperbidNativeAdView, hyperbidRender.getClickView(), null);
            mNativeAd.manualImpressionTrack();

            mPanel.setVisibility(View.VISIBLE);
            initPanelButtonList(hyperbidRender.adType);
        } else {
            ViewUtil.printLog(tvShowLog,"this placement no cache!");
        }
    }

    private void initCloseView() {
        if (mCloseView == null) {
            mCloseView = new ImageView(this);
            mCloseView.setImageResource(R.mipmap.ad_close);

            int padding = dip2px(5);
            mCloseView.setPadding(padding, padding, padding, padding);

            int size = dip2px(30);
            int margin = dip2px(2);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
            layoutParams.topMargin = margin;
            layoutParams.rightMargin = margin;
            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

            mCloseView.setLayoutParams(layoutParams);
        }
    }

//    public void changeBg(View view,boolean selected) {
//        view.setBackgroundResource(selected ? R.drawable.bg_white_selected : R.drawable.bg_white);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAd();
    }

    private void destroyAd() {
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

    public int dip2px(float dipValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initPanel() {
        mPanel = findViewById(R.id.rl_panel);
        mAdContainer = findViewById(R.id.ad_container);
        rvButtonList = findViewById(R.id.rv_button);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvButtonList.setLayoutManager(manager);

        final boolean isMute[] = new boolean[]{true};
        NativeVideoButtonAdapter adapter = new NativeVideoButtonAdapter(mData, new NativeVideoButtonAdapter.OnNativeVideoButtonCallback() {
            @Override
            public void onClick(String action) {
                if (action == VideoAction.VOICE_CHANGE) {
                    if (mNativeAd != null) {
                        mNativeAd.setVideoMute(!isMute[0]);
                        isMute[0] = !isMute[0];
                    }
                } else if (action == VideoAction.VIDEO_RESUME) {
                    if (mNativeAd != null) {
                        mNativeAd.resumeVideo();
                    }
                } else if (action == VideoAction.VIDEO_PAUSE) {
                    if (mNativeAd != null) {
                        mNativeAd.pauseVideo();
                    }
                } else if (action == VideoAction.VIDEO_PROGRESS) {
                    if (mNativeAd != null) {
                        String tips = "video duration: " + mNativeAd.getVideoDuration() + ", progress: " + mNativeAd.getVideoProgress();
                        Log.i(TAG, tips);
                        Toast.makeText(NativeAdActivity.this, tips, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        rvButtonList.setAdapter(adapter);
    }

    private void initPanelButtonList(String adType) {
        if (adType == CustomNativeAd.NativeAdConst.VIDEO_TYPE) {
            boolean isNativeExpress = true;
            if (mNativeAd != null) {
                isNativeExpress = mNativeAd.isNativeExpress();
            }

            if (isNativeExpress) {
                return;
            }

            HBAdInfo atAdInfo = mNativeAd.getAdInfo();
            int networkId = atAdInfo.getNetworkFirmId();

            switch (networkId) {
                case 8:
                    //for GDT
                    mData.add(VideoAction.VOICE_CHANGE);
                    mData.add(VideoAction.VIDEO_RESUME);
                    mData.add(VideoAction.VIDEO_PAUSE);
                    mData.add(VideoAction.VIDEO_PROGRESS);
                    break;
//                case 15:
//                    //for CSJ
//                    mData.add(VideoAction.VOICE_CHANGE);
//                    break;
                case 22:
                    //for BaiDu
                    mData.add(VideoAction.VIDEO_PROGRESS);
                    break;
                case 28:
                    //for KuaiShou
                    mData.add(VideoAction.VIDEO_PROGRESS);
                    break;
            }
        }
    }

    private void exitNativePanel(){
        mData.clear();
        destroyAd();
        mPanel.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK && mPanel.getVisibility() == View.VISIBLE) {
            exitNativePanel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
