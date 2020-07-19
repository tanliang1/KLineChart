package com.xiaoxiong.flag.utils;

import android.util.Log;

public class LogUtil {
    private  static String TAG_PREXI = "com.xiaoxion.flag.";
    private  static  boolean sLogSwitch = true;
    public static void v(String tag, String msg){
        if (sLogSwitch) {
            Log.v(TAG_PREXI+tag,msg);
        }
    }

    public static void d(String tag, String msg){
        if (sLogSwitch) {
            Log.d(TAG_PREXI+tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if (sLogSwitch) {
            Log.i(TAG_PREXI+tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if (sLogSwitch) {
            Log.w(TAG_PREXI+tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if (sLogSwitch) {
            Log.e(TAG_PREXI+tag,msg);
        }
    }
}
