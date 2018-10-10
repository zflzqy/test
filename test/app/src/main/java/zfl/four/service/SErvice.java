package zfl.four.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zfl.com.myapplication.R;

public class SErvice extends AppCompatActivity implements View.OnClickListener {
    private Button start;
    private Button stop;
    private Button bind;
    private Button unbind;
    private Button play;
    private Button pause;
    private Button pervious;
    private Button next;
    private Intent intent1;
    private Intent intent2;
    private MyIbindservice service;
    ServiceConnection connection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = ((MyIbindservice.mYbind)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_service);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start :
                intent1 = new Intent(SErvice.this,myservice.class);
                startService(intent1);
                break;
            case R.id.stop :
                stopService(intent1);
                break;
            case R.id.bind :
                intent2 = new Intent(SErvice.this,MyIbindservice.class);
                bindService(intent2,connection, Service.BIND_AUTO_CREATE);
                break;
            case R.id.unbind :
                unbindService(connection);
                break;
            case R.id.play :
                service.play();
                break;
            case R.id.pause :
                service.pause();
                break;
            case R.id.pervious :
                service.pervious();
                break;
            case R.id.next :
                service.next();
                break;
        }
    }

    private void init() {
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        bind = findViewById(R.id.bind);
        unbind = findViewById(R.id.unbind);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        pervious = findViewById(R.id.pervious);
        next = findViewById(R.id.next);


        //监听
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        bind.setOnClickListener(this);
        unbind.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        pervious.setOnClickListener(this);
        next.setOnClickListener(this);
    }
}
