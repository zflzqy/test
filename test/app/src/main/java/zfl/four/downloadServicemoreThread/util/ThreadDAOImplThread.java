package zfl.four.downloadServicemoreThread.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zfl.four.downloadServicemoreThread.bean.ThreadInfo;

//该类可能报错是由于as对sql语法的强制检测，本身语法并没有错误
public class ThreadDAOImplThread implements ThreadDAOThread {

    private DBhelperThread mDBhelperThread = null;
    private SQLiteDatabase db;
    public ThreadDAOImplThread(Context context){
        mDBhelperThread = DBhelperThread.getInstance(context);
    }
    //添加synchronized防止多个线程对同个数据库执行同样操作造成死锁,synchronize会牺牲一部分性能
    @Override
    public synchronized void insert(ThreadInfo threadInfo) {
        db = mDBhelperThread.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id ,url ,start ,stop ,finished ) values (?,?,?,?,?)",
                new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),threadInfo.getStop(),
                        threadInfo.getFinished()});
        db.close();
    }

    @Override
    public synchronized void delete(String url) {
        db = mDBhelperThread.getWritableDatabase();
        db.execSQL("delete from thread_info where url =? ",
                new String[]{url});
        db.close();
    }

    @Override
    public synchronized void update(String url, int thread_id, int finished) {
        db = mDBhelperThread.getWritableDatabase();
        db.execSQL("update thread_info set finished =? where url = ? and thread_id = ?",
                new String[]{finished+"",url,thread_id+""});
        db.close();
    }
    //查询操作不用synchronized，只是查询，没有操作
    @Override
    public List<ThreadInfo> query(String url) {
        db = mDBhelperThread.getReadableDatabase();
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
        db = mDBhelperThread.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url =?",new String[]{url});
        boolean isExists = cursor.moveToNext();
        cursor.close();
        db.close();
        return isExists;
    }
}
