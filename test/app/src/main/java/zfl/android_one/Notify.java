package zfl.android_one;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zfl.com.myapplication.R;

public class Notify extends AppCompatActivity implements View.OnClickListener {
    private Button btn_send;
    private Button btn_cancel;
    private NotificationManager notifiynManager;
    private int notify_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notify);
        init();
        notifiynManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void init() {
        btn_send = findViewById(R.id.send);
        btn_cancel = findViewById(R.id.cancel);

        //监听
        btn_send.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
                sendNotify();
                break;
            case  R.id.cancel:
                notifiynManager.cancel(notify_id);
                break;
        }
    }

    private void sendNotify() {
        Intent intent = new Intent(this,Notify.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.a);
        builder.setTicker("有一条新消息");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("新消息");
        builder.setContentText("这是消息内容");
        builder.setAutoCancel(true);//通知消息会在被点击后自动消失
        builder.setContentIntent(pendingIntent);//设置点击跳转
        builder.setDefaults(Notification.DEFAULT_ALL);//设置效果提示

        Notification notification = builder.build();
        notifiynManager.notify(notify_id,notification);
    }
}
