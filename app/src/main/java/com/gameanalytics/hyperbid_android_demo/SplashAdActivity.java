/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.hyperbid_android_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SplashAdActivity extends Activity {
    String placementIds[] = new String[]{
            DemoApplication.mPlacementId_splash_admob,
            DemoApplication.mPlacementId_splash_adx,
            DemoApplication.mPlacementId_splash_mintegral,
            DemoApplication.mPlacementId_splash_myoffer,
            DemoApplication.mPlacementId_splash_online
    };

    String unitGroupName[] = new String[]{
            "Admob",
            "Adx",
            "Mintegral",
            "MyOffer",
            "OnlineApi"
    };

    int mCurrentSelectIndex;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spinner = (Spinner) findViewById(R.id.splash_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SplashAdActivity.this, android.R.layout.simple_spinner_dropdown_item,
                unitGroupName);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(SplashAdActivity.this,
                        parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                mCurrentSelectIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        findViewById(R.id.show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashAdActivity.this, SplashAdShowActivity.class);
                intent.putExtra("placementId", placementIds[mCurrentSelectIndex]);
                startActivity(intent);
            }
        });


        findViewById(R.id.show_in_current_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashAdActivity.this, SplashAdShowInCurrentActivity.class);
                intent.putExtra("placementId", placementIds[mCurrentSelectIndex]);
                startActivity(intent);
            }
        });
    }

}
