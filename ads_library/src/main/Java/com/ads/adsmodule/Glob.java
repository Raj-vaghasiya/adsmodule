package com.ads.adsmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Lifecycle;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Glob {
    public static NativeAd fbnativeAd = null;
    public static AdLoader ADLOADER = null;
    public static int exit_check = 0;
    private static InterstitialAd fbinterstitialAd = null;
    public static com.google.android.gms.ads.interstitial.InterstitialAd Inter_Google;
    public static com.google.android.gms.ads.nativead.NativeAd Native_Google;
    public static com.google.android.gms.ads.nativead.NativeAd Native_Google_small;

    public static int ADS_CLICK = 0;
    public static int CLICK_INTERVAL = 0;

    public static String FB_SPLASH_INTERSTITIAL = "";
    public static String FB_INTERSTITIAL = "";
    public static String FB_NATIVE = "";
    public static String FB_BANNER = "";
    public static String FB_NATIVE_BANNER = "";
    public static String GOOGLE_NATIVE = "";
    public static String GOOGLE_NATIVE_BANNER = "";
    public static String GOOGLE_INTERSTITIAL = "";
    public static String GOOGLE_BANNER = "";
    public static String APP_OPEN = "";
    public static String PACKAGE = "";
    public static String PRIVACY = "";
    public static boolean FB_ADS = false;

    private static long mLastClickTime = 0;
    public static boolean AdsOpenIntrestial = false;
    public static boolean LoadingAllData = false;

    private static Glob mInstance;
    com.facebook.ads.AdView adView2;

    public static Glob getInstance() {
        if (mInstance == null) {
            mInstance = new Glob();
        }
        return mInstance;
    }

    public void showBanner(Activity activity, ViewGroup relativeLayout) {
        String string = Glob.GOOGLE_BANNER;
        AdView adView = new AdView(activity);
        adView.setAdSize(getAdSize(activity));
        adView.setAdUnitId(string);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                fb_banner(activity, relativeLayout);
            }

            @Override

            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
            }
        });
        relativeLayout.addView(adView);
    }

    private AdSize getAdSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
    }

    public void fb_banner(Activity activity, ViewGroup relativeLayout) {
        adView2 = new com.facebook.ads.AdView(activity, Glob.FB_BANNER, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        ViewGroup adContainer = relativeLayout;
        adContainer.addView(adView2);
        adView2.loadAd();
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager;
        if (!(context == null || (connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)) == null)) {
            if (Build.VERSION.SDK_INT >= 29) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities == null ||
                        (!networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) {
                    return false;
                }
                return true;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static void handleDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }


    public static void showInterstitial(AppCompatActivity activity, MyAdListener myAdListener) {
        handleDoubleClick();
        if (Glob.isOnline(activity)) {
            if (!Glob.FB_INTERSTITIAL.equals("") || !Glob.GOOGLE_INTERSTITIAL.equals("")) {

                if (Glob.ADS_CLICK == Glob.CLICK_INTERVAL) {

                    Dialog progressDialog = new Dialog(activity, R.style.full_dialog);
                    progressDialog.setContentView(R.layout.ad_dialog);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    if (Glob.FB_ADS) {
                        loadFBAd(activity, progressDialog, myAdListener);
                    } else {
                        AdsOpenIntrestial = false;
                        loadGoogleAd(activity, progressDialog, myAdListener);
                    }
                } else {
                    Glob.ADS_CLICK++;
                    myAdListener.onAdDismissed();
                }
            } else {
                myAdListener.onAdDismissed();
            }
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public static void loadFBAd(AppCompatActivity activity, Dialog progressDialog, MyAdListener myAdListener) {

        fbinterstitialAd = new InterstitialAd(activity, Glob.FB_INTERSTITIAL);

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                AdsOpenIntrestial = true;
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e("--deep--", "Interstitial ad displayed.111");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                AdsOpenIntrestial = false;
                Log.e("--deep--", "Interstitial ad dismissed.");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Glob.ADS_CLICK = 0;
                myAdListener.onAdDismissed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {

                if (!Glob.FB_ADS) {
                    Log.e("--deep--", "Glob.FB_ADS loadFBAd = " + Glob.FB_ADS);
                    myAdListener.onAdDismissed();
                } else {
                    Log.e("--deep--", "Glob.FB_ADS loadFBAd else = " + Glob.FB_ADS);
                    AdsOpenIntrestial = false;
                    loadGoogleAd(activity, progressDialog, myAdListener);
                }

                Log.e("--deep--", "Interstitial ad failed to load: 222" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.e("--deep--", "Interstitial ad is loaded and ready to be displayed! 333");
                fbinterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("--deep--", "Interstitial ad clicked! 444");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("--deep--", "Interstitial ad impression logged! 555");
            }
        };

        fbinterstitialAd.loadAd(fbinterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
    }

    public static void loadGoogleAd(AppCompatActivity activity, Dialog progressDialog, MyAdListener myAdListener) {
        Log.e("--deep--", "interstitial loadAd: ad id " + GOOGLE_INTERSTITIAL);
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, GOOGLE_INTERSTITIAL, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        Log.e("--deep--", "interstitial onAdLoaded");
                        Inter_Google = interstitialAd;
                        if (activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                            /*AdsOpenIntrestial=true;*/
                            Inter_Google.show(activity);
                        }

                        Inter_Google.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        AdsOpenIntrestial = false;
                                        Log.e("--deep--", "The ad was dismissed.");
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Inter_Google = null;
                                        Glob.ADS_CLICK = 0;
                                        myAdListener.onAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                        AdsOpenIntrestial = false;
                                        Log.e("--deep--", "The ad failed to show.");
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Inter_Google = null;
                                        myAdListener.onAdDismissed();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        AdsOpenIntrestial = true;
                                        Log.e("--deep--", "The ad was shown.");
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e("--deep--", "interstitial ad failed to load" + loadAdError.getMessage());

//                        Inter_Google = null;
//                        myAdListener.onAdDismissed();

                        if (!Glob.FB_ADS) {
                            Log.e("--deep--", "Glob.FB_ADS loadGoogleAd = " + Glob.FB_ADS);
                            loadFBAd(activity, progressDialog, myAdListener);
                        } else {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Log.e("--deep--", "Glob.FB_ADS loadGoogleAd else = " + Glob.FB_ADS);
                            Inter_Google = null;
                            myAdListener.onAdDismissed();
                        }


                    }
                });
    }


    public static void fixInterstitial(AppCompatActivity activity, MyAdListener myAdListener) {
        handleDoubleClick();
        if (Glob.isOnline(activity)) {
            if (!Glob.FB_INTERSTITIAL.equals("") || !Glob.GOOGLE_INTERSTITIAL.equals("")) {
                Dialog progressDialog = new Dialog(activity, R.style.full_dialog);
                progressDialog.setContentView(R.layout.ad_dialog);
                progressDialog.setCancelable(false);
                progressDialog.show();
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                if (Glob.FB_ADS) {
                    fixloadFBAd(activity, progressDialog, myAdListener);
                } else {
                    AdsOpenIntrestial = false;
                    fixloadGoogleAd(activity, progressDialog, myAdListener);
                }
            } else {
                myAdListener.onAdDismissed();
            }
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public static void fixloadFBAd(AppCompatActivity activity, Dialog progressDialog, MyAdListener myAdListener) {

        fbinterstitialAd = new InterstitialAd(activity, Glob.FB_INTERSTITIAL);

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                AdsOpenIntrestial = true;

                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                Log.e("--deep--", "Interstitial ad displayed.111");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                AdsOpenIntrestial = false;
                Log.e("--deep--", "Interstitial ad dismissed.");
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                myAdListener.onAdDismissed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {

                if (!Glob.FB_ADS) {
                    Log.e("--deep--", "Glob.FB_ADS fixloadFBAd = " + Glob.FB_ADS);
                    myAdListener.onAdDismissed();
                } else {
                    Log.e("--deep--", "Glob.FB_ADS fixloadFBAd else = " + Glob.FB_ADS);
                    AdsOpenIntrestial = false;
                    fixloadGoogleAd(activity, progressDialog, myAdListener);
                }

                Log.e("--deep--", "Interstitial ad failed to load: 222" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.e("--deep--", "Interstitial ad is loaded and ready to be displayed! 333");
                fbinterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("--deep--", "Interstitial ad clicked! 444");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("--deep--", "Interstitial ad impression logged! 555");
            }
        };

        fbinterstitialAd.loadAd(fbinterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
    }

    public static void fixloadGoogleAd(AppCompatActivity activity, Dialog progressDialog, MyAdListener myAdListener) {
        Log.e("--deep--", "interstitial loadAd: ad id " + GOOGLE_INTERSTITIAL);
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, GOOGLE_INTERSTITIAL, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        Log.e("--deep--", "interstitial onAdLoaded");
                        Inter_Google = interstitialAd;
                        if (activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                            /*AdsOpenIntrestial=true;*/
                            Inter_Google.show(activity);
                        }

                        Inter_Google.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        AdsOpenIntrestial = false;
                                        Log.e("--deep--", "The ad was dismissed.");
                                        if (progressDialog != null) {
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                        Inter_Google = null;
                                        myAdListener.onAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                        AdsOpenIntrestial = false;
                                        Log.e("--deep--", "The ad failed to show.");
                                        if (progressDialog != null) {
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                        Inter_Google = null;
                                        myAdListener.onAdDismissed();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        AdsOpenIntrestial = true;
                                        Log.e("--deep--", "The ad was shown.");
                                        if (progressDialog != null) {
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e("--deep--", "interstitial ad failed to load" + loadAdError.getMessage());

//                        Inter_Google = null;
//                        myAdListener.onAdDismissed();

                        if (!Glob.FB_ADS) {
                            Log.e("--deep--", "Glob.FB_ADS loadGoogleAd = " + Glob.FB_ADS);
                            fixloadFBAd(activity, progressDialog, myAdListener);
                        } else {
                            if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                            Log.e("--deep--", "Glob.FB_ADS loadGoogleAd else = " + Glob.FB_ADS);
                            Inter_Google = null;
                            myAdListener.onAdDismissed();
                        }

                    }
                });
    }


    // slash implementation
    private NetworkChangeReceiver brd;
    static FirebaseRemoteConfig firebaseRemoteConfig;
    static InterstitialAd splashfbinterstitialAd = null;
    AppOpenAd.AppOpenAdLoadCallback loadCallback;

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

    public static SplashCallBack splashCallBack;

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (Glob.isOnline(context)) {

                    if (!Glob.LoadingAllData) {
                        getAdsData((Activity) context, splashCallBack);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                MobileAds.initialize(context);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        LoadAdsdata((Activity) context, splashCallBack);
                                    }
                                }, 500);
                            }
                        }, 3000);
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAdsData(Activity context, SplashCallBack splashCallBack) {
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

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LoadAdsdata(context, splashCallBack);
                        }
                    }, 500);

                } else {
                    Toast.makeText(context, "Please Turn on Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadAdsdata(Activity context, SplashCallBack splashCallBack) {
        if (Glob.isOnline(context)) {
            if (Glob.FB_ADS) {
                if (!Glob.FB_SPLASH_INTERSTITIAL.equals("")) {
                    SplashFbInter(context, splashCallBack);
                } else if (!Glob.APP_OPEN.equals("")) {
                    showAppOpen(context, splashCallBack);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashCallBack.init();
                        }
                    }, 1000);
                }
            } else {
                if (!Glob.APP_OPEN.equals("")) {
                    showAppOpen(context, splashCallBack);
                } else if (!Glob.FB_SPLASH_INTERSTITIAL.equals("")) {
                    SplashFbInter(context, splashCallBack);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashCallBack.init();
                        }
                    }, 1000);
                }
            }
        } else if (!Glob.isOnline(context)) {
            Toast.makeText(context, "Network connection is not available", Toast.LENGTH_LONG).show();
        }
    }

    public void SplashFbInter(Activity context, SplashCallBack splashCallBack) {
        if (Glob.isOnline(context)) {

            fbinterstitialAd = new InterstitialAd(context, Glob.FB_SPLASH_INTERSTITIAL);

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    Log.e("--deep--", "Interstitial ad displayed.111");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.e("--deep--", "Interstitial ad dismissed.");
                    Glob.ADS_CLICK = 0;
                    splashCallBack.init();
                }

                @Override
                public void onError(Ad ad, AdError adError) {

                    if (Glob.FB_ADS) {
                        if (!Glob.APP_OPEN.equals("")) {
                            showAppOpen(context, splashCallBack);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    splashCallBack.init();
                                }
                            }, 500);
                        }
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                splashCallBack.init();
                            }
                        }, 500);
                    }
                    Log.e("--deep--", "Interstitial ad failed to load: 222" + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d("--deep--", "Interstitial ad is loaded and ready to be displayed! 333");
                    fbinterstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d("--deep--", "Interstitial ad clicked! 444");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d("--deep--", "Interstitial ad impression logged! 555");
                }
            };

            fbinterstitialAd.loadAd(fbinterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAppOpen(Activity context, SplashCallBack splashCallBack) {

        FullScreenContentCallback fullScreenContentCallback =
                new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        splashCallBack.init();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {

                        if (!Glob.FB_ADS) {
                            if (!Glob.FB_SPLASH_INTERSTITIAL.equals("")) {
                                SplashFbInter(context, splashCallBack);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        splashCallBack.init();
                                    }
                                }, 500);

                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    splashCallBack.init();
                                }
                            }, 500);
                        }
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Glob.AdsOpenIntrestial = true;
                    }
                };

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (!Glob.FB_ADS) {
                    if (!Glob.FB_SPLASH_INTERSTITIAL.equals("")) {
                        SplashFbInter(context, splashCallBack);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                splashCallBack.init();
                            }
                        }, 500);
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashCallBack.init();
                        }
                    }, 500);
                }
            }

            @Override
            public void onAdLoaded(@NonNull @NotNull AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(context);
            }
        };
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context, Glob.APP_OPEN, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }


    //native ad
    LinearLayout fbAdView;
    NativeBannerAd nativeBannerAd;
    //    NativeAdLayout nativeAdLayout;
    LinearLayout adView;
    com.google.android.gms.ads.nativead.NativeAd mynative;
    public void loadfbNativeAd(Activity baseActivity, TextView tvLoadingAds, FrameLayout googleFrameNative, NativeAdLayout fbAdLayout, View view) {
        if (!Glob.FB_NATIVE.equals("") || !Glob.GOOGLE_NATIVE.equals("")) {
            tvLoadingAds.setVisibility(View.VISIBLE);
            googleFrameNative.setVisibility(View.VISIBLE);
            fbAdLayout.setVisibility(View.VISIBLE);

            //Native_Google
            //GOOGLE_NATIVE
            if (Glob.FB_ADS) {
                Log.e("--deep--", "Glob.FB_ADS loadfbNativeAd = " + Glob.FB_ADS);
                loadFBNativeAd(baseActivity, tvLoadingAds, googleFrameNative, fbAdLayout, view);
            } else {
                Log.e("--deep--", "Glob.FB_ADS loadfbNativeAd else = " + Glob.FB_ADS);
                loadGoogleNativeAd(baseActivity, tvLoadingAds, googleFrameNative, fbAdLayout, view);
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void loadFBNativeAd(Activity baseActivity, TextView tvLoadingAds, FrameLayout googleFrameNative, NativeAdLayout fbAdLayout, View view) {
        if (Glob.fbnativeAd == null) {
            Glob.fbnativeAd = new NativeAd(baseActivity, Glob.FB_NATIVE);

            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                    Log.e("--deep--", "Native ad finished downloading all assets.");
                    if (Glob.fbnativeAd == null || Glob.fbnativeAd != ad) {
                        return;
                    }

                    inflateAd(baseActivity,Glob.fbnativeAd, tvLoadingAds, googleFrameNative, fbAdLayout);
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Glob.fbnativeAd = null;
                    Log.e("--deep--", "Native ad failed to load: " + adError.getErrorMessage());

                    if (!Glob.FB_ADS) {
                        Log.e("--deep--", "Glob.FB_ADS loadFBNativeAd = " + Glob.FB_ADS);
                    } else {
                        Log.e("--deep--", "Glob.FB_ADS loadFBNativeAd else = " + Glob.FB_ADS);
                        loadGoogleNativeAd(baseActivity, tvLoadingAds, googleFrameNative, fbAdLayout, view);
                    }

                }

                @Override
                public void onAdLoaded(Ad ad) {

                    Log.e("--deep--", "Native ad is loaded and ready to be displayed!");
                    if (Glob.fbnativeAd == null || Glob.fbnativeAd != ad) {
                        return;
                    }

                    inflateAd(baseActivity,Glob.fbnativeAd, tvLoadingAds, googleFrameNative, fbAdLayout);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Glob.fbnativeAd = null;
                    loadfbNativeAd(baseActivity, tvLoadingAds, googleFrameNative, fbAdLayout, view);
                    Log.e("--deep--", "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {

                    Log.e("--deep--", "Native ad impression logged!");
                }
            };

            Glob.fbnativeAd.loadAd(
                    Glob.fbnativeAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
            showNativeAdWithDelay(baseActivity,tvLoadingAds, googleFrameNative, fbAdLayout);
        } else {
            if (Glob.fbnativeAd.isAdInvalidated()) {
//                loadfbNativeAd(baseActivity, view);
                return;
            }
            inflateAd(baseActivity,Glob.fbnativeAd, tvLoadingAds, googleFrameNative, fbAdLayout);
        }
    }

    private void inflateAd(Activity baseActivity,NativeAd nativeAd, TextView tvLoadingAds, FrameLayout googleFrameNative, NativeAdLayout fbAdLayout) {

        tvLoadingAds.setVisibility(View.GONE);
        googleFrameNative.setVisibility(View.GONE);

        nativeAd.unregisterView();

        LayoutInflater inflater = LayoutInflater.from(baseActivity);

        fbAdView = (LinearLayout) inflater.inflate(R.layout.fb_ad_layout_1, fbAdLayout, false);
        fbAdLayout.addView(fbAdView);


        LinearLayout adChoicesContainer = fbAdView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(baseActivity, nativeAd, fbAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);


        com.facebook.ads.MediaView nativeAdIcon = fbAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = fbAdView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = fbAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = fbAdView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = fbAdView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = fbAdView.findViewById(R.id.native_ad_sponsored_label);
        AppCompatButton nativeAdCallToAction = fbAdView.findViewById(R.id.native_ad_call_to_action);


        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());


        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);


        nativeAd.registerViewForInteraction(
                fbAdView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

    private void showNativeAdWithDelay(Activity baseActivity,TextView tvLoadingAds, FrameLayout googleFrameNative, NativeAdLayout fbAdLayout) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (Glob.fbnativeAd == null || !Glob.fbnativeAd.isAdLoaded()) {
                    return;
                }

                if (Glob.fbnativeAd.isAdInvalidated()) {
                    return;
                }
                inflateAd(baseActivity,Glob.fbnativeAd, tvLoadingAds, googleFrameNative, fbAdLayout);
            }
        }, 1000 * 60 * 15);
    }

    private void loadGoogleNativeAd(Activity baseActivity, TextView tvLoadingAds, FrameLayout googleFrameNative, NativeAdLayout fbAdLayout, View view) {

        if (Glob.Native_Google == null) {
            Log.e("--deep--", "refreshAd: MainStartActivity ad not loaded null ad");
            VideoOptions videoOptions =
                    new VideoOptions.Builder().setStartMuted(true).build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            Glob.ADLOADER = new AdLoader.Builder(baseActivity, Glob.GOOGLE_NATIVE).withNativeAdOptions(adOptions)
                    .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                            Glob.Native_Google = nativeAd;

//                            FrameLayout frameLayout = findViewById(R.id.nativead);
                            NativeAdView adView =
                                    (NativeAdView) baseActivity.getLayoutInflater().inflate(R.layout.mygoogle_native, null);
                            populateNativeAdView(baseActivity,nativeAd, adView, tvLoadingAds, fbAdLayout);
                            googleFrameNative.removeAllViews();
                            googleFrameNative.addView(adView);

                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Glob.Native_Google = null;

                            if (!Glob.FB_ADS) {
                                Log.e("--deep--", "Glob.FB_ADS loadGoogleNativeAd = " + Glob.FB_ADS);
                                loadFBNativeAd(baseActivity, tvLoadingAds, googleFrameNative, fbAdLayout, view);
                            } else {
                                Log.e("--deep--", "Glob.FB_ADS loadGoogleNativeAd else = " + Glob.FB_ADS);
                            }
                        }

                        @Override
                        public void onAdOpened() {
                            super.onAdOpened();
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.e("--deep--", "onAdLoaded: successfully loaded in MainStartActivity");
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }
                    }).build();
            Glob.ADLOADER.loadAd(new AdRequest.Builder().build());

        } else {

            Log.e("--deep--", "refreshAd: Glob ad already loaded showing in MainStartActivity");
//            FrameLayout frameLayout = findViewById(R.id.nativead);
            NativeAdView adView =
                    (NativeAdView) baseActivity.getLayoutInflater().inflate(R.layout.mygoogle_native, null);
            populateNativeAdView(baseActivity, Glob.Native_Google, adView, tvLoadingAds, fbAdLayout);
            googleFrameNative.removeAllViews();
            googleFrameNative.addView(adView);

        }

    }

    private void populateNativeAdView(Activity baseActivity, com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView, TextView tvLoadingAds, NativeAdLayout fbAdLayout) {
        tvLoadingAds.setVisibility(View.GONE);
        fbAdLayout.setVisibility(View.GONE);

        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        Objects.requireNonNull(adView.getMediaView()).setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

    }

    //small native

    public void loadsmallFBNative(Activity baseActivity,View view, TextView tv_loadingAds_1, FrameLayout googleFrameNativesmall, NativeAdLayout fbAdLayoutnativenbanner) {
        if (!Glob.FB_NATIVE_BANNER.equals("")) {
            nativeBannerAd = new NativeBannerAd(baseActivity, Glob.FB_NATIVE_BANNER);
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets
                    Log.e("--deep--", "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("--deep--", "onError: " + adError.getErrorMessage());
//                    loadNativeBanner();

                    if (!Glob.FB_ADS) {
                        Log.e("--deep--", "Glob.FB_ADS loadsmallFBNative = " + Glob.FB_ADS);
                    } else {
                        Log.e("--deep--", "Glob.FB_ADS loadsmallFBNative else = " + Glob.FB_ADS);
                        checkAndShowNativesmall(view, baseActivity, googleFrameNativesmall, tv_loadingAds_1, fbAdLayoutnativenbanner, new MyNativeAdListener() {
                            @Override
                            public void onAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                                Log.e("TAG", "onNativeAdLoaded : mainactivity");
                                mynative = nativeAd;
                                NativeAdView adView =
                                        (NativeAdView) baseActivity.getLayoutInflater().inflate(R.layout.mygoogle_native_small, null);
                                populateNativeAdViewsmall(googleFrameNativesmall, mynative, adView);
                            }
                        });
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    tv_loadingAds_1.setVisibility(View.GONE);
                    Log.d("--deep--", "Native ad is loaded and ready to be displayed!");
                    if (nativeBannerAd == null || nativeBannerAd != ad) {
                        return;
                    }
                    // Inflate Native Banner Ad into Container
                    inflateAd(baseActivity,nativeBannerAd, fbAdLayoutnativenbanner);
//                inflateAd1(nativeBannerAd);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                    smallfbnative(baseActivity,view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
                    Log.d("--deep--", "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                    Log.d("--deep--", "Native ad impression logged!");
                }
            };
            // load the ad
            nativeBannerAd.loadAd(
                    nativeBannerAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
            showNativeAdWithDelay(baseActivity,tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
        }

    }

    public void smallfbnative(Activity baseActivity,View view, TextView tv_loadingAds_1, FrameLayout googleFrameNativesmall, NativeAdLayout fbAdLayoutnativenbanner) {
        if (!Glob.FB_NATIVE_BANNER.equals("") || !Glob.GOOGLE_NATIVE_BANNER.equals("")) {

            tv_loadingAds_1.setVisibility(View.VISIBLE);
            googleFrameNativesmall.setVisibility(View.VISIBLE);
            fbAdLayoutnativenbanner.setVisibility(View.VISIBLE);
            Log.e("sasasasas", "smallfbnative: " + Glob.FB_NATIVE_BANNER);

            if (Glob.FB_ADS) {
                loadsmallFBNative(baseActivity,view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
            } else {
                checkAndShowNativesmall(view, baseActivity, googleFrameNativesmall, tv_loadingAds_1, fbAdLayoutnativenbanner, new MyNativeAdListener() {
                    @Override
                    public void onAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                        Log.e("TAG", "onNativeAdLoaded : mainactivity");
                        mynative = nativeAd;
                        NativeAdView adView =
                                (NativeAdView) baseActivity.getLayoutInflater().inflate(R.layout.mygoogle_native_small, null);
                        populateNativeAdViewsmall(googleFrameNativesmall, mynative, adView);
                    }
                });
            }

        } else {
            view.setVisibility(View.GONE);
        }

    }

    private void inflateAd(Activity baseActivity,NativeBannerAd nativeBannerAd, NativeAdLayout fbAdLayoutnativenbanner) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
//        nativeAdLayout = findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(baseActivity);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.fb_ad_layout_small, fbAdLayoutnativenbanner, false);
        fbAdLayoutnativenbanner.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(baseActivity, nativeBannerAd, fbAdLayoutnativenbanner);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        com.facebook.ads.MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        AppCompatButton nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    public interface MyNativeAdListener {
        void onAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd);
    }

    //load and give callback
    public void loadNativeAndShowsmall(Activity baseActivity, View view, TextView tv_loadingAds_1, FrameLayout googleFrameNativesmall, NativeAdLayout fbAdLayoutnativenbanner, MyNativeAdListener myNativeAdListener) {

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        AdLoader adLoader = new AdLoader.Builder(baseActivity, Glob.GOOGLE_NATIVE_BANNER).withNativeAdOptions(adOptions)
                .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {

                        Glob.Native_Google_small = nativeAd;
                        myNativeAdListener.onAdLoaded(nativeAd);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Glob.Native_Google_small = null;

                        if (!Glob.FB_ADS) {
                            Log.e("--deep--", "Glob.FB_ADS loadNativeAndShowsmall = " + Glob.FB_ADS);
                            loadsmallFBNative(baseActivity,view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
                        } else {
                            Log.e("--deep--", "Glob.FB_ADS loadNativeAndShowsmall else= " + Glob.FB_ADS);
                        }
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void checkAndShowNativesmall(View view, Activity activity, FrameLayout googleFrameNativesmall, TextView tv_loadingAds_1, NativeAdLayout fbAdLayoutnativenbanner, MyNativeAdListener myNativeAdListener) {
        if (Glob.Native_Google_small != null) {

            showNativesmall(activity, view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
        } else {
            loadNativeAndShowsmall(activity, view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner, myNativeAdListener);
        }
    }

    public void showNativesmall(Activity activity, View view, TextView tv_loadingAds_1, FrameLayout googleFrameNativesmall, NativeAdLayout fbAdLayoutnativenbanner) {
        if (Glob.Native_Google_small != null) {
            tv_loadingAds_1.setVisibility(View.GONE);

            NativeAdView adView =
                    (NativeAdView) activity.getLayoutInflater().inflate(R.layout.mygoogle_native_small, null);
            populateNativeAdViewsmall(Glob.Native_Google_small, adView);
            googleFrameNativesmall.removeAllViews();
            googleFrameNativesmall.addView(adView);
        } else {
            loadnativesmall(activity, view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
        }
    }

    public static void populateNativeAdViewsmall(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        Objects.requireNonNull(adView.getMediaView()).setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

    }

    public static void populateNativeAdViewsmall(FrameLayout frameLayout, com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        Objects.requireNonNull(adView.getMediaView()).setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        frameLayout.removeAllViews();
        frameLayout.addView(adView);

    }

    public void loadnativesmall(Activity baseActivity, View view, TextView tv_loadingAds_1, FrameLayout googleFrameNativesmall, NativeAdLayout fbAdLayoutnativenbanner) {

        if (Glob.ADLOADER != null) {
            if (Glob.ADLOADER.isLoading()) {
                return;
            }
        }

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        Glob.ADLOADER = new AdLoader.Builder(baseActivity, Glob.GOOGLE_NATIVE_BANNER).withNativeAdOptions(adOptions)
                .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                        Glob.Native_Google_small = nativeAd;

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Glob.Native_Google_small = null;

                        if (!Glob.FB_ADS) {
                            Log.e("--deep--", "Glob.FB_ADS loadnativesmall = " + Glob.FB_ADS);
                            loadsmallFBNative(baseActivity,view, tv_loadingAds_1, googleFrameNativesmall, fbAdLayoutnativenbanner);
                        } else {
                            Log.e("--deep--", "Glob.FB_ADS loadnativesmall else = " + Glob.FB_ADS);
                        }

                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                }).build();
        Glob.ADLOADER.loadAd(new AdRequest.Builder().build());
    }


}
