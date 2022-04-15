/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 *
 */

package com.HyperBid.demoApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HyperBid.demoApp.Adapter.NativeListAdapter;
import com.hyperbid.core.api.HBAdConst;
import com.hyperbid.core.api.HBAdInfo;
import com.hyperbid.core.api.AdError;
import com.hyperbid.nativead.api.HBNative;
import com.hyperbid.nativead.api.HBNativeAdRenderer;
import com.hyperbid.nativead.api.HBNativeAdView;
import com.hyperbid.nativead.api.HBNativeDislikeListener;
import com.hyperbid.nativead.api.HBNativeEventListener;
import com.hyperbid.nativead.api.HBNativeNetworkListener;
import com.hyperbid.nativead.api.NativeAd;
import com.hyperbid.nativead.unitgroup.api.CustomNativeAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeListActivity extends Activity {

    public static final String TAG = NativeListActivity.class.getSimpleName();

    private RecyclerView rvNative;
    private NativeListAdapter mAdapter;
    private int mPage = -1;
    private final int mDataCountInPerPage = 20;
    private final int mCheckLoadItemInterval = 2;
    private boolean isLoadSuccessful;
    private int firstCompletelyVisibleItemPosition = -1;
    private int lastCompletelyVisibleItemPosition = -1;

    private int adViewWidth;
    private int adViewHeight;
    private HBNative mHBNative;
    private boolean isLoadingAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_list);

        adViewWidth = getResources().getDisplayMetrics().widthPixels;
        adViewHeight = dip2px(340);
        initRv();

        checkAndLoadAd();
    }

    public int dip2px(float dipValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initRv() {
        final List<String> data = createMockData();

        rvNative = findViewById(R.id.rv_native);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(NativeListActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNative.setLayoutManager(layoutManager);
        mAdapter = new NativeListAdapter(adViewWidth, adViewHeight, data, new NativeListAdapter.OnNativeListCallback() {
            @Override
            public HBNativeAdView onBindAdView(NativeAd nativeAd, HBNativeAdView atNativeAdView,  HBNativeAdRenderer<? extends CustomNativeAd> atNativeAdRenderer) {
                return fetchAd(nativeAd, atNativeAdView, atNativeAdRenderer);
            }

            @Override
            public void onClickLoadMore() {
                List<String> mockData = createMockData();
                mAdapter.addData(mockData);
            }
        });
        rvNative.setAdapter(mAdapter);

        rvNative.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if (isLoadingAd) {
                    return;
                }

                if (lastCompletelyVisibleItemPosition % mAdapter.getIntervalAd() == mCheckLoadItemInterval
                        || layoutManager.findFirstVisibleItemPosition() % mAdapter.getIntervalAd() == mCheckLoadItemInterval) {
//                    checkAndLoadAd();
                }

            }
        });
    }

    private List<String> createMockData() {
        mPage++;

        List<String> data = new ArrayList<>();
        for (int i = 0; i < mDataCountInPerPage; i++) {
            data.add("Data: " + ((mPage * mDataCountInPerPage) + i));
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        if (mAdapter != null) {
            mAdapter.onDestroy();
            mAdapter = null;
        }

        if (rvNative != null) {
            rvNative.setAdapter(null);
            rvNative = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mAdapter != null) {
            mAdapter.onPause(firstCompletelyVisibleItemPosition, lastCompletelyVisibleItemPosition);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAdapter != null) {
            mAdapter.onResume(firstCompletelyVisibleItemPosition, lastCompletelyVisibleItemPosition);
        }
        super.onResume();
    }

    // ----------------------------------------------------------------------------------------

    public void checkAndLoadAd() {

        if (isLoadingAd) {
            return;
        }

        if (mHBNative == null) {
            Log.e(TAG, "checkAndLoadAd: no ad obj, need to load ad");
            loadAd();
            return;
        }

        NativeAd nativeAd = mHBNative.getAd();
        if (nativeAd == null) {
            Log.e(TAG, "checkAndLoadAd: no cache, need to load ad");
            loadAd();

        } else {
            addLoadedCache(nativeAd);
            Log.e(TAG, "checkAndLoadAd:  has cache");
        }
    }


    private void loadAd() {
        if (mHBNative == null) {
            mHBNative = new HBNative(this, PlacementId.BANNER_AD_ID, new HBNativeNetworkListener() {
                @Override
                public void onNativeAdLoaded() {
                    isLoadingAd = false;
                    Log.i(TAG, "native ad onNativeAdLoaded------------- ");

                    NativeAd nativeAd = mHBNative.getAd();
                    addLoadedCache(nativeAd);

                    if (!isLoadSuccessful) {
                        isLoadSuccessful = true;
                        findViewById(R.id.rv_native).setVisibility(View.VISIBLE);
                        findViewById(R.id.pb).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNativeAdLoadFail(AdError adError) {
                    isLoadingAd = false;
                    Log.e(TAG, "native ad onNativeAdLoadFail------------- " + adError.getFullErrorInfo());
                }
            });
        }

        Map<String, Object> localMap = new HashMap<>();
        localMap.put(HBAdConst.KEY.AD_WIDTH, adViewWidth);
        localMap.put(HBAdConst.KEY.AD_HEIGHT, adViewHeight);
        mHBNative.setLocalExtra(localMap);

        //load ad
        mHBNative.loadAd();
        isLoadingAd = true;

        Log.i(TAG, "native ad start to load ad------------- ");

    }

    private void addLoadedCache(NativeAd nativeAd) {
        if (mHBNative != null) {
            if (mAdapter != null && nativeAd != null) {
                mAdapter.addCache(nativeAd);
            }
        }
    }

    private HBNativeAdView fetchAd(NativeAd nativeAd, HBNativeAdView atNativeAdView,  HBNativeAdRenderer<? extends CustomNativeAd> atNativeAdRenderer) {

        if (nativeAd != null) {
            Log.i(TAG, "fetchAd: startRenderAd");
            renderAd(nativeAd, atNativeAdView, atNativeAdRenderer);

            return atNativeAdView;
        }
        return null;
    }

    private void renderAd(final NativeAd nativeAd, final HBNativeAdView atNativeAdView,  HBNativeAdRenderer<? extends CustomNativeAd> atNativeAdRenderer) {
        nativeAd.setNativeEventListener(new HBNativeEventListener() {
            @Override
            public void onAdImpressed(HBNativeAdView view, HBAdInfo entity) {
                Log.i(TAG, "native ad onAdImpressed--------\n" + entity.toString());

                checkAndLoadAd();
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

        nativeAd.setDislikeCallbackListener(new HBNativeDislikeListener() {
            @Override
            public void onAdCloseButtonClick(HBNativeAdView view, HBAdInfo entity) {
                for (int i = firstCompletelyVisibleItemPosition; i < lastCompletelyVisibleItemPosition; i++) {
                    RecyclerView.ViewHolder viewHolder = rvNative.findViewHolderForAdapterPosition(i);
                    if (viewHolder != null && viewHolder.itemView instanceof HBNativeAdView) {
                        if (atNativeAdView == viewHolder.itemView) {
                            Log.i(TAG, "onAdCloseButtonClick: remove " + i);
                            mAdapter.removeAdView(i);
                            return;
                        }
                    }
                }
            }
        });

        try {
            Log.i(TAG, "native ad start to render ad------------- ");
            nativeAd.renderAdView(atNativeAdView, atNativeAdRenderer);
            nativeAd.prepare(atNativeAdView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
