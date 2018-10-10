package zfl.four.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/29.
 */

public class myservice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("abc", "serive_creat ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("abc", "serive_onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("abc", "serive_onDestroy: ");
        super.onDestroy();
    }
}
