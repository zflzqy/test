package zfl.weixin;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zfl.com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class weixin_1 extends FragmentActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private TextView tv_chart;
    private TextView tv_find;
    private TextView tv_person;
    private LinearLayout line_cahrt;
    private LinearLayout line_find;
    private LinearLayout line_person;
    private ImageView viewpager_image;
    private BadgeView mBadgeView;
    private int mScreen1_3;//屏幕宽度的3分之一
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_weixin1);
        init();
        inittab();
    }

    private void init() {
        viewPager = findViewById(R.id.viewpager1);

        tv_chart = findViewById(R.id.tv_chart);
        tv_find = findViewById(R.id.tv_find);
        tv_person = findViewById(R.id.tv_person);

        line_cahrt = findViewById(R.id.linelt_chart);
        line_find = findViewById(R.id.linelt_find);
        line_person = findViewById(R.id.linelt_person);
        line_cahrt.setOnClickListener(this);
        line_find.setOnClickListener(this);
        line_person.setOnClickListener(this);

        viewpager_image = findViewById(R.id.viewpage_image);

        fragmentList = new ArrayList<>();
        mBadgeView = new BadgeView(this);

        viewpagerfrachart chart = new viewpagerfrachart();
        viewpagerfrafind find = new viewpagerfrafind();
        viewpagerfraperson person = new viewpagerfraperson();

        fragmentList.add(chart);
        fragmentList.add(find);
        fragmentList.add(person);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewpager_image.getLayoutParams();
                lp.leftMargin=(int) ((position+positionOffset)*mScreen1_3);
                viewpager_image.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position){
                    case 0:
//                        frme(tv_chart,line_cahrt,mBadgeView);
                        tv_chart.setTextColor(Color.GREEN);
                        break;
                    case 1:
//                        frme(tv_find,line_find,mBadgeView);
                        tv_find.setTextColor(Color.GREEN);
                        break;
                    case 2:
//                        frme(tv_person,line_person,mBadgeView);
                        tv_person.setTextColor(Color.GREEN);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void inittab(){
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outM = new DisplayMetrics();
        display.getMetrics(outM);
        mScreen1_3 = outM.widthPixels/3;
        ViewGroup.LayoutParams lp = viewpager_image.getLayoutParams();
        lp.width  = mScreen1_3;
        viewpager_image.setLayoutParams(lp);
    }
    private void resetTextView() {
        tv_chart.setTextColor(Color.BLACK);
        tv_find.setTextColor(Color.BLACK);
        tv_person.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View view) {
        resetTextView();
        switch (view.getId()){
            case R.id.linelt_chart:
                viewPager.setCurrentItem(0);
                tv_chart.setTextColor(Color.GREEN);
                break;
            case R.id.linelt_find:
                viewPager.setCurrentItem(1);
                tv_find.setTextColor(Color.GREEN);
                break;
            case R.id.linelt_person:
                viewPager.setCurrentItem(2);
                tv_person.setTextColor(Color.GREEN);
                break;
        }
    }
//    失败的方法会出现程序崩溃
//    private void frme(TextView textView,LinearLayout layout,BadgeView badgeView){
//        if (badgeView!=null){
//            layout.removeView(badgeView);
//        }
//        badgeView.setBadgeCount(10);
//        layout.addView(badgeView);
//        textView.setTextColor(Color.GREEN);
//    }

}
