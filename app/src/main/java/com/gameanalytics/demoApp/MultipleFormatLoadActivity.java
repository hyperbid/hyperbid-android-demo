package com.gameanalytics.demoApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyperbid.banner.api.HBBannerListener;
import com.hyperbid.banner.api.HBBannerView;
import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.interstitial.api.HBInterstitial;
import com.hyperbid.interstitial.api.HBInterstitialListener;
import com.hyperbid.nativead.api.HBNative;
import com.hyperbid.nativead.api.HBNativeAdView;
import com.hyperbid.nativead.api.HBNativeEventListener;
import com.hyperbid.nativead.api.HBNativeNetworkListener;
import com.hyperbid.nativead.api.NativeAd;
import com.hyperbid.rewardvideo.api.HBRewardVideoAd;
import com.hyperbid.rewardvideo.api.HBRewardVideoListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultipleFormatLoadActivity extends Activity {

    private interface TaskEntry
    {
        void execute();
    }

    private static String TAG = MultipleFormatLoadActivity.class.getSimpleName();

    HBRewardVideoAd mRewardVideoAd;
    HBInterstitial mInterstitialAd;
    HBNative mAtNative;
    HBBannerView mAtBannerView;
    private FrameLayout mFrameLayout;
    private HBNativeAdView mHBNativeAdView;
    private NativeDemoRender upArpuRender;

    private String mNativePlacementId = PlacementId.NATIVE_BANNER_ID;
    private String mBannerPlacementId = PlacementId.BANNER_AD_ID;
    private String mRewardVideoPlacementId = PlacementId.REWARDED_VIDEO_ID;
    private String mInterstitialPlacementId = PlacementId.INTERSTITIAL_AD_ID;

    private boolean mNeedLoadRewardVideo = true;
    private boolean mNeedLoadInterstitial = true;
    private boolean mNeedLoadNative = true;
    private boolean mNeedLoadBanner = true;

    private Spinner multiSpinner;

    private ArrayList<TaskEntry> taskEntries = new ArrayList<>(Arrays.asList(
            () -> multiLoad(), // Multi load
            () -> { // Show reward video
                if (mRewardVideoAd != null) {
                    mRewardVideoAd.show(this);
                }
            },
            () -> { // Show interstitial
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(this);
                }
            },
            () -> { // Show native
                mFrameLayout.removeAllViews();
                mFrameLayout.addView(mHBNativeAdView);

                NativeAd nativeAd = mAtNative.getAd();
                if (nativeAd != null) {
                    nativeAd.setNativeEventListener(new HBNativeEventListener() {
                        @Override
                        public void onAdImpressed(HBNativeAdView view, HBAdInfo entity) {
                            Log.i(TAG, "native ad onAdImpressed--------\n" + entity.toString());
                        }

                        @Override
                        public void onAdClicked(HBNativeAdView view, HBAdInfo entity) {
                            Log.i(TAG, "native ad onAdClicked--------\n" + entity.toString());
                        }

                        @Override
                        public void onAdVideoStart(HBNativeAdView view) {
                            Log.i(TAG, "native ad onAdVideoStart--------");
                        }

                        @Override
                        public void onAdVideoEnd(HBNativeAdView view) {
                            Log.i(TAG, "native ad onAdVideoEnd--------");
                        }

                        @Override
                        public void onAdVideoProgress(HBNativeAdView view, int progress) {
                            Log.i(TAG, "native ad onAdVideoProgress--------:" + progress);
                        }
                    });
                    try {
                        nativeAd.renderAdView(mHBNativeAdView, upArpuRender);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(CommonUtil.dip2px(MultipleFormatLoadActivity.this, 20), CommonUtil.dip2px(MultipleFormatLoadActivity.this, 8));
                    layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

                    nativeAd.prepare(mHBNativeAdView, null, layoutParams);
                } else {
                    Log.i(TAG, "onClick: this placement no cache! ");
                    Toast.makeText(MultipleFormatLoadActivity.this, "this placement no cache!", Toast.LENGTH_LONG).show();
                }
            },
            () -> { // Show banner
                mFrameLayout.removeAllViews();
                mFrameLayout.addView(mAtBannerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(getApplicationContext(), 300)));
            },
            () -> mFrameLayout.removeAllViews() // Remove view
    ));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_format_load);

        init();
        initRewardVideo();
        initInterstitial();
        initNative();
        initBanner();

        multiSpinner = findViewById(R.id.multiFormatSpinner);

        findViewById(R.id.multiFormatExecuteButton).setOnClickListener(v -> {
            int itemPosition = multiSpinner.getSelectedItemPosition();
            taskEntries.get(itemPosition).execute();
        });

        if(BuildConfig.DEBUG)
        {
            if(taskEntries.size() != multiSpinner.getCount())
            {
                new AlertDialog.Builder(this).setMessage("Descriptions and spinner count doesn't match").setTitle("Error").setPositiveButton("Close", (dialogInterface, i) -> MultipleFormatLoadActivity.this.finishAffinity()).create().show();
            }
        }
    }

    private void init() {
        mFrameLayout = ((FrameLayout) findViewById(R.id.ad_container));
    }

    private void initRewardVideo() {

        if (!mNeedLoadRewardVideo) {
            return;
        }

        mRewardVideoAd = new HBRewardVideoAd(this, mRewardVideoPlacementId);
        mRewardVideoAd.setAdListener(new HBRewardVideoListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i(TAG, "onRewardedVideoAdLoaded");
            }

            @Override
            public void onRewardedVideoAdFailed(AdError errorCode) {
                Log.i(TAG, "onRewardedVideoAdFailed error:" + errorCode.printStackTrace());
            }

            @Override
            public void onRewardedVideoAdPlayStart(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayStart, \n" + entity.toString());
            }

            @Override
            public void onRewardedVideoAdPlayEnd(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayEnd, \n" + entity.toString());
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError errorCode, HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayFailed error:" + errorCode.printStackTrace()
                        + "\n" + entity.toString());
            }

            @Override
            public void onRewardedVideoAdClosed(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdClosed" + "\n" + entity.toString());
            }

            @Override
            public void onRewardedVideoAdPlayClicked(HBAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayClicked, \n" + entity.toString());
            }

            @Override
            public void onReward(HBAdInfo upArpuAdInfo) {
                Log.i(TAG, "onReward, \n" + upArpuAdInfo.toString());
            }
        });
    }

    private void initInterstitial() {

        if (!mNeedLoadInterstitial) {
            return;
        }

        mInterstitialAd = new HBInterstitial(this, mInterstitialPlacementId);
        mInterstitialAd.setAdListener(new HBInterstitialListener() {
            @Override
            public void onInterstitialAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded");
            }

            @Override
            public void onInterstitialAdLoadFail(AdError adError) {
                Log.i(TAG, "onInterstitialAdLoadFail:" + adError.printStackTrace());
            }

            @Override
            public void onInterstitialAdClicked(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdClicked, \n" + entity.toString());
            }

            @Override
            public void onInterstitialAdShow(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdShow, \n" + entity.toString());
            }

            @Override
            public void onInterstitialAdClose(HBAdInfo entity) {
                Log.i(TAG, "onInterstitialAdClose, \n" + entity.toString());
            }

            @Override
            public void onInterstitialAdVideoStart(HBAdInfo adInfo) {
                Log.i(TAG, "onInterstitialAdVideoStart");
            }

            @Override
            public void onInterstitialAdVideoEnd(HBAdInfo adInfo) {
                Log.i(TAG, "onInterstitialAdVideoEnd");
            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {
                Log.i(TAG, "onInterstitialAdVideoError");
            }
        });
    }

    private void initNative() {

        if (!mNeedLoadNative) {
            return;
        }

        mAtNative = new HBNative(this.getApplicationContext(), mNativePlacementId, new HBNativeNetworkListener() {
            @Override
            public void onNativeAdLoaded() {
                Log.i(TAG, "onNativeAdLoaded: ");
            }

            @Override
            public void onNativeAdLoadFail(AdError adError) {
                Log.i(TAG, "onNativeAdLoadFail: ");
            }
        });

        Map<String, Object> localMap = new HashMap<>();
        localMap.put(HBAdConst.KEY.AD_WIDTH, CommonUtil.dip2px(this, 250));
        localMap.put(HBAdConst.KEY.AD_HEIGHT, CommonUtil.dip2px(this, 170));
        mAtNative.setLocalExtra(localMap);

        mHBNativeAdView = new HBNativeAdView(this);

        upArpuRender = new NativeDemoRender(this);
    }

    private void initBanner() {

        if (!mNeedLoadBanner) {
            return;
        }

        if (mAtBannerView != null) {
            mAtBannerView.destroy();
            mAtBannerView = null;
        }
        mAtBannerView = new HBBannerView(this);
        mAtBannerView.setPlacementId(mBannerPlacementId);
        mAtBannerView.setBackgroundColor(0xffAD4949);
        mAtBannerView.setBannerAdListener(new HBBannerListener() {
            @Override
            public void onBannerLoaded() {
                Log.i("BannerAdActivity", "onBannerLoaded");
            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.i(TAG, "onBannerFailed:" + adError.printStackTrace());
            }

            @Override
            public void onBannerClicked(HBAdInfo entity) {
                Log.i(TAG, "onBannerClicked, \n" + entity.toString());
            }

            @Override
            public void onBannerShow(HBAdInfo entity) {
                Log.i(TAG, "onBannerShow, \n" + entity.toString());
            }

            @Override
            public void onBannerClose(HBAdInfo adInfo) {
                Log.i(TAG, "onBannerClose");
            }

            @Override
            public void onBannerAutoRefreshed(HBAdInfo entity) {
                Log.i(TAG, "onBannerAutoRefreshed, \n" + entity.toString());
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.i(TAG, "onBannerAutoRefreshFail:" + adError.printStackTrace());
            }
        });
    }

    private void multiLoad() {
        if (mNeedLoadRewardVideo) {
            mRewardVideoAd.load();
        }

        if (mNeedLoadInterstitial) {
            mInterstitialAd.load();
        }

        if (mNeedLoadBanner) {
            mAtBannerView.loadAd();
        }

        if (mNeedLoadNative) {
            try {
                mAtNative.loadAd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
