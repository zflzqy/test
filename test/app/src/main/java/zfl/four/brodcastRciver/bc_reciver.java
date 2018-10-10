package zfl.four.brodcastRciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/29.
 */

public class bc_reciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //普通广播
       // String name = intent.getStringExtra("xyz");
        //Log.i("abc", "接收到了 ");

        //截断广播
       // abortBroadcast();

        //有序广播
        String name2 = intent.getStringExtra("xyz2");
        name2 = "处理的广播信息";
       Log.i("abc", "接收到了 "+name2);

    }
}
