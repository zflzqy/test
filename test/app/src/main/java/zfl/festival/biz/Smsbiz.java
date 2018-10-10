package zfl.festival.biz;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import zfl.festival.Bean.sendMessage;
import zfl.festival.Database.Smscontentprovider;

public class Smsbiz {
    private Context mCotext;

    public Smsbiz(Context mCotext) {
        this.mCotext = mCotext;
    }

    public int sendMesssage(String number, String msg, PendingIntent sentPi, PendingIntent deliverPi){
        //发送短信
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(msg);
        for (String content : contents) {
            if (msg.length() < 100) {
                smsManager.sendTextMessage(number, null, content, sentPi, deliverPi);
                return 1;//内容小于100 不进行分割
            }
            smsManager.sendTextMessage(number, null, content, sentPi, deliverPi);
        }
        return contents.size();
    }
    public int sendMesssage(Set<String> numbers, sendMessage msg, PendingIntent sentPi, PendingIntent deliverPi){
        //发送短信的数量，并保存记录到数据库
        saveMsg(msg);
        int result = 0;
        for (String number:numbers){
            int count =sendMesssage(number,msg.getMsg(),sentPi,deliverPi);
            result+=count;
        }
        return result;
    }
    //保存发送的短信
    private void saveMsg(sendMessage sm){
        sm.setDate(new Date());
        ContentValues values =new ContentValues();
        values.put("id",sm.getId());
        values.put("date",sm.getDate().getTime());
        values.put("msg",sm.getMsg());
        values.put("festName",sm.getFestName());
        values.put("names",sm.getNames());
        values.put("numbers",sm.getNumbers());
        mCotext.getContentResolver().insert(Smscontentprovider.URI_SMS_ALL,values);
    }


}
