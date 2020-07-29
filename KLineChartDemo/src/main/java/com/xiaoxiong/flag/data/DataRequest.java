package com.xiaoxiong.flag.data;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiong.flag.entity.AllSecuritiesEntity;
import com.xiaoxiong.flag.entity.PricePeriodRequestEntity;
import com.xiaoxiong.flag.entity.PricePeriodResponseEntity;
import com.xiaoxiong.flag.entity.StockResponseEntity;
import com.xiaoxiong.flag.entity.TokenEntity;
import com.xiaoxiong.flag.net.OkHttpUtils;
import com.xiaoxiong.flag.ui.KLineEntity;
import com.xiaoxiong.flag.utils.DateUtil;
import com.xiaoxiong.flag.utils.SharedPreferencesUtil;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaoxiong.flag.data.DataOperationHelper.ALL_SECURITIES_KEY;


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
    private static List<StockResponseEntity> sSecuritiesEntities = new ArrayList<>();
    private static List<PricePeriodResponseEntity> sPricePeriodResponseEntities = new ArrayList<>();
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

    public StockResponseEntity getTestStocks(int segment) {
        StockResponseEntity stockResponseEntity = new StockResponseEntity();
        stockResponseEntity.setCode("000004.XSHE");
        stockResponseEntity.setStart_date((1991+segment*4)+"-04-03");
        stockResponseEntity.setEnd_date((1991+(segment+1)*4)+"-04-03");
        return stockResponseEntity;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  List<StockResponseEntity> getAllSecurities(Context context) {
        List<StockResponseEntity> getAllSecurities = SpDataHelper.getAllSecuritiesFromSp(context);
        if (getAllSecurities != null) {
            return getAllSecurities;
        }
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
        sSecuritiesEntities.addAll(securitiesEntities);
        return securitiesEntities;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public   List<PricePeriodResponseEntity> getPricePeriodSecurities(StockResponseEntity stockResponseEntity,Context context) {
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
            PricePeriodResponseEntity pricePeriodResponseEntity = null;
            for (int i = STOCK_PRICE_PARAMETERS_SIZE;i<results.length-STOCK_PRICE_PARAMETERS_SIZE;i++) {
                int yushu = i % STOCK_PRICE_PARAMETERS_SIZE;
                if (yushu == 0) {
                    pricePeriodResponseEntity = new PricePeriodResponseEntity();
                }
                if (pricePeriodResponseEntity != null) {
                    switch (i % STOCK_PRICE_PARAMETERS_SIZE) {
                        case 0:
                            pricePeriodResponseEntity.setDate(results[i]);
                            break;
                        case 1:
                            pricePeriodResponseEntity.setOpen(results[i]);
                            break;
                        case 2:
                            pricePeriodResponseEntity.setClose(results[i]);
                            break;
                        case 3:
                            pricePeriodResponseEntity.setHigh(results[i]);
                            break;
                        case 4:
                            pricePeriodResponseEntity.setLow(results[i]);
                            break;
                        case 5:
                            pricePeriodResponseEntity.setVolume(results[i]);
                            break;
                        case 6:
                            pricePeriodResponseEntity.setMoney(results[i]);
                            break;
                        case 7:
                            pricePeriodResponseEntity.setPaused(results[i]);
                            break;
                        case 8:
                            pricePeriodResponseEntity.setHigh_limit(results[i]);
                            break;
                        case 9:
                            pricePeriodResponseEntity.setLow_limit(results[i]);
                            break;
                        case 10:
                            pricePeriodResponseEntity.setAvg(results[i]);
                            break;
                        case 11:
                            pricePeriodResponseEntity.setPre_close(results[i]);
                            break;
                    }
                    if (yushu == STOCK_PRICE_PARAMETERS_SIZE -1) {
                        pricePeriodResponseEntity.setCode(stockResponseEntity.getCode());
                        pricePeriodResponseEntities.add(pricePeriodResponseEntity);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }
}


