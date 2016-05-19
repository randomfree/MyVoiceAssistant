package com.muzhihudong.myprojectlib.presenter;

import android.os.Handler;

import com.muzhihudong.myprojectlib.BaseHttpResponse;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Prensenter基类
 * 带有网络请求功能
 */
public class BasePresenter {

    private BaseViews baseViews;
    private Handler handler;
    private OkHttpClient okHttpClient;

    public BasePresenter(BaseViews baseViews, Handler handler) {
        this.baseViews = baseViews;
        this.handler = handler;
        okHttpClient = new OkHttpClient();
    }

    public void httpGet(String url, BaseHttpResponse baseHttpResponse) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(baseHttpResponse);
    }

    public void httpPost(String url, HashMap<String, String> args, BaseHttpResponse baseHttpResponse) {
        FormEncodingBuilder builder = HashMap2FormEncodingBuilder(args);
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(baseHttpResponse);

    }

    public void upLoadFile(String url,File file,BaseHttpResponse baseHttpResponse) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(fileBody).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(baseHttpResponse);

    }

    public void download() {

    }

    public void cancleAll() {

    }

    public void reTry() {

    }

    public static FormEncodingBuilder HashMap2FormEncodingBuilder(HashMap<String, String> map) {
        if (map == null || map.size() == 0) {
            return new FormEncodingBuilder();
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String values = (String) entry.getValue();
            builder.add(key, values);
        }
        return builder;
    }

}
