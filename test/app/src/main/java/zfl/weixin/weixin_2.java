package zfl.weixin;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import zfl.com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class weixin_2 extends Activity implements View.OnClickListener{
    private ViewPager viewPager;
    private PagerAdapter mAdapter;
    private List<View> mViewlist;

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
        setContentView(R.layout.layout_weixin_2);
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

            }

            @Override
            public void onPageSelected(int position) {
                int crurren = viewPager.getCurrentItem();
                reset();
                switch (crurren){
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

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        viewPager = findViewById(R.id.weinx2_viewpager);
        mWeixin = findViewById(R.id.line_weixn);
        mPerson = findViewById(R.id.line_person);
        mAddress = findViewById(R.id.line_address);
        mSetting = findViewById(R.id.line_setting);

        mViewlist = new ArrayList<>();

        mImageWeixin = findViewById(R.id.image_weixn);
        mImagePerson = findViewById(R.id.image_person);
        mImageAddress = findViewById(R.id.image_address);
        mImageSetting = findViewById(R.id.image_setting);

        final LayoutInflater mInflater = LayoutInflater.from(this);
        View tb1= mInflater.inflate(R.layout.item_weixin2_tb1,null);
        View tb2= mInflater.inflate(R.layout.item_weixin2_tb2,null);
        View tb3= mInflater.inflate(R.layout.item_weixin2_tb3,null);
        View tb4= mInflater.inflate(R.layout.item_weixin2_tb4,null);
        mViewlist.add(tb1);
        mViewlist.add(tb2);
        mViewlist.add(tb3);
        mViewlist.add(tb4);

        mAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViewlist.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViewlist.get(position);
                container.addView(view);
                return  view;

            }

            @Override
            public int getCount() {
                return mViewlist.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        reset();
        switch (view.getId()){
            case R.id.line_weixn:
                viewPager.setCurrentItem(0);
                mImageWeixin.setImageResource(R.drawable.tab_weixin_pressed);
                break;
            case R.id.line_person:
                viewPager.setCurrentItem(1);
                mImagePerson.setImageResource(R.drawable.tab_find_frd_pressed);
                break;
            case R.id.line_address:
                viewPager.setCurrentItem(2);
                mImageAddress.setImageResource(R.drawable.tab_address_pressed);
                break;
            case R.id.line_setting:
                viewPager.setCurrentItem(3);
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
