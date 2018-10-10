package zfl.android_one.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zfl.com.myapplication.R;

public class mainTsendziThread extends AppCompatActivity implements View.OnClickListener{
    private Button button1;
    private Button button2;
    private Handler zhuHandler;
    private Handler ziHandler;
    private HandlerThread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_tsendzi_thread);
        init();
        thread.start();
        ziHandler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                zhuHandler.sendMessageDelayed(message,1000);
                System.out.println("往主线程发送"+Thread.currentThread());
            }
        };
    }

    private void init() {
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        thread = new HandlerThread("hi");

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        zhuHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                ziHandler.sendMessageDelayed(message,1000);
                System.out.println("往子线程发送"+Thread.currentThread());
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                zhuHandler.sendEmptyMessage(1);
                break;
            case  R.id.button2:
                //无法停止
                zhuHandler.removeMessages(1);
//                zhuHandler.removeCallbacksAndMessages(null);
//                ziHandler.removeMessages(0);
                break;
        }
    }
}
