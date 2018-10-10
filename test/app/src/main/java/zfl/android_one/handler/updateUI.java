package zfl.android_one.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import zfl.com.myapplication.R;

public class updateUI extends AppCompatActivity {
    private TextView textView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_ui);
        textView = findViewById(R.id.textView2);
        handler = new Handler(){
            //handler2添加的
            @Override
            public void handleMessage(Message msg) {
                textView.setText("right");
            }
        };
        //更新ui
        new Thread(){
            @Override
            public void run() {
                //会报错，因为没有Looper.prepare();而主线程在创建activity时已经运行/Looper.prepare()；
//                Handler handler = new Handler();

                try {
                    Thread.sleep(100);
                    handler4();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }
    //handler1
    private void handler1(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("right");
            }
        });
    }
    //handler2
    private  void handler2(){
        handler.sendEmptyMessage(1);
    }
    //handler3
    private void handler3(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("right");
            }
        });
    }
    //handler4该方法需要睡眠线程其他方法不用
    //该方法通过viewrootimpl执行更新ui由于ui线程需要在resume方法中创建viewrootimpl所有睡眠当前线程以保证ui线程执行onresume方法
    private void handler4(){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("right");
            }
        });
    }
}
