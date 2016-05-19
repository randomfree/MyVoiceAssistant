package com.muzhihudong.myprojectlib.utils;

import android.util.Log;

import com.muzhihudong.myprojectlib.global_config;


public class LogUtils {
    private static String TAG = "com.muzhihudong.myprojectlib";

    public static void writeLog(String message) {
        if (global_config.LOG_SWITCH) {
            Log.e(TAG, message);
        }
    }

    public static void writeLog(String tag, String message) {
        if (global_config.LOG_SWITCH) {
            Log.e(TAG, message);
        }
    }

    public static void writeLog(Class<?> cls, String message) {
        if (global_config.LOG_SWITCH) {
            Log.e(TAG, cls.getSimpleName() + "__" + message);
        }
    }

    public static void writeLog(Class<?> cls, Exception e, String methodName) {
        if (global_config.LOG_SWITCH) {
            Log.e(TAG, cls.getSimpleName() + "_" + methodName + e.getClass().getSimpleName() + "_" + e);
        }
    }

}
