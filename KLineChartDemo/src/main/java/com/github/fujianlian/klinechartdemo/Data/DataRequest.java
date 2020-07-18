package com.github.fujianlian.klinechartdemo.Data;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.net.entity.AllSecuritiesEntity;
import com.github.fujianlian.klinechart.net.entity.PricePeriodRequestEntity;
import com.github.fujianlian.klinechart.net.entity.PricePeriodResponseEntity;
import com.github.fujianlian.klinechart.net.entity.SecuritiesEntity;
import com.github.fujianlian.klinechart.net.OkHttpUtils;
import com.github.fujianlian.klinechart.net.entity.StockResponseEntity;
import com.github.fujianlian.klinechart.net.entity.TokenEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模拟网络请求
 * Created by tifezh on 2017/7/3.
 */

public class DataRequest {
    private static List<KLineEntity> datas = null;
    private static String TAG = "DataRequest";
    private static DataRequest mInstance;
    private static int SECURITIES_PARAMETERS_SIZE = 6;
    private static int STOCK_PRICE_PARAMETERS_SIZE = 12;

    public static DataRequest getInstance() {
        if (mInstance == null) {
            synchronized (DataRequest.class){
                if (mInstance == null) {
                    mInstance = new DataRequest();
                }
            }
        }
        return mInstance;

    }


    public  String getStringFromAssert(Context context, String fileName) {
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
    public  List<KLineEntity> getALL(Context context) {
        StockResponseEntity stockResponseEntity = new StockResponseEntity();
        stockResponseEntity.setCode("000008.XSHE");
        getPricePeriodSecurities(stockResponseEntity);
        if (datas == null) {
            final List<KLineEntity> data = new Gson().fromJson(getStringFromAssert(context, "ibm.json"), new TypeToken<List<KLineEntity>>() {
            }.getType());

            DataHelper.calculate(data);
            datas = data;
        }
        return datas;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getToken() {
        Gson gson = new Gson();
        String body = gson.toJson(new TokenEntity());
        try {
            String result =  OkHttpUtils.getInstance().post(OkHttpUtils.url,body);
            Log.d(TAG,"getToken:" + result);
            return  result;
        } catch (Exception ex) {
            Log.d(TAG,"IOException:" + ex);
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  List<StockResponseEntity> getAllSecurities() {
        List<StockResponseEntity> securitiesEntities = new ArrayList<>();
        Gson gson = new Gson();
        AllSecuritiesEntity getSecuritiesBody = new AllSecuritiesEntity();
        getSecuritiesBody.setToken(getToken());
        String body = gson.toJson(getSecuritiesBody);
        try {
            String result = OkHttpUtils.getInstance().post(OkHttpUtils.url, body);
            String[] results = splitResult(result);
            StockResponseEntity securitiesEntity = null;
            for (int i = SECURITIES_PARAMETERS_SIZE;i<results.length-SECURITIES_PARAMETERS_SIZE;i++) {
                int yushu = i % SECURITIES_PARAMETERS_SIZE;
                if (yushu == 0) {
                    securitiesEntity = new StockResponseEntity();
                }
                if (securitiesEntity != null) {
                    switch (i % SECURITIES_PARAMETERS_SIZE) {
                        case 0:
                            securitiesEntity.setCode(results[i]);
                            break;
                        case 1:
                            securitiesEntity.setDisplay_name(results[i]);
                            break;
                        case 2:
                            securitiesEntity.setName(results[i]);
                            break;
                        case 3:
                            securitiesEntity.setStart_date(results[i]);
                            break;
                        case 4:
                            securitiesEntity.setEnd_date(results[i]);
                            break;
                        case 5:
                            securitiesEntity.setType(results[i]);
                            break;
                    }
                    if (yushu == SECURITIES_PARAMETERS_SIZE -1) {
                        securitiesEntities.add(securitiesEntity);
                    }
                }
            }
        } catch (Exception ex) {
            Log.d(TAG,"IOException:" + ex);
        }
        Log.d(TAG,"getAllSecurities,securitiesEntities:" + securitiesEntities.size());
        return securitiesEntities;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  List<PricePeriodResponseEntity> getPricePeriodSecurities(StockResponseEntity stockResponseEntity) {
        List<PricePeriodResponseEntity> pricePeriodResponseEntities = new ArrayList<>();
        Gson gson = new Gson();
        PricePeriodRequestEntity pricePeriodRequestEntity = new PricePeriodRequestEntity();
        pricePeriodRequestEntity.setToken(getToken());
        pricePeriodRequestEntity.setDate(stockResponseEntity.getStart_date());
        pricePeriodRequestEntity.setCode(stockResponseEntity.getCode());
        pricePeriodRequestEntity.setEnd_date(getCurFormatDate());
        String body = gson.toJson(pricePeriodRequestEntity);
        try {
            String result = OkHttpUtils.getInstance().post(OkHttpUtils.url, body);
            String[] results = splitResult(result);
            PricePeriodResponseEntity securitiesEntity = null;
            for (int i = STOCK_PRICE_PARAMETERS_SIZE;i<results.length-STOCK_PRICE_PARAMETERS_SIZE;i++) {
                int yushu = i % STOCK_PRICE_PARAMETERS_SIZE;
                if (yushu == 0) {
                    securitiesEntity = new PricePeriodResponseEntity();
                }
                if (securitiesEntity != null) {
                    switch (i % STOCK_PRICE_PARAMETERS_SIZE) {
                        case 0:
                            securitiesEntity.setDate(results[i]);
                            break;
                        case 1:
                            securitiesEntity.setOpen(results[i]);
                            break;
                        case 2:
                            securitiesEntity.setClose(results[i]);
                            break;
                        case 3:
                            securitiesEntity.setHigh(results[i]);
                            break;
                        case 4:
                            securitiesEntity.setLow(results[i]);
                            break;
                        case 5:
                            securitiesEntity.setVolume(results[i]);
                            break;
                        case 6:
                            securitiesEntity.setMoney(results[i]);
                            break;
                        case 7:
                            securitiesEntity.setPaused(results[i]);
                            break;
                        case 8:
                            securitiesEntity.setHigh_limit(results[i]);
                            break;
                        case 9:
                            securitiesEntity.setLow_limit(results[i]);
                            break;
                        case 10:
                            securitiesEntity.setAvg(results[i]);
                            break;
                        case 11:
                            securitiesEntity.setPre_close(results[i]);
                            break;
                    }
                    if (yushu == STOCK_PRICE_PARAMETERS_SIZE -1) {
                        pricePeriodResponseEntities.add(securitiesEntity);
                    }
                }
            }
            Log.d(TAG,"getPricePeriodSecurities,result:" + pricePeriodResponseEntities);
        } catch (Exception ex) {
            Log.d(TAG,"getPricePeriodSecurities,IOException:" + ex);
        }
        return pricePeriodResponseEntities;
    }

    private String[] splitResult(String result) {
        Pattern p = Pattern.compile("\n");
        Matcher m = p.matcher(result);
        result = m.replaceAll(",");
        String[] results = result.split(",");
        return results;
    }

    private void buildResultList() {

    }


    /**
     * 分页查询
     *
     * @param context
     * @param offset  开始的索引
     * @param size    每次查询的条数
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  List<KLineEntity> getAllSecurities(Context context, int offset, int size) {
        List<KLineEntity> all = getALL(context);
        List<KLineEntity> data = new ArrayList<>();
        int start = Math.max(0, all.size() - 1 - offset - size);
        int stop = Math.min(all.size(), all.size() - offset);
        for (int i = start; i < stop; i++) {
            data.add(all.get(i));
        }
        return data;
    }

    private String getCurFormatDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

}


