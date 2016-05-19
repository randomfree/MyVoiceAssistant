package com.muzhihudong.myprojectlib.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.muzhihudong.myprojectlib.CustomApplication;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ToastUtils {

    private static List<Toast> toasts = new ArrayList<>();
    private static Handler handler;
    private static Timer timer;
    private static TimerTask timerTask;

    static {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (toasts.size() > 0) {
                    Toast toast = toasts.get(0);
                    toast.show();
                    toasts.remove(0);
                }

            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public static void showToast(Context context, String str) {
        if (context == null || StringUtils.isStringEmpty(str)) {
            return;
        }
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        TextView tv = (TextView) view.findViewById(android.R.id.message);
        tv.setBackgroundColor(context.getResources().getColor(
                android.R.color.transparent));
        showToast(toast, str);
    }

    public static void showToast(String str) {
        if (StringUtils.isStringEmpty(str)) {
            return;
        }
        Toast toast = Toast.makeText(CustomApplication.getApp().getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        TextView tv = (TextView) view.findViewById(android.R.id.message);
        tv.setBackgroundColor(CustomApplication.getApp().getApplicationContext().getResources().getColor(
                android.R.color.transparent));
        showToast(toast, str);
    }

    private static void showToast(Toast toast, String str) {
        boolean isContans = false;
        if (toast != null) {
            for (int i = 0; i < toasts.size(); i++) {
                TextView textView = (TextView) toasts.get(i).getView().findViewById(android.R.id.message);
                String toast_str = textView.getText() + "";
                if (!StringUtils.isStringEmpty(toast_str) && toast_str.equals(str)) {
                    isContans = true;
                }
            }
            if (!isContans) {
                toasts.add(toast);
            }
        }
    }


}
