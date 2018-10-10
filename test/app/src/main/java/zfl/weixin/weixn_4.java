package zfl.weixin;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import zfl.com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class weixn_4 extends FragmentActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    private LinearLayout mWeixin;
    private LinearLayout mPerson;
    private LinearLayout mAddress;
    private LinearLayout mSetting;

    private ImageView mImageWeixin;
    private ImageView mImagePerson;
    private ImageView mImageAddress;
    private ImageView mImageSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_weixn_4);
        init();
        initEvent();
    }

    private void initEvent() {
        mWeixin.setOnClickListener(this);
        mPerson.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mSetting.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int index = viewPager.getCurrentItem();
                setTab(index);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        mWeixin = findViewById(R.id.line_weixn);
        mPerson = findViewById(R.id.line_person);
        mAddress = findViewById(R.id.line_address);
        mSetting = findViewById(R.id.line_setting);

        mImageWeixin = findViewById(R.id.image_weixn);
        mImagePerson = findViewById(R.id.image_person);
        mImageAddress = findViewById(R.id.image_address);
        mImageSetting = findViewById(R.id.image_setting);

        viewPager = findViewById(R.id.weinx4_viewpager);

        fragmentList = new ArrayList<>();
        weixin_3weixn tb1 = new weixin_3weixn();
        weixin_3person tb2 = new weixin_3person();
        weixin_3address tb3 = new weixin_3address();
        weixin_3weixnsetting tb4 = new weixin_3weixnsetting();
        fragmentList.add(tb1);
        fragmentList.add(tb2);
        fragmentList.add(tb3);
        fragmentList.add(tb4);

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View view) {
        reset();
        switch (view.getId())
        {
            case  R.id.line_weixn:
                setSelect(0);
                break;
            case R.id.line_person:
                setSelect(1);
                break;
            case R.id.line_address:
                setSelect(2);
                break;
            case R.id.line_setting:
                setSelect(3);
                break;
        }
    }

    private void setSelect(int i) {
        setTab(i);
        viewPager.setCurrentItem(i);
    }

    private void setTab(int i) {
        reset();
        switch (i){
            case 0:
                mImageWeixin.setImageResource(R.drawable.tab_weixin_pressed);
                break;
            case 1:
                mImagePerson.setImageResource(R.drawable.tab_find_frd_pressed);
                break;
            case 2:
                mImageAddress.setImageResource(R.drawable.tab_address_pressed);
                break;
            case 3:
                mImageSetting.setImageResource(R.drawable.tab_settings_pressed);
                break;
        }
    }

    private void reset() {
        mImageWeixin.setImageResource(R.drawable.tab_weixin_normal);
        mImagePerson.setImageResource(R.drawable.tab_find_frd_normal);
        mImageAddress.setImageResource(R.drawable.tab_address_normal);
        mImageSetting.setImageResource(R.drawable.tab_settings_normal);
    }
}
