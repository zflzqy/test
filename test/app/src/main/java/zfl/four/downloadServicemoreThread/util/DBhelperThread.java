package zfl.four.downloadServicemoreThread.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBhelperThread extends SQLiteOpenHelper {
    private static final String DB_NAME = "downloadThread.db";
    private static final int VERSION = 1;
    private static DBhelperThread sDBhelper;//静态DBhelperT，防止多个线程操作
    private DBhelperThread(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE thread_info(_id integer primary key autoincrement,thread_id integer," +
                "url text,start integer,stop integer,finished integer)");
    }
    //获取类的对象,静态，对象只实例化一次
    public static DBhelperThread getInstance(Context context){
        if (sDBhelper ==null){
            sDBhelper = new DBhelperThread(context);
        }
        return  sDBhelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists thread_info");
        db.execSQL("CREATE TABLE thread_info(_id integer primary key autoincrement,thread_id integer," +
                "url text,start integer,stop integer,finished integer)");
    }
}
