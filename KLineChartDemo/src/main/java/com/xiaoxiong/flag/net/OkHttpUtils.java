package com.xiaoxiong.flag.net;

import android.annotation.SuppressLint;

import java.io.IOException;


import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    private static OkHttpUtils mInstance;

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class){
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;

    }

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static final String url = "https://dataapi.joinquant.com/apis";

    OkHttpClient client = new OkHttpClient();


    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (@SuppressLint("NewApi") Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public void postAysn(String url, String jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
         client.newCall(request).enqueue(callback);
    }
}

