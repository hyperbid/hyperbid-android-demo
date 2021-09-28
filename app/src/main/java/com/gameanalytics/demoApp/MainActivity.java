/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */


package com.gameanalytics.demoApp;

import static android.app.Application.getProcessName;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hyperbid.core.api.HBSDK;
import com.hyperbid.core.api.NetTrafficeCallback;

public class MainActivity extends AppCompatActivity {

    static final String _tag    = "hyperbid_main_activity";
    static final String _appid  = "a60948274823ff";
    static final String _appKey = "dcd2834dba9ffd7d654cc8859174070e";

    static final String _appChannel     = "testChannel";
    static final String _appSubChannel  = "testSubChannel";

    static final int BANNER_AD_ID          = 0;
    static final int INTERSTITIAL_AD_ID    = 1;
    static final int REWARDED_VIDEO_ID     = 2;
    static final int SPLASH_AD_ID          = 3;
    static final int NATIVE_AD_ID          = 4;
    static final int NATIVE_BANNER_ID      = 5;
    static final int ALL_ENTRIES           = 6;

    static final String[] _names = new String[] {
            "Banner Ad Sample",
            "Interstitial Ad",
            "Rewarded Video",
            "Splash Ad",
            "Native Ad",
            "Native Banner",
    };

    static final Class[] _activities = new Class[] {
            BannerAdActivity.class,
            InterstitialAdActivity.class,
            RewardVideoAdActivity.class,
            SplashAdActivity.class,
            NativeAdActivity.class,
            NativeBannerActivity.class,
    };

    boolean _hasHyperBidInitialized = false; // true if the sdk was initialized
    boolean _hasGdprBeenAccepted    = false; // true if the gdpr check has been done

    protected void setDataDirectory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    /*
    * for EU users it is necessary to handle GDPR
    * HyperBid provides functions to facilitate this
     */
    protected void askGdpr() {
        HBSDK.checkTraffic(this, new NetTrafficeCallback() {
            @Override
            public void onResultCallback(boolean isEU) {
                if (isEU && HBSDK.getGDPRLevel(MainActivity.this) == HBSDK.UNKNOWN) {
                    // displays the GDPR prompt that the users needs to agree to -> either personalized or non-personalized
                    HBSDK.showGDPRPage(MainActivity.this);
                }

                _hasGdprBeenAccepted = true;
                Log.i(_tag, "onResultCallback:" + isEU);
            }

            @Override
            public void onErrorCallback(String errorMsg) {
                Log.i(_tag, "onErrorCallback:" + errorMsg);
                _hasGdprBeenAccepted = false;
            }
        });
    }

    //  converts the gdpr policy to a string that can be shown in the app
    protected String getGDPRString(int code) {
        switch(code) {
            case HBSDK.PERSONALIZED:
                return "personalized";
            case HBSDK.NONPERSONALIZED:
                return "non-personalized";
            default:
                return "unknown";
        }
    }

    // creates a dialog with extra information on the current HyberBid implementation in the app
    protected void showDetailsDialog() {
        String message = new String();
        message += "HyperBid Status: " + (_hasHyperBidInitialized ? "Active" : "Inactive") + '\n';
        message += "HyperBid Version: " + HBSDK.getVersion() + '\n';
        message += "App Id: "  + _appid + '\n';
        message += "App Key: " + _appKey + '\n';
        message += "Is China Build: " + HBSDK.isCnSDK() + '\n';
        message += "GDPR policy: " + getGDPRString(HBSDK.getGDPRLevel(this)) + '\n';
        message += "Is EU user: " + HBSDK.isEUTraffic(this) + '\n';
        message += "Debug log enabled: " + HBSDK.isLogDebug();

        new AlertDialog.Builder(this)
                .setTitle("Details")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    // handles HyperBid initializations including GDPR for EU users
    protected void initializeHyperbid() {
        setDataDirectory();
        HBSDK.setLogDebug(true);
        HBSDK.checkInit(this);

        askGdpr();

        HBSDK.setAppChannel(_appChannel);
        HBSDK.setAppSubChannel(_appSubChannel);

        // in order to initialize HyperBid it is required to use the id & key from the HyperBid dashboard
        HBSDK.start(getApplicationContext(), _appid, _appKey);

        _hasHyperBidInitialized = true;
    }

    // creates a menu with all the samples for HyperBid
    protected void createEntries() {
        SampleListAdapter listAdapter = new SampleListAdapter(this, _names);

        for (int i = 0; i < ALL_ENTRIES; ++i)
            listAdapter.addEntry(new SampleListAdapter.Entry(_names[i], null));

        ListView sampleList = (ListView) findViewById(R.id.sample_list);
        sampleList.setAdapter(listAdapter);

        sampleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i < ALL_ENTRIES)
                {
                    Log.d(_tag, "Starting activity for:" + _names[i]);
                    startActivity(new Intent(MainActivity.this, _activities[i]));
                }
                else
                {
                    final String errMessage = "Invalid entry id:" + i;
                    Toast.makeText(null, errMessage, Toast.LENGTH_SHORT).show();
                    Log.e(_tag, errMessage);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button initializeBtn = (Button) findViewById(R.id.initialize_btn);
        initializeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_hasHyperBidInitialized) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.already_init_title)
                            .setMessage(R.string.already_init_message)
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                    return;
                }

                createEntries();
                initializeHyperbid();
            }
        });

        Button detailsBtn = (Button) findViewById(R.id.details_btn);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailsDialog();
            }
        });
    }
}