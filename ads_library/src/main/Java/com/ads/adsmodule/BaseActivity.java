package com.ads.adsmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.ads.NativeAdLayout;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
    }

    private void showBanner(ViewGroup baner) {
        if (Glob.FB_ADS) {
            Glob.getInstance().fb_banner(this, baner);
        } else {
            Glob.getInstance().showBanner(this, baner);
        }
    }

    private void bigNative(TextView loading, FrameLayout googleFramLayout, NativeAdLayout fbnativeContainer, View card_view) {
        Glob.getInstance().loadfbNativeAd(this, loading,
                googleFramLayout, fbnativeContainer,
                card_view);
    }

    private void smallNative(View card_view, TextView textView, FrameLayout googleNative, NativeAdLayout fbnativeContainer) {
        Glob.getInstance().smallfbnative(this, card_view, textView, googleNative, fbnativeContainer);
    }

    private void showInter(MyAdListener adListener) {
        Glob.showInterstitial(this, adListener);
    }
}