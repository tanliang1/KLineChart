package com.github.fujianlian.klinechart.net;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.FormBody;
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
    public static final String url1 = "www.baidu.com";



    OkHttpClient client = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post(String url, String json) throws IOException {
        /*RequestBody body = RequestBody.create(json, JSON);*/
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("method","get_current_token");
        formBody.add("mob","15210366756");
        formBody.add("pwd","asdf456987");
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

