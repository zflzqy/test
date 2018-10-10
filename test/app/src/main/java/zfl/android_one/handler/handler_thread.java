package zfl.android_one.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import zfl.com.myapplication.R;

public class handler_thread extends AppCompatActivity {
    private TextView textView;
    private mYthread mythread = new mYthread();
    private HandlerThread Handerthread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_handler_thread);
        textView = findViewById(R.id.handler_thread);

        mythread.start();//子线程
        try {
            // mythread.handler为空，mythread.start();后面添加Thread.sleep(500);休眠半秒钟
            // mythread.handler在run中创建，mythread.handler不为空
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textView.setText(""+mythread.handler.sendEmptyMessage(1));

        Handerthread = new HandlerThread("hi");//指定名字，子线程
        Handerthread.start();
        //通过getLooper方法判断线程是否存在，并判断loop是否为空，为空等待，在HandlerThread的run方法中创建loop
        handler = new Handler(Handerthread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                System.out.println("handlerthread执行"+Thread.currentThread());
            }
        };
        handler.sendEmptyMessage(1);
    }
    class  mYthread extends Thread{
      public Handler handler;
        @Override
        public void run() {
            Looper.prepare();//创建loop
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("在非主线程中执行"+Thread.currentThread());
                }
            };
            Looper.loop();//循环读取messagequeue内的消息
        }
    }
}
