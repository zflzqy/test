package zfl.four.service;

import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import zfl.com.myapplication.R;

public class MySystemService extends AppCompatActivity implements View.OnClickListener{
    private Button btn_network;
    private Button btn_wifi;
    private Button btn_voice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mysystemservice);
        init();
    }

    private void init() {
        btn_network = findViewById(R.id.network);
        btn_wifi = findViewById(R.id.btn_wifi);
        btn_voice = findViewById(R.id.btn_voice);
        //监听
        btn_network.setOnClickListener(this);
        btn_wifi.setOnClickListener(this);
        btn_voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.network :
                if (isNetwork(MySystemService.this)==true){
                    Toast.makeText(MySystemService.this,"网络已打开",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MySystemService.this,"网络已关闭",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_wifi :
                WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                if (mWifiManager.isWifiEnabled()){
                    mWifiManager.setWifiEnabled(false);
                    Toast.makeText(MySystemService.this,"wifi已关闭",Toast.LENGTH_SHORT).show();
                }
                else {
                    mWifiManager.setWifiEnabled(true);
                    Toast.makeText(MySystemService.this,"wifi已打开",Toast.LENGTH_SHORT).show();
                }
            case  R.id.btn_voice :
                AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
                int now = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
                Toast.makeText(MySystemService.this,"最大音量"+max+"当前音量"+now,Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isNetwork (Context context){
        if (context!=null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            assert mConnectivityManager != null;
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo!=null){
                return mNetworkInfo.isAvailable();
            }
        }
            return  false;
    }
}
