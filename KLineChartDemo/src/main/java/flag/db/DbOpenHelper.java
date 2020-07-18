package flag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "flag.db";
    public static final String STOCK_PRICE_TABLE_NAME = "price";

    private static final int DB_VERSION = 1;
    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS "+ STOCK_PRICE_TABLE_NAME + "(date string primary key,code varchar(30)," +
            "open Float,close Float,high Float,lowest Float,k Float,d  Float,j Float)";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}