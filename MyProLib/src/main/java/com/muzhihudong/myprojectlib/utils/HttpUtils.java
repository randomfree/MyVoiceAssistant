package com.muzhihudong.myprojectlib.utils;

import android.os.Handler;
import android.os.Looper;

import com.muzhihudong.myprojectlib.httpresponse.StringCallBack;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * 网络请求工具类
 */
public class HttpUtils {
    private static HttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;//竟然是交货的意思
    private static final String TAG = "HttpUtils";

    private HttpUtils() {
        mOkHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }

            }
        }
        return mInstance;
    }

    /**
     * 同步的get请求 想了半天哪能用到··· 想到了融云的请求用户头像啥的那个鬼 尼玛···
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Response getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 同步get直接返回String
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String getSyncAsString(String url) throws IOException {
        return getSync(url).body().string();
    }

    /**
     * 异步请求直接返回String的
     *
     * @param url
     * @param callBack
     */
    private void getAsync(String url, StringCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(callBack, request);
    }

    /**
     * 同步post请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    private Response postSync(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 同步post请求
     *
     * @param url
     * @param params
     * @return 字符串
     * @throws IOException
     */
    private String postSyncAsString(String url, Param... params) throws IOException {
        Response response = postSync(url, params);
        return response.body().string();
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param callBack
     * @param params
     */
    private void postAsync(String url, StringCallBack callBack, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callBack, request);
    }

    /**
     * 同步文件上传
     *
     * @param url
     * @param files
     * @param filsKeys
     * @param params
     * @return
     * @throws IOException
     */
    private Response postFileSync(String url, File[] files, String[] filsKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, filsKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response postFileSync(String url, File file, String filKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{filKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response postFileSync(String url, File file, String filKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{filKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, StringCallBack callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, StringCallBack callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, StringCallBack callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    private void downLoadAsync(final String url, final String destFileDir, final StringCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFaildStringCallback(request, e, callBack);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    //TODO filename从url获取根据情况修改
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessStringCallback(file.getAbsolutePath(), callBack);
                } catch (IOException e) {
                    sendFaildStringCallback(request, e, callBack);
                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {

                    }
                    if (fos != null) try {
                        fos.close();
                    } catch (IOException e) {

                    }
                }
            }
        });
    }


    private void deliveryResult(final StringCallBack callBack, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFaildStringCallback(request, e, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String string = response.body().string();
                    sendSuccessStringCallback(string, callBack);
                } catch (IOException e) {
                    sendFaildStringCallback(request, e, callBack);
                }
            }
        });
    }

    private void sendFaildStringCallback(final Request request, final IOException e, final StringCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(request, e);
                }
            }
        });
    }

    private void sendSuccessStringCallback(final String string, final StringCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(string);
                }
            }
        });
    }

    public Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.values);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\\" + param.key + "\\"), RequestBody.create(null, param.values));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\\" + fileKeys[i] + " \\; filename=\\" + fileName + "\\"),
                        fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }


    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 验证参数
     *
     * @param params
     * @return
     */
    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    /**
     * 获取文件名
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    public static class Param {
        public Param() {

        }

        public Param(String key, String values) {
            this.key = key;
            this.values = values;
        }

        public String key;
        public String values;
    }

}
