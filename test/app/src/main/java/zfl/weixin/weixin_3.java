package zfl.weixin;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import zfl.com.myapplication.R;

public class weixin_3 extends FragmentActivity implements View.OnClickListener {
    private LinearLayout mWeixin;
    private LinearLayout mPerson;
    private LinearLayout mAddress;
    private LinearLayout mSetting;

    private ImageView mImageWeixn;
    private ImageView mImagePerson;
    private ImageView mImageAddress;
    private ImageView mImageSetting;

    private weixin_3weixn weixn;
    private weixin_3person person;
    private weixin_3address address;
    private weixin_3weixnsetting setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_weixin_3);
        init();
        initEvent();
        select(0);
    }

    private void initEvent() {
        mWeixin.setOnClickListener(this);
        mPerson.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mSetting.setOnClickListener(this);
    }

    private void init() {
        mWeixin = findViewById(R.id.line_weixn);
        mPerson = findViewById(R.id.line_person);
        mAddress = findViewById(R.id.line_address);
        mSetting = findViewById(R.id.line_setting);

        mImageWeixn = findViewById(R.id.image_weixn);
        mImagePerson = findViewById(R.id.image_person);
        mImageAddress = findViewById(R.id.image_address);
        mImageSetting = findViewById(R.id.image_setting);
    }

    @Override
    public void onClick(View view) {
        reset();
        switch (view.getId()){
            case R.id.line_weixn:
                select(0);
                mImageWeixn.setImageResource(R.drawable.tab_weixin_pressed);
                break;
            case R.id.line_person:
                select(1);
                mImagePerson.setImageResource(R.drawable.tab_find_frd_pressed);
                break;
            case R.id.line_address:
                select(2);
                mImageAddress.setImageResource(R.drawable.tab_address_pressed);
                break;
            case R.id.line_setting:
                select(3);
                mImageSetting.setImageResource(R.drawable.tab_settings_pressed);
                break;
        }
    }

    private void select(int i) {
        FragmentManager fm  = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hidefragment(ft);
        switch (i){
            case 0:
                if (weixn==null){
                    weixn= new weixin_3weixn();
                }
                else {
                    ft.show(person);
                }
            case 1:
                if (person==null){
                    person= new weixin_3person();
                }
                else {
                    ft.show(person);
                }
            case 2:
                if (address==null){
                    address = new weixin_3address();
                }
                else {
                    ft.show(address);
                }
            case 3:
                if (setting==null){
                    setting= new weixin_3weixnsetting();
                }
                else {
                    ft.show(setting);
                }
        }
    }


    private void hidefragment(FragmentTransaction ft) {
        if (weixn!=null){
            ft.hide(weixn);
        }
        if (person!=null){
            ft.hide(person);
        }
        if (address!=null){
            ft.hide(address);
        }
        if (setting!=null){
            ft.hide(setting);
        }
    }

    private void reset() {
        mImageWeixn.setImageResource(R.drawable.tab_weixin_normal);
        mImagePerson.setImageResource(R.drawable.tab_find_frd_normal);
        mImageAddress.setImageResource(R.drawable.tab_address_normal);
        mImageSetting.setImageResource(R.drawable.tab_settings_normal);
    }
}
