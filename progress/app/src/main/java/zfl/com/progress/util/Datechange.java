package zfl.com.progress.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
* 日期转换类
* */
public class Datechange {
    public static String formatype ="yyyy-MM-dd HH:mm:ss";
    //date-->String
    public static String dateToString(Date data) {
        return new SimpleDateFormat(formatype).format(data);
    }
    //String-->date
    public static Date stringToDate(String strTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatype);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    //long-->date
    public static Date longToDate(long currentTime)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数成一个date类型的时间
        String sDateTime = dateToString(dateOld); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime); // 把String类型转换为Date类型
        return date;
    }
    //返回当前时间的字符串
    public static  String getNow(){
        Date date =new Date(System.currentTimeMillis());
        return new SimpleDateFormat(formatype).format(date);
    };
    //获取几天后的日期
    public static Date getDateAfter(Date d,int day){
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }
}
