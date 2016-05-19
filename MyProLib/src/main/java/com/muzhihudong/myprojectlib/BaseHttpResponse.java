package com.muzhihudong.myprojectlib;

import android.os.Handler;
import android.os.Message;

import com.muzhihudong.myprojectlib.presenter.BaseViews;
import com.muzhihudong.myprojectlib.utils.KeyStringUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class BaseHttpResponse implements Callback {

    private BaseViews baseViews;
    private Handler handler;

    public BaseHttpResponse(BaseViews baseViews, Handler handler) {
        this.baseViews = baseViews;
        this.handler = handler;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        if (baseViews != null) {
            baseViews.onLoadFaild();
        }
    }

    @Override
    public void onResponse(Response response) throws IOException {
        //非UI线程 可做延时操作
        if (baseViews != null) {
            baseViews.onLoadComplte();
        }
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.what = KeyStringUtils.HTTP_SUCCEED;
            message.obj = response;
            handler.sendMessage(message);
        }
    }

}
