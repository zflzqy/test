package zfl.festival.Database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zfl.festival.Bean.festival;

public class SmsDAOImpl implements SmsDAO {
    private SmsSQLHelper mSmsSQLHlper =null;
    private SQLiteDatabase db;
    private Context mContext;

    public SmsDAOImpl(Context mContext) {
        mSmsSQLHlper = new SmsSQLHelper(mContext);
    }
    //增加短信记录
    @Override
    public synchronized void insert(festival festival) {
        db = mSmsSQLHlper.getWritableDatabase();
        db.execSQL("insert into msg(msgId,date,msg,festName) values (?,?,?,?)",
                new Object[]{festival.getMsgId(),festival.getDate().getTime(),festival.getMsg(),festival.getFestName()});
        db.close();
    }
    //删除短信记录
    @Override
    public synchronized void delete(String festName, int msgId) {
        db =mSmsSQLHlper.getWritableDatabase();
        db.execSQL("delete from msg where msgId =? and festName=?",new String[]{msgId+"",festName});
        db.close();
    }
    //修改短信记录
    @Override
    public synchronized void update(festival festival) {
        db = mSmsSQLHlper.getWritableDatabase();
        db.execSQL("update  msg set msg=? and date =? where msgId=? and festName =?",
                new String[]{festival.getMsgId()+"",festival.getDate().getTime()+""});
    }
    //查询短信记录
    @Override
    public List<festival> query(String festName) {
        List<festival> festivals = new ArrayList<>();
        db =mSmsSQLHlper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from msg where festName=?",
                new String[]{festName});
//        cursor.moveToFirst();//取第一条数据
        while (cursor.moveToNext()){
            festival festival = new festival();
            festival.setMsgId(cursor.getInt(cursor.getColumnIndex("msgId")));
            festival.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
            festival.setFestName(cursor.getString(cursor.getColumnIndex("festName")));

            long ldate =(cursor.getInt(cursor.getColumnIndex("date")));
            festival.setDate(parseDate(ldate));

            festivals.add(festival);
        }
        cursor.close();
        db.close();
        return festivals;
    }
    //查找单条短信
    @Override
    public synchronized festival queryOne(String festName, int msgId) {
        db =mSmsSQLHlper.getReadableDatabase();
        festival  festival = new festival();
        Cursor cursor = db.rawQuery("select * from msg where festName=? and msgId = ?",
                new String[]{festName,msgId+""});
//        cursor.moveToFirst();
        while (cursor.moveToNext()){
            festival.setMsgId(cursor.getInt(cursor.getColumnIndex("msgId")));
            festival.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
            festival.setFestName(cursor.getString(cursor.getColumnIndex("festName")));
            long ldate =(cursor.getInt(cursor.getColumnIndex("date")));
            festival.setDate(parseDate(ldate));
        }
        cursor.close();
        db.close();
        return festival;
    }

    //日期格式
    private Date parseDate(long date) {
        Date rDate = new Date(date);
        //先转换成String类型
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sDate =df.format(rDate);
        //把String转换成date
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date newDate = null;
        try {
            newDate =sdf.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
}
