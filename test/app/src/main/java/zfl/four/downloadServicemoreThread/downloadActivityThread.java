package zfl.four.downloadServicemoreThread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zfl.com.myapplication.R;
import zfl.four.downloadServicemoreThread.bean.FileInfo;
import zfl.four.downloadServicemoreThread.util.downloadAdapter;
import zfl.four.downloadServicemoreThread.util.downloadServiceThread;

public class downloadActivityThread extends AppCompatActivity {
    private List<FileInfo> fileInfos;
    private ListView mListview;
    private downloadAdapter adapter;
    private String url;
    public static int LENGTH = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_downloadnorethread);
        init();
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(downloadServiceThread.ACTION_UPDATE);
        filter.addAction(downloadServiceThread.ACTION_FINISHED);
        registerReceiver(bdr,filter);
        adapter = new downloadAdapter(this,fileInfos);
        mListview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bdr);
    }

    private void init() {
        mListview = findViewById(R.id.downloadthread);
        fileInfos = new ArrayList<>();
        String yun_360 = "http://down.360safe.com/yunpan/360wangpan_setup_6.5.6.1288.exe";
        String baidu = "http://dl.ops.baidu.com/baidusearch_AndroidPhone_757p.apk";
        FileInfo fileInfo = new FileInfo(0,"baidu",baidu,0,0);
        FileInfo fileInfo1 = new FileInfo(1,"360yun",yun_360,0,0);
        FileInfo fileInfo2 = new FileInfo(2,"微信",yun_360,0,0);
        FileInfo fileInfo3 = new FileInfo(3,"手机淘宝",yun_360,0,0);
        fileInfos.add(fileInfo);
        fileInfos.add(fileInfo1);
        fileInfos.add(fileInfo2);
        fileInfos.add(fileInfo3);

    }
    //广播接收者
    BroadcastReceiver bdr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (downloadServiceThread.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                int id = intent.getIntExtra("id",0);
                adapter.updateprogress(id,finished);
            }
            else if (downloadServiceThread.ACTION_FINISHED.equals(intent.getAction())){
                //更新进度为0
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");

                adapter.updateprogress(fileInfo.getId(),0);
                Toast.makeText(downloadActivityThread.this,fileInfos.get(fileInfo.getId()).getName(),Toast.LENGTH_SHORT).show();
            }
        }
    };
}
