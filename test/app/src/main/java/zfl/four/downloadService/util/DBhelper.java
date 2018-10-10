package zfl.four.downloadService.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBhelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "download.db";
    private static final int VERSION = 1;
    public DBhelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE thread_info(_id integer primary key autoincrement,thread_id integer," +
                "url text,start integer,stop integer,finished integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists thread_info");
        db.execSQL("CREATE TABLE thread_info(_id integer primary key autoincrement,thread_id integer," +
                "url text,start integer,stop integer,finished integer)");
    }
}
