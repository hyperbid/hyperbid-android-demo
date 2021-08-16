/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.hyperbid_android_demo;

import android.content.Context;
import android.util.DisplayMetrics;

public class CommonUtil {
    public static int dip2px(Context context, int value) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float px = displayMetrics.density * value + 0.5f;
        return (int)px;
    }
}
