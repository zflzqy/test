package zfl.four.brodcastRciver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import zfl.com.myapplication.R;

/**
 * Created by Administrator on 2018/3/29.
 */

public class manActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_brodcast;
    private Button btn_brodcast2;
    public bc_reciver reciver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_brodcase);
        btn_brodcast = findViewById(R.id.btn_brodcast);
        btn_brodcast2 = findViewById(R.id.btn_brodcast2);
        btn_brodcast.setOnClickListener(this);
        btn_brodcast2.setOnClickListener(this);

        //动态注册
//        bc_reciver reciver =new bc_reciver();
//        IntentFilter intentFilter = new IntentFilter("brodcast");
//        registerReceiver(reciver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_brodcast:
                Intent intent = new Intent();
                intent.putExtra("xyz","这是一条普通信息");
                intent.setAction("brodcast");
                sendBroadcast(intent);
            break;
            case  R.id.btn_brodcast2 :
                Intent intent1 = new Intent();
                intent1.putExtra("xyz2","这是一条有序广播");
                intent1.setAction("broadcast2");
                sendOrderedBroadcast(intent1,null);
                break;
        }
    }
}
