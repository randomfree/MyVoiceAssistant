package com.muzhihudong.myprojectlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2016/4/26.
 */
public class SharedPreferencesUtils {

    public static SharedPreferences getTopicSendSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("topicSendSp", Context.MODE_PRIVATE);
        return sp;
    }


    /**
     * 序列化保存对象
     *
     * @throws IOException
     */
    public static void saveObject(Context c, Object object, String sp_key) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        LogUtils.writeLog("bytearrio----size" + byteArrayOutputStream.size());
        objectOutputStream.close();
        byteArrayOutputStream.close();
        saveObject(c, serStr, sp_key);
    }

    private static void saveObject(Context c, String strObject, String sp_key) {
        SharedPreferences sp = c.getSharedPreferences(sp_key, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(sp_key, strObject);
        edit.commit();
    }


    private static String getString(Context context, String sp_key) {
        SharedPreferences sp = context.getSharedPreferences(sp_key, 0);
        return sp.getString(sp_key, null);
    }

    /**
     * 序列化获取对象
     *
     * @param context
     * @param sp_key
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getObject(Context context, String sp_key) throws IOException,
            ClassNotFoundException {
        String str = getString(context, sp_key);
        if (StringUtils.isStringEmpty(str)) {
            return null;
        }
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object configBean = (Object) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return configBean;
    }

    /**
     * 清空存储数据
     *
     * @param c
     * @param sp_key
     */
    public static void clearData(Context c, String sp_key) {
        SharedPreferences dataBase = c.getSharedPreferences(sp_key, 0);
        dataBase.edit().clear().commit();
    }
}
