package com.xiaoxiong.flag.data;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiong.flag.db.FlagProvider;
import com.xiaoxiong.flag.entity.PricePeriodResponseEntity;
import com.xiaoxiong.flag.entity.StockResponseEntity;
import com.xiaoxiong.flag.ui.KLineEntity;
import com.xiaoxiong.flag.utils.LogUtil;
import com.xiaoxiong.flag.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOperationHelper {
    private final static String TAG = "DbOperationHelper";
    private static HashMap<String,List<PricePeriodResponseEntity>> sStockPriceHashMap = new HashMap<>();

    private static HashMap<String,List<KLineEntity>> sKlineMap = new HashMap<>();

    private final static String STOCKS_KEY = "STOCKS_KEY";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void initData(Context context) {
        List<StockResponseEntity> stockResponseEntities =  DataRequest.getInstance().getAllSecurities();
        for (int i = 0; i < stockResponseEntities.size() ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);
            if (sStockPriceHashMap.get(stockResponseEntity.getCode()) == null) {
                List<PricePeriodResponseEntity> pricePeriodResponseEntities = DataRequest.getInstance().getPricePeriodSecurities(stockResponseEntity);
                sStockPriceHashMap.put(stockResponseEntity.getCode(),pricePeriodResponseEntities);
            }
        }
        LogUtil.d(TAG,"initData,sStockPriceHashMap:"+sStockPriceHashMap);
    }

    public static void insertData(List<PricePeriodResponseEntity> entities,Context context) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try {
            for (int i = 0;i< entities.size();i++) {
                PricePeriodResponseEntity pricePeriodResponseEntity = entities.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("index_key",pricePeriodResponseEntity.getDate()+pricePeriodResponseEntity.getCode());
                contentValues.put("date",pricePeriodResponseEntity.getDate());
                contentValues.put("code",pricePeriodResponseEntity.getCode());
                contentValues.put("open",Float.valueOf(pricePeriodResponseEntity.getOpen()));
                contentValues.put("close",Float.valueOf(pricePeriodResponseEntity.getClose()));
                contentValues.put("high",Float.valueOf(pricePeriodResponseEntity.getHigh()== null ? "" : pricePeriodResponseEntity.getHigh()));
                contentValues.put("lowest",Float.valueOf(pricePeriodResponseEntity.getLow()== null ? "" : pricePeriodResponseEntity.getHigh()));
                ops.add(ContentProviderOperation.newInsert(FlagProvider.STOCK_PRICE_CONTENT_URI).withValues(contentValues).build());
            }
            context.getContentResolver().applyBatch(FlagProvider.AUTHORITY,ops);
        } catch (Exception ex) {
            LogUtil.d(TAG,"insertData,ex:"+ex);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void saveData (Context context) {
        List<StockResponseEntity> stockResponseEntities =  DataRequest.getInstance().getAllSecurities();
        for (int i = 0; i < stockResponseEntities.size() ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);

                List<PricePeriodResponseEntity> pricePeriodResponseEntities = sStockPriceHashMap.get(stockResponseEntity.getCode());
                List<KLineEntity> sKlineEntities = new ArrayList<>();
                  for (int j=0;j<pricePeriodResponseEntities.size();j++) {
                      PricePeriodResponseEntity pricePeriodResponseEntity = pricePeriodResponseEntities.get(j);
                      KLineEntity kLineEntity = new KLineEntity();
                      kLineEntity.Date = pricePeriodResponseEntity.getDate();
                      kLineEntity.Open = Float.valueOf(pricePeriodResponseEntity.getOpen());
                      kLineEntity.Close = Float.valueOf(pricePeriodResponseEntity.getClose());
                      kLineEntity.High = Float.valueOf(pricePeriodResponseEntity.getHigh());
                      kLineEntity.Low = Float.valueOf(pricePeriodResponseEntity.getLow());
                      kLineEntity.code = pricePeriodResponseEntity.getCode();
                      sKlineEntities.add(kLineEntity);
                  }
                sKlineMap.put(stockResponseEntity.getCode(),sKlineEntities);
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void calculateDate() {
        List<StockResponseEntity> stockResponseEntities =  DataRequest.getInstance().getAllSecurities();
        for (int i = 0; i < stockResponseEntities.size() ;i++) {
            StockResponseEntity stockResponseEntity = stockResponseEntities.get(i);
            List<KLineEntity> sKlineEntities = sKlineMap.get(stockResponseEntity.getCode());
            DataHelper.calculate(sKlineEntities);
        }
    }

    public static void getStocksMapFromSp (Context context) {
        String stocksHashMap = (String)SharedPreferencesUtil.get(context,STOCKS_KEY,"");
        Gson gson = new Gson();
         String fromJson = gson.fromJson(stocksHashMap,new TypeToken<Map<String, List<PricePeriodResponseEntity>>>() {}.getType());
    }

}
