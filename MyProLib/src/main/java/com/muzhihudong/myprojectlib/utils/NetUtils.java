package com.muzhihudong.myprojectlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by sonic on 2016/4/11.
 */
public class NetUtils {
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return true;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
