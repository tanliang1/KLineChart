package flag.db;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FlagProvider extends ContentProvider {

    /** Database filename */

    /** Current database version */
    public static final String TAG = "FlagProvider";
    public static final String AUTHORITY = "flag.db.FlagProvider";
    public static final Uri STOCK_PRICE_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/price");
    private Context mContext;
    private SQLiteDatabase mDb;

    public static final int STOCK_PRICE_URI_CODE = 0;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(AUTHORITY,"price",STOCK_PRICE_URI_CODE);
    }
    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder, @Nullable CancellationSignal cancellationSignal) {
        String table = getTableName(uri);
        if (table==null) {
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }
        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String table = getTableName(uri);
        if (table==null){
            throw new IllegalArgumentException("Unsupport URI");
        }
        mDb.insert(table,null,contentValues);
        return null;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = getTableName(uri);
        if (table==null){
            throw new IllegalArgumentException("Unsupport URI");
        }
        int count = mDb.delete(table,selection,selectionArgs);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table  = getTableName(uri);
        if (table==null){
            throw new IllegalArgumentException("Unsupport URI");
        }
        int row = mDb.update(table,contentValues,selection,selectionArgs);
        return 0;
    }

    private String getTableName(Uri uri){
        String tableName=null;
        switch (sUriMatcher.match(uri)){
            case STOCK_PRICE_URI_CODE:
                tableName = DbOpenHelper.STOCK_PRICE_TABLE_NAME;
                break;
            default:
                break;
        }return tableName;
    }
}
