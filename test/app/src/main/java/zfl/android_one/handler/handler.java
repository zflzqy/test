package zfl.android_one.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import zfl.com.myapplication.*;

public class handler extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    private Handler handler;
    private int[] images={R.drawable.a,R.drawable.b,R.drawable.c};
    private int index;//变量默认值0
    private myRunable mYrunable= new myRunable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_handler);
        init();
        handler.postDelayed(mYrunable,1000);//这里的延时是延时1秒进入mYrunable方法
//        handler跟新UI
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("hello update");
                    }
                });
            }
        }.start();
//        handler发送信息
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    Message message = new Message();
//                    message = handler.obtainMessage();//获取一个message对象
//                    message.arg1 = 11;
//                    message.arg2 = 22;
                    people p = new people();
                    p.name = "赵飞龙";
                    p.age = 21;
                    message.obj = p;
                    handler.sendMessage(message);//发送给handler自己
//                  message.sendToTarget();//第二种发送消息
//                    handler.removeCallbacks();//移除一个消息，传入一个runnable对象
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void init() {
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView2);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                textView.setText(""+msg.obj);
//                    textView.setText(""+msg.arg1+"--"+msg.arg2);
            }
        };
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                return false;
        //当reuturn ture 时将不会调用void HandMessage方法，也就是消息被boolean HandMessage方法截获
//            }
//        }){
//            @Override
//            public void handleMessage(Message msg) {
//            }
//        };
    }
    class  myRunable implements Runnable{

        @Override
        public void run() {
            index++;
            index = index%3;
                imageView.setImageResource(images[index]);
                //这里的延时是延时执行本身，递归调用mYrunable执行图片轮换
                handler.postDelayed(mYrunable,1000);
        }
    }
    class  people{
        public int age;
        public String name;

        @Override
        public String toString() {
            return "名字："+name+"     "+"年龄："+age;
        }
    }
}
