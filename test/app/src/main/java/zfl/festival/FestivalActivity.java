package zfl.festival;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import zfl.com.myapplication.R;
import zfl.festival.festival_fragment.festival_blessing;
import zfl.festival.festival_fragment.festival_history;


public class FestivalActivity extends AppCompatActivity {
    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private String[] mTitles = new String[]{"节日祝福","发送记录"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_festival);
        init();
    }

    private void init() {
        mTablayout = findViewById(R.id.festival_tablayot);
        mViewpager = findViewById(R.id.festival_viewpager);
        setTitle("祝福短信");

        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position==1)
                    return new festival_history();
                return new festival_blessing();
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        mTablayout.setupWithViewPager(mViewpager);//tablayout关联viewpager
    }
}
