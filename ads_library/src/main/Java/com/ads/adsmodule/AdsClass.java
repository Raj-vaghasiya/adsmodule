package com.ads.adsmodule;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class AdsClass extends Application {
    static Application instance;
    public static AppOpenManager appOpenManager;
    NetworkChangeReceiver brd;
    FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        setupActivityListener();

        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("--deep--", "onInitializationComplete: " + initializationStatus);
            }
        });

        Log.e("qqqqqqqqqq", "onCreate: aaaa " );

        appOpenManager = new AppOpenManager(this);

        if (Glob.isOnline(instance)) {
            brodCarst(instance);
        }

    }
    public void brodCarst(final Context ctx) {
        try {
            brd = new NetworkChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            ctx.registerReceiver(brd, filter);
        } catch (Exception e) {

        }
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {

            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (Glob.isOnline(context)) {

                    if (!Glob.LoadingAllData) {
                        getAdsData(context);
                    }

                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAdsData(Context context) {
        Glob.LoadingAllData = true;
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.ads_data);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {

                    Glob.CLICK_INTERVAL = Integer.parseInt(firebaseRemoteConfig.getString("CLICK_INTERVAL"));
                    Glob.FB_SPLASH_INTERSTITIAL = firebaseRemoteConfig.getString("FB_SPLASH_INTERSTITIAL");
                    Glob.FB_INTERSTITIAL = firebaseRemoteConfig.getString("FB_INTERSTITIAL");
                    Glob.FB_NATIVE = firebaseRemoteConfig.getString("FB_NATIVE");
                    Glob.FB_BANNER = firebaseRemoteConfig.getString("FB_BANNER");
                    Glob.FB_NATIVE_BANNER = firebaseRemoteConfig.getString("FB_NATIVE_BANNER");

                    Glob.GOOGLE_NATIVE = firebaseRemoteConfig.getString("GOOGLE_NATIVE");
                    Glob.GOOGLE_NATIVE_BANNER = firebaseRemoteConfig.getString("GOOGLE_NATIVE_BANNER");
                    Glob.GOOGLE_INTERSTITIAL = firebaseRemoteConfig.getString("GOOGLE_INTERSTITIAL");
                    Glob.GOOGLE_BANNER = firebaseRemoteConfig.getString("GOOGLE_BANNER");
                    Glob.APP_OPEN = firebaseRemoteConfig.getString("APP_OPEN");
                    Glob.PACKAGE = firebaseRemoteConfig.getString("PACKAGE");
                    Glob.PRIVACY = firebaseRemoteConfig.getString("PRIVACY");

                    Glob.FB_ADS = firebaseRemoteConfig.getBoolean("FB_ADS");


//                    Glob.CLICK_INTERVAL = 3;
//                    Glob.FB_SPLASH_INTERSTITIAL = "";
//                    Glob.FB_NATIVE = "";
//                    Glob.FB_NATIVE_BANNER = "";
//                    Glob.FB_BANNER = "";
//                    Glob.FB_INTERSTITIAL = "";
//                    Glob.GOOGLE_NATIVE = "";
//                    Glob.GOOGLE_NATIVE_BANNER = "";
//                    Glob.GOOGLE_BANNER = "";
//                    Glob.GOOGLE_INTERSTITIAL = "";
//                    Glob.APP_OPEN = "";

                    MobileAds.initialize(context);


                }
            }
        });
    }

}
