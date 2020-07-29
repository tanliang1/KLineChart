package com.xiaoxiong.flag.data;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.xiaoxiong.flag.db.FlagProvider;
import com.xiaoxiong.flag.entity.PricePeriodResponseEntity;
import com.xiaoxiong.flag.entity.StockResponseEntity;
import com.xiaoxiong.flag.ui.KLineEntity;
import com.xiaoxiong.flag.utils.LogUtil;
import com.xiaoxiong.flag.utils.SharedPreferencesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
        LinkedList<PricePeriodResponseEntity> pricePeriodResponseEntities = new LinkedList<PricePeriodResponseEntity>();
        for (int i = 0; i < 10/*stockResponseEntities.size()*/ ;i++) {
            StockResponseEntity stockResponseEntity =  DataRequest.getInstance().getTestStocks(i);
            pricePeriodResponseEntities.addAll(DataRequest.getInstance().getPricePeriodSecurities(stockResponseEntity,context)) ;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pricePeriodResponseEntities.sort(new Comparator<PricePeriodResponseEntity>() {
                @Override
                public int compare(PricePeriodResponseEntity o1, PricePeriodResponseEntity o2) {
                    return compareDate(o1.getDate(),o2.getDate()) ? -1: 1;
                }
            });
        }
        List<KLineEntity> lineEntityList = buildKlineEntities(pricePeriodResponseEntities);
        DataHelper.calculate(lineEntityList);
        insertData(lineEntityList,context);
        LogUtil.d(TAG,"initData,sStockPriceHashMap:"+sStockPriceHashMap);
    }

    private static  boolean compareDate(String beginTime ,String endTime) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date bt= null;
        try {
            bt = sdf.parse(beginTime);
            Date et=sdf.parse(endTime);
            if (bt.before(et)){
                return true;
            } else{
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;

    }

 /*   @RequiresApi(api = Build.VERSION_CODES.O)
    private List<KLineEntity> getDataFromDb(Context context) {
        String[] projections = new String[]{"index_key", "code", "date", "open", "close", "high", "lowest"};
        List<KLineEntity> kLineEntities = new ArrayList<>();
        try {
            KLineEntity entity;
            Cursor cursor = context.getContentResolver().query(FlagProvider.STOCK_PRICE_CONTENT_URI, projections, null, null);
            while (cursor != null && cursor.moveToNext()) {
                entity = new KLineEntity();
                entity.code = (cursor.getString(cursor
                        .getColumnIndex("code")));
                entity.date = (cursor.getString(cursor
                        .getColumnIndex("date")));
                entity.open = cursor.getFloat(cursor
                        .getColumnIndex("open"));
                entity.close = cursor.getFloat(cursor
                        .getColumnIndex("close"));
                entity.high = (cursor.getFloat(cursor
                        .getColumnIndex("high")));
                entity.low = (String.valueOf(cursor.getFloat(cursor
                        .getColumnIndex("lowest"))));
                kLineEntities.add(entity);
            }
        } catch (Exception ex) {

        }

        return pricePeriodResponseEntities;
    }
*/
    private static List<KLineEntity>  buildKlineEntities(List<PricePeriodResponseEntity> pricePeriodResponseEntities) {
        List<KLineEntity> list = new ArrayList();
        for(PricePeriodResponseEntity entity : pricePeriodResponseEntities) {
            list.add(entity.obtain());
        }
        return list;
    }



    public static void insertData(List<KLineEntity> entities,Context context) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try {
            for (int i = 0;i< entities.size();i++) {
                KLineEntity pricePeriodResponseEntity = entities.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("index_key",pricePeriodResponseEntity.getDate()+pricePeriodResponseEntity.code);
                contentValues.put("code",pricePeriodResponseEntity.code);
                contentValues.put("date",pricePeriodResponseEntity.getDate());
                contentValues.put("open",pricePeriodResponseEntity.open);
                contentValues.put("close",pricePeriodResponseEntity.close);
                contentValues.put("high",pricePeriodResponseEntity.high);
                contentValues.put("lowest",pricePeriodResponseEntity.low);
                contentValues.put("k",pricePeriodResponseEntity.k);
                contentValues.put("d",pricePeriodResponseEntity.d);
                contentValues.put("j",pricePeriodResponseEntity.j);
                contentValues.put("acrossType",pricePeriodResponseEntity.acrossType);
                context.getContentResolver().insert(FlagProvider.STOCK_PRICE_CONTENT_URI,contentValues);
                /*

                ops.add(ContentProviderOperation.newInsert(FlagProvider.STOCK_PRICE_CONTENT_URI).withValues(contentValues).build());
*/
            }
            /*context.getContentResolver().applyBatch(FlagProvider.AUTHORITY,ops);*/
        } catch (Exception ex) {
            LogUtil.d(TAG,"insertData,ex:"+ex);
        }
    }

 /*   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                *//*insertData(pricePeriodResponseEntities,context);*//*
            }
    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }*/

   /* @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }*/



   /* public static void saveStocksMapToSp (Context context) {
        String stocksHashMap = (String)SharedPreferencesUtil.get(context,STOCKS_KEY,"");
        Gson gson = new Gson();
        String fromJson = gson.fromJson(stocksHashMap,new TypeToken<Map<String, List<PricePeriodResponseEntity>>>() {}.getType());
    }*/

}
