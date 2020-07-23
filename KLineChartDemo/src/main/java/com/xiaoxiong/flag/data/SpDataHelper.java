package com.xiaoxiong.flag.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiong.flag.entity.PricePeriodResponseEntity;
import com.xiaoxiong.flag.entity.StockResponseEntity;
import com.xiaoxiong.flag.ui.KLineEntity;
import com.xiaoxiong.flag.utils.SharedPreferencesUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static com.xiaoxiong.flag.data.DataOperationHelper.ALL_SECURITIES_KEY;
import static com.xiaoxiong.flag.data.DataOperationHelper.KLINES_KEY;
import static com.xiaoxiong.flag.data.DataOperationHelper.STOCKS_KEY;

public class SpDataHelper {
    public static List<StockResponseEntity> getAllSecuritiesFromSp(Context context) {
        try {
            String allSecuritiesJson = (String) SharedPreferencesUtil.get(context, ALL_SECURITIES_KEY,null);
            Gson gson = new Gson();
            Type type = new TypeToken<List<StockResponseEntity>>(){}.getType();
            List<StockResponseEntity> list = gson.fromJson(allSecuritiesJson, type);
            return list;
        } catch (Exception ex) {
            return null;
        }
    }

    public static HashMap<String,List<PricePeriodResponseEntity>> getAllPriceSecuritiesMapFromSp(Context context) {
        try {
            String json = (String) SharedPreferencesUtil.get(context, STOCKS_KEY,null);
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String,List<PricePeriodResponseEntity>>>(){}.getType();
            HashMap<String,List<PricePeriodResponseEntity>> map = gson.fromJson(json, type);
            return map;
        } catch (Exception ex) {
            return null;
        }
    }

    public static HashMap<String,List<KLineEntity>> getKLinesMapFromSp(Context context) {
        try {
            String json = (String) SharedPreferencesUtil.get(context, KLINES_KEY,null);
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String,List<KLineEntity>>>(){}.getType();
            HashMap<String,List<KLineEntity>> map = gson.fromJson(json, type);
            return map;
        } catch (Exception ex) {
            return null;
        }
    }
}
