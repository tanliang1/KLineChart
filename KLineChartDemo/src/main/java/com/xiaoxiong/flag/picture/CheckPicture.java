package com.xiaoxiong.flag.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xiaoxiong.flag.R;
import com.xiaoxiong.flag.net.OkHttpUtils;
import com.xiaoxiong.flag.utils.LogUtil;

public class CheckPicture {
    private static CheckPicture mInstance;
    private static Context mContext;


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
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.line);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int [][] datas = new int[w][h];

        for (int i = 0;i < w;i++) {
            for (int j = 0;j < h;j++) {
                if (bitmap.getPixel(i,j) < (-14802646+1000) && bitmap.getPixel(i,j) < (-14802646-1000)) {
                    LogUtil.d("CheckPicture","i:"+i+ " j:" +j + " value:"+bitmap.getPixel(i,j));

                }
            }
        }
    }

}
