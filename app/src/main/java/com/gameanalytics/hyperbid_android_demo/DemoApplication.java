/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 *
 */

package com.gameanalytics.hyperbid_android_demo;

import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import androidx.multidex.MultiDexApplication;

import com.hyperbid.core.api.HBSDK;
import com.hyperbid.core.api.NetTrafficeCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.hyperbid.core.api.DeviceDataInfo;


public class DemoApplication extends MultiDexApplication {
    public static final String appid = "a5aa1f9deda26d";
    public static final String appKey = "4f7b9ac17decb9babec83aac078742c7";
    //Native
    public static final String mPlacementId_native_all = "b5aa1fa2cae775";
    public static final String mPlacementId_native_facebook = "b5aa1fa4165ea3";
    public static final String mPlacementId_native_banner_facebook = "b5ee0cb54b17f8";
    public static final String mPlacementId_native_admob = "b5aa1fa501d9f6";
    public static final String mPlacementId_native_inmobi = "b5aa1fa5d10190";
    public static final String mPlacementId_native_applovin = "b5aa1fa7956158";
    public static final String mPlacementId_native_mintegral = "b5aa1fa85b86d5";
    public static final String mPLacementId_native_automatic_rending_mintegral= "b5ee8aeb8f3458";
    public static final String mPlacementId_native_mopub = "b5ab858fb0175f";
    public static final String mPlacementId_native_appnext = "b5bc7f369610cd";
    public static final String mPlacementId_native_nend = "b5cb95ead9e60a";
    public static final String mPlacementId_native_googleAdManager = "b5f1ea92c46353";
    public static final String mPlacementId_native_myoffer = "b5f33a12982b7f";
    public static final String mPlacementId_native_huawei = "b5f35311178efe";
    public static final String mPlacementId_native_adx = "b5fdc16beb3bb4";
    public static final String mPlacementId_native_online = "b5fdc197e4da39";
    public static final String mPlacementId_native_toutiao = "b5c2c97629da0d";
    public static final String mPlacementId_native_toutiao_drawer = "b5c355d79ef9be";

    //RewardedVideo
    public static final String mPlacementId_rewardvideo_all = "b5b449fb3d89d7";
    public static final String mPlacementId_rewardvideo_facebook = "b5b449eefcab50";
    public static final String mPlacementId_rewardvideo_admob = "b5b449f025ec7c";
    public static final String mPlacementId_rewardvideo_inmobi = "b5b449f0c6b84a";
    public static final String mPlacementId_rewardvideo_applovin = "b5b449f20155a7";
    public static final String mPlacementId_rewardvideo_mintegral = "b5b449f2f58cd7";
    public static final String mPlacementId_rewardvideo_mopub = "b5b449f4927359";
    public static final String mPlacementId_rewardvideo_CHARTBOOST = "b5b449f548e010";
    public static final String mPlacementId_rewardvideo_TAPJOY = "b5b449f66ceaf5";
    public static final String mPlacementId_rewardvideo_IRONSOURCE = "b5b449f75948c5";
    public static final String mPlacementId_rewardvideo_UNITYAD = "b5b449f809139c";
    public static final String mPlacementId_rewardvideo_vungle = "b5b449f97e0b5f";
    public static final String mPlacementId_rewardvideo_adcolony = "b5b449faa95391";
    public static final String mPlacementId_rewardvideo_appnext = "b5bc7f38df0a73";
    public static final String mPlacementId_rewardvideo_nend = "b5cb95efa0c793";
    public static final String mPlacementId_rewardvideo_maio = "b5cb961e495a18";
    public static final String mPlacementId_rewardvideo_startapp = "b5cff0d063ac32";
    public static final String mPlacementId_rewardvideo_myoffer = "b5db6c3764aea3";
    public static final String mPlacementId_rewardvideo_ogury = "b5dde267f73eb4";
    public static final String mPlacementId_rewardvideo_fyber = "b5e96f5e1ade5b";
    public static final String mPlacementId_rewardvideo_googleAdManager = "b5f1ea95c4594e";
    public static final String mPlacementId_rewardvideo_huawei = "b5f353142ed321";
    public static final String mPlacementId_rewardvideo_adx = "b5fdc16c943b7f";
    public static final String mPlacementId_rewardvideo_online = "b5fdc1987c5516";
    public static final String mPlacementId_rewardvideo_kidoz = "b5fe1a15f35ed8";
    public static final String mPlacementId_rewardvideo_mytarget = "b5fe44e9ddde50";
    public static final String mPlacementId_rewardvideo_toutiao = "b5b728e7a08cd4";

    //Banner
    public static final String mPlacementId_banner_all = "b5baca4f74c3d8";
    public static final String mPlacementId_banner_facebook = "b5bbdc51a35e29";
    public static final String mPlacementId_banner_admob = "b5baca41a2536f";
    public static final String mPlacementId_banner_inmobi = "b5bbdc535a9d1a";
    public static final String mPlacementId_banner_applovin = "b5bbdc59f88520";
    public static final String mPlacementId_banner_mintegral = "b5dd388839bf5e";
    public static final String mPlacementId_banner_mopub = "b5bbdc5c857b2f";
    public static final String mPlacementId_banner_appnext = "b5bc7f3b034a2b";
    public static final String mPlacementId_banner_nend = "b5cb95ed13203c";
    public static final String mPlacementId_banner_fyber = "b5e96f5f2dc516";
    public static final String mPlacementId_banner_startapp = "b5ed47d37934a4";
    public static final String mPlacementId_banner_vungle = "b5ee8ae48f1578";
    public static final String mPlacementId_banner_adcolony = "b5ee8ae62b2f80";
    public static final String mPlacementId_banner_chartboost = "b5ee8ae6f9f5cf";
    public static final String mPlacementId_banner_googleAdManager = "b5f1ea93f1793b";
    public static final String mPlacementId_banner_myoffer = "b5f33a1409b96b";
    public static final String mPlacementId_banner_huawei = "b5f35312714b7f";
    public static final String mPlacementId_banner_unityads = "b5f7fd2c9a50a8";
    public static final String mPLacementId_banner_adx = "b5fdc16d5a63fc";
    public static final String mPLacementId_banner_online = "b5fdc199071597";
    public static final String mPLacementId_banner_kidoz = "b5fe1a185c873d";
    public static final String mPLacementId_banner_mytarget = "b5fe46855125b2";
    public static final String mPlacementId_banner_toutiao = "b5baca45138428";

    //Interstitial
    public static final String mPlacementId_interstitial_all = "b5baca53984692";
    public static final String mPlacementId_interstitial_facebook = "b5bbdc69a21187";
    public static final String mPlacementId_interstitial_admob = "b5baca54674522";
    public static final String mPlacementId_interstitial_inmobi = "b5bbdc6b63458f";
    public static final String mPlacementId_interstitial_applovin = "b5bbdc6fc65dd1";
    public static final String mPlacementId_interstitial_mintegral = "b5bbdc725768fa";
    public static final String mPlacementId_interstitial_video_mintegral = "b5bbdc855a1506";
    public static final String mPlacementId_interstitial_mopub = "b5bbdc86dd8e3b";
    public static final String mPlacementId_interstitial_CHARTBOOST = "b5bbdc8a68d901";
    public static final String mPlacementId_interstitial_TAPJOY = "b5bbdc8b6e9829";
    public static final String mPlacementId_interstitial_IRONSOURCE = "b5bbdc8e9ef916";
    public static final String mPlacementId_interstitial_vungle = "b5bbdc9182f9f2";
    public static final String mPlacementId_interstitial_adcolony = "b5bbdc92f49ce7";
    public static final String mPlacementId_interstitial_appnext = "b5bc7f3ec5b952";
    public static final String mPlacementId_interstitial_UNITYAD = "b5c21a303c25e0";
    public static final String mPlacementId_interstitial_nend = "b5cb95eeb7e908";
    public static final String mPlacementId_interstitia_maio = "b5cb961d9d3414";
    public static final String mPlacementId_interstitia_startapp = "b5d5e641d9c30a";
    public static final String mPlacementId_interstitial_myoffer = "b5db6c39aed9c5";
    public static final String mPlacementId_interstitial_ogury = "b5dde269060938";
    public static final String mPlacementId_interstitial_fyber = "b5e96f607235f6";
    public static final String mPlacementId_interstitial_googleAdManager = "b5f1ea94f36790";
    public static final String mPlacementId_interstitial_huawei = "b5f3531369a785";
    public static final String mPlacementId_interstitial_adx = "b5fdc16e15a78b";
    public static final String mPlacementId_interstitial_online = "b5fdc199aa4cc8";
    public static final String mPlacementId_interstitial_kidoz = "b5fe1a17660acb";
    public static final String mPlacementId_interstitial_mytarget = "b5fe46832e1023";
    public static final String mPlacementId_interstitial_toutiao = "b5baca585a8fef";
    public static final String mPlacementId_interstitial_toutiao_video = "b5baca599c7c61";

    //Splash
    public static final String mPlacementId_splash_admob = "b5f73fe0c5db29";
    public static final String mPlacementId_splash_mintegral = "b5ee8ae8611366";
    public static final String mPlacementId_splash_myoffer = "b5f33a1598fe94";
    public static final String mPlacementId_splash_huawei = "b5f3531509b722";
    public static final String mPlacementId_splash_adx = "b5fdc16eb9e2ee";
    public static final String mPlacementId_splash_online = "b5fdc19a532489";
    public static final String mPlacementId_splash_toutiao = "b5bea7c1b653ef";

    @Override
    public void onCreate() {
        super.onCreate();

//        JacocoHelper.Builder builder = new JacocoHelper.Builder();
//        builder.setApplication(this).setDebuggable(true);
//        JacocoHelper.initialize(builder.build());

        //Android 9 or above must be set
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }

        HBSDK.setLogDebug(true);
        HBSDK.checkInit(this);

//        HBSDK.deniedDeviceData(
//                DeviceDataInfo.DEVICE_SCREEN_SIZE
//                , DeviceDataInfo.ANDROID_ID
//                , DeviceDataInfo.APP_PACKAGE_NAME
//                , DeviceDataInfo.APP_VERSION_CODE
//                , DeviceDataInfo.APP_VERSION_NAME
//                , DeviceDataInfo.BRAND
//                , DeviceDataInfo.GAID
//                , DeviceDataInfo.LANGUAGE
//                , DeviceDataInfo.MCC
//                , DeviceDataInfo.MNC
//                , DeviceDataInfo.MODEL
//                , DeviceDataInfo.ORIENTATION
//                , DeviceDataInfo.OS_VERSION_CODE
//                , DeviceDataInfo.OS_VERSION_NAME
//                , DeviceDataInfo.TIMEZONE
//                , DeviceDataInfo.USER_AGENT
//                , DeviceDataInfo.NETWORK_TYPE
//                , DeviceDataInfo.INSTALLER
//
//        );

        HBSDK.checkTraffic(this, new NetTrafficeCallback() {

            @Override
            public void onResultCallback(boolean isEU) {
                if (isEU && HBSDK.getGDPRLevel(DemoApplication.this) == HBSDK.UNKNOWN) {
                    HBSDK.showGDPRPage(DemoApplication.this);
                }

                Log.i("Demoapplication", "onResultCallback:" + isEU);
            }

            @Override
            public void onErrorCallback(String errorMsg) {
                Log.i("Demoapplication", "onErrorCallback:" + errorMsg);
            }
        });

        List excludelist = new ArrayList();
        excludelist.add("com.exclude.myoffer1");
        excludelist.add("com.exclude.myoffer2");
        HBSDK.setExcludeOfferPkgs(excludelist);

        Log.i("Demoapplication", "isChinaSDK:" + HBSDK.isCnSDK());
        Log.i("Demoapplication", "SDKVersionName:" + HBSDK.getVersion());

        Map<String, Object> custommap = new HashMap<String, Object>();
        custommap.put("key1","initCustomMap1");
        custommap.put("key2","initCustomMap2");
        HBSDK.setAppCustomData(custommap);

        Map<String, Object> subcustommap = new HashMap<String, Object>();
        subcustommap.put("key1","initPlacementCustomMap1");
        subcustommap.put("key2","initPlacementCustomMap2");
        HBSDK.setPlacementCustomData("b5aa1fa4165ea3",subcustommap);//native  facebook


        HBSDK.setAppChannel("testChannle_-.123");
        HBSDK.setAppSubChannel("testSubChannle_-.123");


        HBSDK.start(DemoApplication.this, appid, appKey);


    }


}
