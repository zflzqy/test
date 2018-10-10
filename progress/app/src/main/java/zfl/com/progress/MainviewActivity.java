package zfl.com.progress;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.Mytask.MytaskFrag;
import zfl.com.progress.otherpeotask.OthertaskFrag;
import zfl.com.progress.personcenter.PercenFrag;
import zfl.com.progress.receiveTask.receivetaskFrag;
import zfl.com.progress.util.log;

public class MainviewActivity extends FragmentActivity implements View.OnClickListener{
    private ImageView iv_mytask,iv_personcenter,iv_othertask,iv_receive;
    private LinearLayout li_mytask,li_personcenter,li_othertask,li_receive;
    private ViewPager viewPager;
    private List<Fragment> mList;
    private FragmentPagerAdapter mAdapter;
    private String ACTION;
    private User user;
    public  static  boolean perfect =false;//检测是否完善信息
    private long time = 0;//记录上一次点击时间;不能放在监听内，不然一直置为0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        ACTION= intent.getStringExtra("ACTION");
        user = (User) intent.getSerializableExtra("user");
        log.i("user","这是用户信息"+user.toString());
        //传递过来的对象自动赋值为本身名称
        init();//初始化控件
        initEvent();//监听控件
        if (ACTION != null) {
                if (ACTION.equals(constant.ACTION_PERCENTER)) {
                    setSelect(3);//启动个人中心
                }
        }
    }
    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - time) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();//并记录下本次点击“返回键”的时刻，以便下次进行判断
            } else {
                finish();//小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initEvent() {
        //监听
        li_mytask.setOnClickListener(this);
        li_personcenter.setOnClickListener(this);
        li_othertask.setOnClickListener(this);
        li_receive.setOnClickListener(this);
        //当完善信息后才监听(进行滑动)
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               if (!perfect){
                    return true;
               }else {
                    return false;
               }
            }
        });
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    setTab(viewPager.getCurrentItem());
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
        iv_mytask =findViewById(R.id.main_image_mytask);
        iv_othertask =findViewById(R.id.main_image_othertask);
        iv_personcenter =findViewById(R.id.main_image_personcenter);
        iv_receive =findViewById(R.id.main_image_receive);

        li_mytask =findViewById(R.id.main_line_mytask);
        li_personcenter =findViewById(R.id.main_line_personcenter);
        li_othertask =findViewById(R.id.main_line_othertask);
        li_receive =findViewById(R.id.main_line_receive);

        viewPager = findViewById(R.id.main_vp);

        user =new User();
        mList =new ArrayList<>();
        //添加顺序很重要,不然就添加到指定下标
        MytaskFrag mytask =new MytaskFrag();
        OthertaskFrag othertask = new OthertaskFrag();
        receivetaskFrag receivetask =new receivetaskFrag();
        PercenFrag personcenter = new PercenFrag();
        mList.add(mytask);
        mList.add(othertask);
        mList.add(receivetask);
        mList.add(personcenter);
        mAdapter =new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        };
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (!perfect) {
            setSelect(3);
        }else {
            switch (v.getId()) {
                case R.id.main_line_mytask:
                    log.i("onclik", "" + "taskThread");
                    setSelect(0);
                    break;
                case R.id.main_line_othertask:
                    log.i("onclik", "" + "othertask");
                    setSelect(1);
                    break;
                case R.id.main_line_receive:
                    log.i("onclik", "" + "receive");
                    setSelect(2);
                    break;
                case R.id.main_line_personcenter:
                    log.i("onclik", "" + "person");
                    setSelect(3);
                    break;
            }
        }
    }
    //设置下方页面切换
    private void setSelect(int i) {
        setTab(i);
        viewPager.setCurrentItem(i);
    }

    private void setTab(int i) {
        reset();
        switch (i){
            case 0:
                iv_mytask.setImageResource(R.drawable.mytask_press);
                break;
            case 1:
                iv_othertask.setImageResource(R.drawable.othertask_press);
                break;
            case 2:
                iv_receive.setImageResource(R.drawable.receivetask_press);
                break;
            case 3:
                iv_personcenter.setImageResource(R.drawable.personcenter_press);
                break;
        }
    }
    //复位图片
    private void reset(){
        iv_mytask.setImageResource(R.drawable.mytask);
        iv_othertask.setImageResource(R.drawable.othertask);
        iv_receive.setImageResource(R.drawable.receivetask);
        iv_personcenter.setImageResource(R.drawable.personcenter);
    }
}
