package com.xiaoxiong.flag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaoxiong.flag.utils.LogUtil;


public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbOpenHelper";
    private static final String DB_NAME = "com.xiaoxion.flag.db";
    public static final String STOCK_PRICE_TABLE_NAME = "price";
    public static final String STOCKS_TABLE_NAME = "stocks";

    private static final int DB_VERSION = 1;
    private String CREATE_PRICE_TABLE = "CREATE TABLE IF NOT EXISTS "+ STOCK_PRICE_TABLE_NAME + "(index_key string primary key,code varchar(30)," +
            "open Float,close Float,high Float,lowest Float,k Float,d  Float,j Float)";
    private String CREATE_STOCKS_TABLE = "CREATE TABLE IF NOT EXISTS "+ STOCKS_TABLE_NAME + "(code string primary key,code varchar(30)," +
            "open Float,close Float,high Float,lowest Float,k Float,d  Float,j Float)";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRICE_TABLE);
        LogUtil.d(TAG,"onCreate,CREATE_BOOK_TABLE"+ CREATE_PRICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}