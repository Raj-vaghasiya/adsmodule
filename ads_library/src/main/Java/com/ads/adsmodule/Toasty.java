package com.ads.adsmodule;

import android.content.Context;
import android.widget.Toast;

public class Toasty {
    public static void showToast(Context context,String s){
        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
    }
}
