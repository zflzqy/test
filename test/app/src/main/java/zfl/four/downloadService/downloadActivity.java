package zfl.four.downloadService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import zfl.com.myapplication.R;
import zfl.four.downloadService.bean.FileInfo;
import zfl.four.downloadService.util.downloadService;

import static android.widget.Toast.LENGTH_SHORT;

public class downloadActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv;
    private ProgressBar mProgress;
    private Button stop,start;
    private FileInfo fileInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);
        init();
        String qq = "http://www.eoemarket.com/downloadThread/38199_0";
        String nvshen = "http://www.eoemarket.com/downloadThread/918838_0";
        tv.setText("360wangpan");
        fileInfo = new FileInfo(0,"360wangpan_setup_6.5.6.1288.exe","http://down.360safe.com/yunpan/360wangpan_setup_6.5.6.1288.exe",0,0);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(downloadService.ACTION_UPDATE);
        filter.addAction(downloadService.ACTION_FINISHED);
        registerReceiver(bdr,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bdr);
    }

    private void init() {
        tv = findViewById(R.id.tv_download);
        mProgress = findViewById(R.id.pb_download);
        start = findViewById(R.id.start_download);
        stop = findViewById(R.id.stop_download);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        mProgress.setMax(100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_download:
                Intent intent = new Intent(downloadActivity.this,downloadService.class);
                intent.setAction(downloadService.ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
                break;
            case R.id.stop_download:
                Intent intent2 = new Intent(downloadActivity.this,downloadService.class);
                intent2.setAction(downloadService.ACTION_STOP);
                intent2.putExtra("fileInfo",fileInfo);
                startService(intent2);
                break;
        }
    }
    //广播接收者
    BroadcastReceiver bdr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (downloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                Log.i("finished", "finished:"+finished);
                mProgress.setProgress(finished);
            }else if (downloadService.ACTION_FINISHED.equals(intent.getAction())){
                mProgress.setProgress(0);
                Toast.makeText(downloadActivity.this,fileInfo.getName()+"下完毕", LENGTH_SHORT).show();
            }
        }
    };
}
