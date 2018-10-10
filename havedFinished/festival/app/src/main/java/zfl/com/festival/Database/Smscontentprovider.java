package zfl.com.festival.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Smscontentprovider extends ContentProvider {
    private static final String AUTHORITY="zfl.com.sms.provider.Smscontentprovider";
    public static final Uri URI_SMS_ALL=Uri.parse("content://"+AUTHORITY+"/sms");
    public static final Uri URI_SMS_ONE=Uri.parse("content://"+AUTHORITY+"/sms/2");//单条短信记录

    private static UriMatcher mAtcher;

    private static final int SMS_ALL =0;//所有短信
    private static final int SMS_ONE =1;//单个短信
    //初始化mAtcher
    static {
        mAtcher = new UriMatcher(UriMatcher.NO_MATCH);
        mAtcher.addURI(AUTHORITY,"sms",SMS_ALL);
        mAtcher.addURI(AUTHORITY,"sms/#",SMS_ONE);
    }
    //数据库操作对象
    private  SmsSQLHelper mSmsSQLHelper;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mSmsSQLHelper =new  SmsSQLHelper(getContext());//初始化数据库对象
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       int match =mAtcher.match(uri);
       switch (match)
       {
           case SMS_ALL:
               break;
           case SMS_ONE:
               long id = ContentUris.parseId(uri);
               selection ="_id =?";
               selectionArgs = new String[]{String.valueOf(id)};
               break;
           default:throw new IllegalArgumentException("wrong ui"+uri);
       }
       mDb = mSmsSQLHelper.getReadableDatabase();
       Cursor cursor = mDb.query("sms",projection,selection,selectionArgs,null,null,sortOrder);
       cursor.setNotificationUri(getContext().getContentResolver(),URI_SMS_ALL);
       return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = mAtcher.match(uri);
        if (match!=SMS_ALL) {
//            throw new IllegalArgumentException();//如果没有就返回uri,,,,,该条语句会报错
        }
        mDb = mSmsSQLHelper.getReadableDatabase();
        long rowId = mDb.insert("sms", null, values);
        if (rowId>0){
            notifyDataChangeed();//数据改变通知
            return ContentUris.withAppendedId(uri,rowId);//返回改变
        }
        return null;
    }

    private void notifyDataChangeed()
    {
    getContext().getContentResolver().notifyChange(URI_SMS_ALL,null);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mAtcher.match(uri);
        mDb =mSmsSQLHelper.getWritableDatabase();
        long rowId = 0;
        switch (match){
            case SMS_ALL:
                break;
            case SMS_ONE:
                //删除单条短信记录
             rowId= mDb.delete("sms",selection,selectionArgs);
            if (rowId>0){
                notifyDataChangeed();
            }
            break;
        }
        return (int) rowId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
