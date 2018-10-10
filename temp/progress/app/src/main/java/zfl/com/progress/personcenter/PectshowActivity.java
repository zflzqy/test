package zfl.com.progress.personcenter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;


public class PectshowActivity extends AppCompatActivity {
    private String ACTION;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private pertInfonFrag perinfo;//个人信息完善
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_pectshow);
        Intent intent = getIntent();//获取意图，启动对应页面
        ACTION = intent.getStringExtra("ACTION");
        fm = getSupportFragmentManager();//获得fragment管理器对象
        ft = fm.beginTransaction();

        perinfo =new pertInfonFrag();
        if (ACTION.equals(constant.ACTION_PERTINFO)){
            ft.replace(R.id.pectshow,perinfo);
            ft.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                // 监听到返回按钮点击事件,后退
                if (ACTION.equals(constant.ACTION_PERTINFO))
                perinfo.exit();//这里我们调用的perinfoFrament中的exit
                return true;    //已处理
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
