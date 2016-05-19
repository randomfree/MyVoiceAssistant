package com.muzhihudong.myprojectlib.utils;

import android.os.Build;
import android.util.Base64;


import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
    public static boolean isStringEmpty(String str) {
        if (str == null || str.length() <= 0 || "null".equals(str) || str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 两个日期的毫秒值算共几晚
     *
     * @param zhu
     * @param li
     * @return
     */
    public static long getGJW(long zhu, long li) {
        if (li <= zhu) {
            return 0;
        }
        long sub = li - zhu;
        long w = sub / 1000 / 60 / 60 / 24;
        return w;

    }

    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.after(dt2)) {
                return 1;
            } else if (dt1.before(dt2)) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public final static String GetMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回中文的正则表达式
     *
     * @return
     */
    public static String getZWRule() {
        return "[\\u4E00-\\u9FA5]+$";
    }


    public static int String2Int(int defaultInt, String str) {
        if (isStringEmpty(str)) {
            return defaultInt;
        }
        try {
            defaultInt = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            LogUtils.writeLog(LogUtils.class, e, "String2Int");
        }
        return defaultInt;
    }

    public static String getTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date(timestamp));
        return str;
    }

    public static long getTime(String timestamp) {
        if (isStringEmpty(timestamp)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timelong = 0;
        try {
            timelong = sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timelong;
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    public static byte[] decode(String encodetring) {
        return Base64.decode(encodetring, Base64.DEFAULT);
    }
}
