/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.demoApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SplashAdActivity extends Activity {

    int mCurrentSelectIndex;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashAdActivity.this, SplashAdShowActivity.class);
                intent.putExtra("placementId", PlacementId.NATIVE_SPLASH_ID);
                startActivity(intent);
            }
        });


        findViewById(R.id.show_in_current_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashAdActivity.this, SplashAdShowInCurrentActivity.class);
                intent.putExtra("placementId", PlacementId.NATIVE_SPLASH_ID);
                startActivity(intent);
            }
        });
    }

}
