package zfl.four.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/29.
 */

public class MyIbindservice extends Service  {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("abc", "seriveIbind_creat ");
    }


    public class mYbind  extends Binder{
        public MyIbindservice getService(){
            return MyIbindservice.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new mYbind();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.i("abc", "seriveIbind_unbindService ");
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        Log.i("abc", "seriveIbind_onDestroy ");
        super.onDestroy();
    }
    public void  play(){
        Log.i("abc", "播放 ");
    }
    public void  pause(){
        Log.i("abc", "暂停");
    }
    public void  pervious(){
        Log.i("abc", "上一首");
    }
    public void  next(){
        Log.i("abc", "下一首");
    }
}
