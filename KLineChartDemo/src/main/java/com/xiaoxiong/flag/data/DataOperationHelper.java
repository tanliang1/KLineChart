package com.xiaoxiong.flag.data;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.xiaoxiong.flag.db.FlagProvider;
import com.xiaoxiong.flag.entity.PricePeriodResponseEntity;
import com.xiaoxiong.flag.entity.StockResponseEntity;
import com.xiaoxiong.flag.ui.KLineEntity;
import com.xiaoxiong.flag.utils.LogUtil;
import com.xiaoxiong.flag.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataOperationHelper {
    private final static String TAG = "DbOperationHelper";
    private static HashMap<String,List<PricePeriodResponseEntity>> sStockPriceHashMap = new HashMap<>();

    private static HashMap<String,List<KLineEntity>> sKlineMap = new HashMap<>();

    public final static String KLINES_KEY = "KLines";
    public final static String STOCKS_KEY = "stocks";
    public final static String ALL_SECURITIES_KEY = "all_securities";
    public static List<StockResponseEntity> stockResponseEntities;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void initData(Context context) {
        stockResponseEntities =  DataRequest.getInstance().getTestStocks();
        for (int i = 0; i < 1/*stockResponseEntities.size()*/ ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);
            if (sStockPriceHashMap.get(stockResponseEntity.getCode()) == null) {
                List<PricePeriodResponseEntity> pricePeriodResponseEntities = DataRequest.getInstance().getPricePeriodSecurities(stockResponseEntity,context);
                sStockPriceHashMap.put(stockResponseEntity.getCode(),pricePeriodResponseEntities);
                List<PricePeriodResponseEntity> subList = pricePeriodResponseEntities.subList(pricePeriodResponseEntities.size()-15,pricePeriodResponseEntities.size()-1);
                insertData(subList,context);
            }
        }
        /*Gson gson = new Gson();
        String stocksPrice = gson.toJson(sStockPriceHashMap);
        String allSecurities = gson.toJson(stockResponseEntities);
        SharedPreferencesUtil.put(context, STOCKS_KEY,stocksPrice);
        SharedPreferencesUtil.put(context, ALL_SECURITIES_KEY,allSecurities);*/
        LogUtil.d(TAG,"initData,sStockPriceHashMap:"+sStockPriceHashMap);
    }



    public static void insertData(List<PricePeriodResponseEntity> entities,Context context) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try {
            for (int i = 0;i< entities.size();i++) {
                PricePeriodResponseEntity pricePeriodResponseEntity = entities.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("index_key",pricePeriodResponseEntity.getDate()+pricePeriodResponseEntity.getCode());
                contentValues.put("code",pricePeriodResponseEntity.getCode());
                contentValues.put("open",Float.valueOf(pricePeriodResponseEntity.getOpen()));
                contentValues.put("close",Float.valueOf(pricePeriodResponseEntity.getClose()));
                contentValues.put("high",Float.valueOf(pricePeriodResponseEntity.getHigh()== null ? "" : pricePeriodResponseEntity.getHigh()));
                contentValues.put("lowest",Float.valueOf(pricePeriodResponseEntity.getLow()== null ? "" : pricePeriodResponseEntity.getLow()));

                ops.add(ContentProviderOperation.newInsert(FlagProvider.STOCK_PRICE_CONTENT_URI).withValues(contentValues).build());
                /*context.getContentResolver().insert(FlagProvider.STOCK_PRICE_CONTENT_URI,contentValues);*/
            }
            context.getContentResolver().applyBatch(FlagProvider.AUTHORITY,ops);
        } catch (Exception ex) {
            LogUtil.d(TAG,"insertData,ex:"+ex);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void saveData (Context context) {
        if (stockResponseEntities == null) {
            return;
        }
        for (int i = 0; i < stockResponseEntities.size() ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);

                List<PricePeriodResponseEntity> pricePeriodResponseEntities = sStockPriceHashMap.get(stockResponseEntity.getCode());
                List<KLineEntity> sKlineEntities = new ArrayList<>();
                  for (int j=0;j<pricePeriodResponseEntities.size();j++) {
                      PricePeriodResponseEntity pricePeriodResponseEntity = pricePeriodResponseEntities.get(j);
                      KLineEntity kLineEntity = new KLineEntity();
                      kLineEntity.date = pricePeriodResponseEntity.getDate();
                      kLineEntity.open = Float.valueOf(pricePeriodResponseEntity.getOpen());
                      kLineEntity.close = Float.valueOf(pricePeriodResponseEntity.getClose());
                      kLineEntity.high = Float.valueOf(pricePeriodResponseEntity.getHigh());
                      kLineEntity.low = Float.valueOf(pricePeriodResponseEntity.getLow());
                      kLineEntity.code = pricePeriodResponseEntity.getCode();
                      sKlineEntities.add(kLineEntity);
                  }
                sKlineMap.put(stockResponseEntity.getCode(),sKlineEntities);
                /*insertData(pricePeriodResponseEntities,context);*/
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void calculateDate(Context context) {
        if (stockResponseEntities == null) {
            return;
        }
        for (int i = 0; i < stockResponseEntities.size() ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);
            List<KLineEntity> sKlineEntities = sKlineMap.get(stockResponseEntity.getCode());
            DataHelper.calculate(sKlineEntities);
            sKlineMap.put(stockResponseEntity.getCode(),sKlineEntities);
        }
        Gson gson = new Gson();
        String fromJson = gson.toJson(sKlineMap);
        SharedPreferencesUtil.put(context, KLINES_KEY,fromJson);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void updateDate(Context context) {
        if (stockResponseEntities == null) {
            return;
        }
        for (int i = 0; i < stockResponseEntities.size() ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);
            List<KLineEntity> sKlineEntities = sKlineMap.get(stockResponseEntity.getCode());
            DataHelper.calculate(sKlineEntities);
            Gson gson = new Gson();
            String fromJson = gson.toJson(sKlineEntities);
            SharedPreferencesUtil.put(context, KLINES_KEY +stockResponseEntity.getCode(),fromJson);
        }
    }



   /* public static void saveStocksMapToSp (Context context) {
        String stocksHashMap = (String)SharedPreferencesUtil.get(context,STOCKS_KEY,"");
        Gson gson = new Gson();
        String fromJson = gson.fromJson(stocksHashMap,new TypeToken<Map<String, List<PricePeriodResponseEntity>>>() {}.getType());
    }*/

}
