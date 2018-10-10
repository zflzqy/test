package zfl.com.book.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import zfl.com.book.R;

public class broadcastRecvier extends AppCompatActivity {
    private recevie mRecevie;
    private localRecevie mLocalRecevie;//本地广播

    private Button mLocalbroadcast;
    private LocalBroadcastManager lbm;

    private IntentFilter intentFilter;
    private IntentFilter mLocalintentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_broadcast_recvier);
        mLocalbroadcast =findViewById(R.id.send_localbroadcast);
        lbm = LocalBroadcastManager.getInstance(this);//当前程序发送广播
        mLocalbroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("zfl.com.book.function.loaclbroadcast");
                lbm.sendBroadcast(intent);
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mRecevie = new recevie();
        registerReceiver(mRecevie,intentFilter);

        mLocalintentFilter = new IntentFilter();
        mLocalintentFilter.addAction("zfl.com.book.function.loaclbroadcast");
        mLocalRecevie = new localRecevie();
        lbm.registerReceiver(mLocalRecevie,mLocalintentFilter);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRecevie);
        lbm.unregisterReceiver(mLocalRecevie);
    }
    class localRecevie extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"接收到了本地广播",Toast.LENGTH_SHORT).show();
        }
    }
    class  recevie extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni!=null&&ni.isAvailable()){
            Toast.makeText(context,"网络打开",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"网络关闭",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
