package zfl.festival.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmsSQLHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sms.db";
    private static final int VERSION = 1;
    public SmsSQLHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       //创建短信记录表
        db.execSQL("create table if not exists sms(_id integer primary key autoincrement," +
                "id integer,date integer,msg text,festName text,names text,numbers)");
        //创建短信表
        db.execSQL("create table if not exists msg(_id integer primary key autoincrement," +
                "msgId integer,date integer,msg text,festName text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新短信记录
        db.execSQL("drop table if exists sms");
        db.execSQL("create table if not exists sms(_id integer primary key autoincrement," +
                "id integer,date integer,msg text,festName text,names text,numbers)");
        //更新短信
        db.execSQL("drop table if exists msg");
        db.execSQL("create table if not exists msg(_id integer primary key autoincrement," +
                "msgId integer,date integer,msg text,festName text)");
    }
}
