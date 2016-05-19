package com.muzhihudong.myprojectlib.httpresponse;

import com.squareup.okhttp.Request;

import java.io.IOException;

public interface StringCallBack {
    void onFailure(Request request, IOException e);

    void onResponse(String string);


}
