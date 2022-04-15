/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */


package com.HyperBid.demoApp;

import static android.app.Application.getProcessName;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.HyperBid.demoApp.Adapter.MainListAdapter;
import com.hyperbid.core.api.DeviceInfoCallback;
import com.hyperbid.core.api.HBSDK;
import com.hyperbid.core.api.NetTrafficeCallback;

import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends Activity {

    static final String _tag    = "hyperbid_main_activity";
    static final String _appid  = "a5aa1f9deda26d";
    static final String _appKey = "4f7b9ac17decb9babec83aac078742c7";

    static final String _appChannel     = "testChannel";
    static final String _appSubChannel  = "testSubChannel";

    static final int BANNER_AD_ID          = 0;
    static final int INTERSTITIAL_AD_ID    = 1;
    static final int REWARDED_VIDEO_ID     = 2;
    static final int ALL_ENTRIES           = 3;

    static final String[] _names = new String[] {
            "Banner Ad Sample",
            "Interstitial Ad",
            "Rewarded Video",
            //"Splash Ad",
            //"Native Ad",
            //"Native Banner",
    };

    static final Class[] _activities = new Class[] {
            BannerAdActivity.class,
            InterstitialAdActivity.class,
            RewardVideoAdActivity.class,
            //SplashAdActivity.class,
            //NativeAdActivity.class,
            //NativeBannerActivity.class,
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


    private int OVERLAY_PERMISSION_REQ_CODE = 2333;
    WindowManager manager;
    TextView tvWindowInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        alertWindow();
        //test


        MainListAdapter mainListAdapter = new MainListAdapter(this);
        mainListAdapter.setMainListListener(new MainListAdapter.MainListListener() {
            @Override
            public void clickedItem(Integer position) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MainActivity.this, RewardVideoAdActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, InterstitialAdActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SplashAdActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, BannerAdActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, NativeAdActivity.class));
                        break;
                }
            }
        });
        ListView container = findViewById(R.id.container);
        container.setAdapter(mainListAdapter);


        ((TextView)findViewById(R.id.tv_sdk_demo)).getPaint().setFakeBoldText(true);


        ((TextView) findViewById(R.id.tv_version)).setText(getResources().getString(R.string.hyperbid_sdk_version, HBSDK.getVersion()));

       initializeHyperbid();

        HBSDK.testModeDeviceInfo(this, new DeviceInfoCallback() {
            @Override
            public void deviceInfo(String deviceInfo) {
                if (!TextUtils.isEmpty(deviceInfo)) {
                    try {
                        JSONObject jsonObject = new JSONObject(deviceInfo);
                        String androidID = jsonObject.optString("AndroidID");

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView deviceIdTextView = (TextView) findViewById(R.id.tv_device_id);
                                deviceIdTextView.setText(getResources().getString(R.string.hyperbid_click_to_copy_device_id, androidID));
                                deviceIdTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        copyContentToClipboard(MainActivity.this, androidID);

                                        Toast.makeText(MainActivity.this, "AndroidID：" + androidID, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void copyContentToClipboard(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(mClipData);
    }

    public void alertWindow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 7.0 以上需要引导用去设置开启窗口浮动权限
            requestDrawOverLays();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 6.0 动态申请
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    private void createWindow() {
        manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0 以上type需要设置成这个
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }

            params.gravity = Gravity.BOTTOM | Gravity.END;

            View windowView = LayoutInflater.from(this).inflate(R.layout.layout_window_info, null);
            tvWindowInfo = windowView.findViewById(R.id.tv_window_info);
            windowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertStackInfo();
                }
            });

            manager.addView(windowView, params);
            new Thread(() -> {
                try {
                    while (true) {
                        tvWindowInfo.post(() -> tvWindowInfo.setText("Thread activeNum:" + Thread.activeCount() + "\nThread groupNum:" + Thread.currentThread().getThreadGroup().activeGroupCount()));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // android 23 以上先引导用户开启这个权限 该权限动态申请不了
    @TargetApi(Build.VERSION_CODES.M)
    public void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Toast.makeText(this, "无法展示全局提示窗口，请授予权限", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivity.this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            Toast.makeText(this, "权限已经授予", Toast.LENGTH_SHORT).show();
            createWindow();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "设置权限拒绝", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "设置权限成功", Toast.LENGTH_SHORT).show();
                createWindow();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (manager != null && windowView.getWm() == null) {
//                wm.addView(windowView, mLayoutParams);
//            }

            Toast.makeText(this, "权限申请成功1111", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertStackInfo() {
        Map<Thread, StackTraceElement[]> threadMap = Thread.getAllStackTraces();
        Log.e("albertThreadDebug", "all start==============================================");
        for (Map.Entry<Thread, StackTraceElement[]> entry : threadMap.entrySet()) {
            Thread thread = entry.getKey();
            StackTraceElement[] stackElements = entry.getValue();
            Log.e("albertThreadDebug", "name:" + thread.getName() + " id:" + thread.getId() + " thread:" + thread.getPriority() + " begin==========");
            for (int i = 0; i < stackElements.length; i++) {
                StringBuilder stringBuilder = new StringBuilder("    ");
                stringBuilder.append(stackElements[i].getClassName() + ".")
                        .append(stackElements[i].getMethodName() + "(")
                        .append(stackElements[i].getFileName() + ":")
                        .append(stackElements[i].getLineNumber() + ")");
                Log.e("albertThreadDebug", stringBuilder.toString());
            }
            Log.e("albertThreadDebug", "name:" + thread.getName() + " id:" + thread.getId() + " thread:" + thread.getPriority() + " end==========");
        }
        Log.e("albertThreadDebug", "all end==============================================");
    }
}