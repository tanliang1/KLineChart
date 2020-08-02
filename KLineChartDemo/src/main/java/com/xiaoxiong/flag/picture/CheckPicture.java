package com.xiaoxiong.flag.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.xiaoxiong.flag.R;
import com.xiaoxiong.flag.net.OkHttpUtils;
import com.xiaoxiong.flag.utils.LogUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.xiaoxiong.flag.data.DataHelper.ACROSS_TYPE_LIVE;

public class CheckPicture {
    private static CheckPicture mInstance;
    private static Context mContext;

    private static int COLOR_K_BLACK = 0;
    private static int COLOR_D_YELLOW = 1;
    private static int COLOR_J_RED = 2;
    private LinkedHashMap<String, KlinePoint> pointsHashMap = new LinkedHashMap<>();


    public static CheckPicture getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CheckPicture.class){
                if (mInstance == null) {
                    mContext = context;
                    mInstance = new CheckPicture();
                }
            }
        }
        return mInstance;

    }

    public void jiexi() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.line2);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int [][] datas = new int[w][h];
        LogUtil.d("CheckPicture","w=:"+w+" h=:"+h);
        for (int x = 0;x < w;x++) {
            KlinePoint klinePoint = new KlinePoint();
            klinePoint.setX(x);
            for (int y = 0;y < h;y++) {
                int color = bitmap.getPixel(x,y);
                 int alpah = (color & 0xff000000)>> 24;
                int r = (color & 0x00ff0000)>> 16;
                int g = (color & 0x0000ff00)>> 8;
                int b = (color & 0x000000ff);
                if (r< 240  &&  g < 240 && b< 240) {
                    LogUtil.d("CheckPicture","color=:"+color+" x=:"+x+ " y:" +y + " alpah:"+alpah +
                            " r:"+r+ " g:"+g + " b:"+b);
                    if (r == g &&  g== b ) {
                        klinePoint.setK((y/(float)h)*100);
                        klinePoint.setType(COLOR_K_BLACK);
                        pointsHashMap.put(String.valueOf(x),klinePoint);
                    } else if (r < 238 & r> 234) {
                        klinePoint.setD((y/(float)h)*100);
                        klinePoint.setType(COLOR_D_YELLOW);
                        pointsHashMap.put(String.valueOf(x),klinePoint);
                    } else if ((r < 169 & r> 165) || (r < 202 & r> 198)){
                        klinePoint.setJ((y/(float)h)*100);
                        klinePoint.setType(COLOR_J_RED);
                    }
                }
            }
            pointsHashMap.put(String.valueOf(x),klinePoint);
        }

        for (Map.Entry<String, KlinePoint> entry : pointsHashMap.entrySet()) {
            KlinePoint klinePoint =  entry.getValue();
            float diff = klinePoint.getD() - klinePoint.getK();
            if ( diff > 0 && diff < 2) {
                klinePoint.setAcrossType(ACROSS_TYPE_LIVE);
                LogUtil.d("CheckPicture","jincha x=:"+klinePoint.getX());
            }
        }
    }

}
