package zfl.four.downloadService.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zfl.four.downloadService.bean.ThreadInfo;
//该类可能报错是由于as对sql语法的强制检测，本身语法并没有错误
public class ThreadDAOImpl implements ThreadDAO {

    private DBhelper mDBhelper = null;
    private SQLiteDatabase db;
    public ThreadDAOImpl(Context context){
        mDBhelper = new DBhelper(context);
    }
    @Override
    public void insert(ThreadInfo threadInfo) {
        db = mDBhelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id ,url ,start ,stop ,finished ) values (?,?,?,?,?)",
                new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),threadInfo.getStop(),
                        threadInfo.getFinished()});
        db.close();
    }

    @Override
    public void delete(String url, int thread_id) {
        db = mDBhelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url =? and thread_id = ?",
                new String[]{url,thread_id+""});
        db.close();
    }

    @Override
    public void update(String url, int thread_id, int finished) {
        db = mDBhelper.getWritableDatabase();
        db.execSQL("update thread_info set finished =? where url = ? and thread_id = ?",
                new String[]{finished+"",url,thread_id+""});
        db.close();
    }

    @Override
    public List<ThreadInfo> query(String url) {
        db = mDBhelper.getWritableDatabase();
        List<ThreadInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from thread_info where url =?",new String[]{url});
        while (cursor.moveToNext()){
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setStop(cursor.getInt(cursor.getColumnIndex("stop")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String url, int thread) {
        db = mDBhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url =?",new String[]{url});
        boolean isExists = cursor.moveToNext();
        cursor.close();
        db.close();
        return isExists;
    }
}
