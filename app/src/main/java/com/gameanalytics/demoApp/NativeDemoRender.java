/*
 * Copyright © 2018-2020 HyperBid. All rights reserved.
 * https://www.hyperbidad.com
 * Licensed under the HyperBid SDK License Agreement
 * https://github.com/hyperbidteam/HyperBid-Android-SDK/blob/master/LICENSE
 */

package com.gameanalytics.demoApp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyperbid.nativead.api.HBNativeAdRenderer;
import com.hyperbid.nativead.api.HBNativeImageView;
import com.hyperbid.nativead.unitgroup.api.CustomNativeAd;

import java.util.ArrayList;
import java.util.List;


public class NativeDemoRender implements HBNativeAdRenderer<CustomNativeAd> {

    Context mContext;
    List<View> mClickView = new ArrayList<>();
    View mCloseView;

    public NativeDemoRender(Context context) {
        mContext = context;
    }

    View mDevelopView;

    int mNetworkType;

    @Override
    public View createView(Context context, int networkType) {
        if (mDevelopView == null) {
            mDevelopView = LayoutInflater.from(context).inflate(R.layout.native_ad_item, null);
        }
        mNetworkType = networkType;
        if (mDevelopView.getParent() != null) {
            ((ViewGroup) mDevelopView.getParent()).removeView(mDevelopView);
        }
        return mDevelopView;
    }

    @Override
    public void renderAdView(View view, CustomNativeAd ad) {
        mClickView.clear();
        TextView titleView = (TextView) view.findViewById(R.id.native_ad_title);
        TextView descView = (TextView) view.findViewById(R.id.native_ad_desc);
        TextView ctaView = (TextView) view.findViewById(R.id.native_ad_install_btn);
        TextView adFromView = (TextView) view.findViewById(R.id.native_ad_from);
        FrameLayout contentArea = (FrameLayout) view.findViewById(R.id.native_ad_content_image_area);
        FrameLayout iconArea = (FrameLayout) view.findViewById(R.id.native_ad_image);
        final HBNativeImageView logoView = (HBNativeImageView) view.findViewById(R.id.native_ad_logo);

        // bind close button
        CustomNativeAd.ExtraInfo extraInfo = new CustomNativeAd.ExtraInfo.Builder()
                .setCloseView(mCloseView)
                .build();
        ad.setExtraInfo(extraInfo);

        titleView.setText("");
        descView.setText("");
        ctaView.setText("");
        adFromView.setText("");
        titleView.setText("");
        contentArea.removeAllViews();
        iconArea.removeAllViews();
        logoView.setImageDrawable(null);

        View mediaView = ad.getAdMediaView(contentArea, contentArea.getWidth());

        if (ad.isNativeExpress()) {// Template rendering
            titleView.setVisibility(View.GONE);
            descView.setVisibility(View.GONE);
            ctaView.setVisibility(View.GONE);
            logoView.setVisibility(View.GONE);
            iconArea.setVisibility(View.GONE);
            if (mCloseView != null) {
                mCloseView.setVisibility(View.GONE);
            }
            if (mediaView.getParent() != null) {
                ((ViewGroup) mediaView.getParent()).removeView(mediaView);
            }

            contentArea.addView(mediaView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            return;
        }

        // Custom rendering

        titleView.setVisibility(View.VISIBLE);
        descView.setVisibility(View.VISIBLE);
        ctaView.setVisibility(View.VISIBLE);
        logoView.setVisibility(View.VISIBLE);
        iconArea.setVisibility(View.VISIBLE);
        if (mCloseView != null) {
            mCloseView.setVisibility(View.VISIBLE);
        }
        View adiconView = ad.getAdIconView();


        final HBNativeImageView iconView = new HBNativeImageView(mContext);
        if (adiconView == null) {
            iconArea.addView(iconView);
            iconView.setImage(ad.getIconImageUrl());
            mClickView.add(iconView);
        } else {
            iconArea.addView(adiconView);
        }


        if (!TextUtils.isEmpty(ad.getAdChoiceIconUrl())) {
            logoView.setImage(ad.getAdChoiceIconUrl());
        } else {
//            logoView.setImageResource(R.drawable.ad_logo);
        }


        if (mediaView != null) {
            if (mediaView.getParent() != null) {
                ((ViewGroup) mediaView.getParent()).removeView(mediaView);
            }

            contentArea.addView(mediaView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        } else {

            final HBNativeImageView imageView = new HBNativeImageView(mContext);

            imageView.setImage(ad.getMainImageUrl());
            ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            contentArea.addView(imageView, params);

            mClickView.add(imageView);
        }

        titleView.setText(ad.getTitle());
        descView.setText(ad.getDescriptionText());
        ctaView.setText(ad.getCallToActionText());
        if (!TextUtils.isEmpty(ad.getAdFrom())) {
            adFromView.setText(ad.getAdFrom() != null ? ad.getAdFrom() : "");
            adFromView.setVisibility(View.VISIBLE);
        } else {
            adFromView.setVisibility(View.GONE);
        }

        mClickView.add(titleView);
        mClickView.add(descView);
        mClickView.add(ctaView);

    }

    public List<View> getClickView() {
        return mClickView;
    }

    public void setCloseView(ImageView closeView) {
        this.mCloseView = closeView;

    }
}
