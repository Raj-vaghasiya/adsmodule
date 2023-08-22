package com.ads.adsmodule;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AppOpenManager implements LifecycleObserver, AdsClass.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "Uvs=======AppOpen";
    public AppOpenAd appOpenAd = null;
    private long loadTime = 0;
    public AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private final AdsClass myApplication;
    private Activity currentActivity;

    public AppOpenManager(AdsClass myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdLoaded(@NonNull @NotNull AppOpenAd appOpenAd) {
                        super.onAdLoaded(appOpenAd);
                        AppOpenManager.this.appOpenAd = appOpenAd;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                    }


                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, Glob.APP_OPEN, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;

    }

    public static boolean isShowingAd = false;

    public void showAdIfAvailable() {

        if (!isShowingAd && isAdAvailable()) {
            if (!Glob.AdsOpenIntrestial) {
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                AppOpenManager.this.appOpenAd = null;
                                isShowingAd = false;
                                fetchAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;
                            }
                        };
                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity);
            }

        } else {
            countDownTimerFistTime.start();

        }
    }


    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
    }


    public CountDownTimer countDownTimerFistTime = new CountDownTimer(300000, 10) {
        @Override
        public void onTick(long millisUntilFinished) {

            if (Glob.LoadingAllData) {
                fetchAd();
                countDownTimerFistTime.cancel();

            }
        }

        @Override
        public void onFinish() {
        }
    };
}
