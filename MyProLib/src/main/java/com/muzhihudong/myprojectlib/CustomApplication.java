package com.muzhihudong.myprojectlib;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.muzhihudong.myprojectlib.utils.ImageUtils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CustomApplication extends Application {

    private static CustomApplication app;

    private static long mMainThreadId;
    private static Handler mMainHander;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initImageLoader();
        mMainThreadId = android.os.Process.myTid();
        //创建主线程的Handler
        mMainHander = new Handler();
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainHander() {
        return mMainHander;
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(
                ImageUtils.getDefaultImageLoaderOprations()).memoryCache(new WeakMemoryCache()).build();
        ImageLoader.getInstance().init(config);
    }

    public static CustomApplication getApp() {
        return app;
    }


}
