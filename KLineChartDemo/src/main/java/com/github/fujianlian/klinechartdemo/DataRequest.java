package com.github.fujianlian.klinechartdemo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toolbar;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.net.OkHttpUtils;
import com.github.fujianlian.klinechart.net.TokenBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟网络请求
 * Created by tifezh on 2017/7/3.
 */

public class DataRequest {
    private static List<KLineEntity> datas = null;
    private static String TAG = "DataRequest";


    public static String getStringFromAssert(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            return new String(buffer, 0, buffer.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<KLineEntity> getALL(Context context) {
        if (datas == null) {
            final List<KLineEntity> data = new Gson().fromJson(getStringFromAssert(context, "ibm.json"), new TypeToken<List<KLineEntity>>() {
            }.getType());
            Gson gson = new Gson();
            String body = gson.toJson(new TokenBody());
            try {
                String result = OkHttpUtils.getInstance().post(OkHttpUtils.url1,body);
                Log.d(TAG,"result:" + result);
            } catch (IOException ex) {

            }

            DataHelper.calculate(data);
            datas = data;
        }
        return datas;
    }

    /**
     * 分页查询
     *
     * @param context
     * @param offset  开始的索引
     * @param size    每次查询的条数
     */
    public static List<KLineEntity> getData(Context context, int offset, int size) {
        List<KLineEntity> all = getALL(context);
        List<KLineEntity> data = new ArrayList<>();
        int start = Math.max(0, all.size() - 1 - offset - size);
        int stop = Math.min(all.size(), all.size() - offset);
        for (int i = start; i < stop; i++) {
            data.add(all.get(i));
        }
        return data;
    }

}


