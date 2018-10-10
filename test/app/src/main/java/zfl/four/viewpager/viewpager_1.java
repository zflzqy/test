package zfl.four.viewpager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import zfl.com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class viewpager_1 extends Activity {
    private ViewPager viewPager;
    private int[] imageids = new int[]{R.drawable.guide_image1,R.drawable.guide_image2,R.drawable.guide_image3};
    private List<ImageView> imageslist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_viewpager_1);
        viewPager = findViewById(R.id.viewpager1);
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(viewpager_1.this);
                imageView.setImageResource(imageids[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                //这种方法与下边的container.removeView(imageslist.get(position));联合使用，但会造成内存外泄
//                imageslist.add(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
//                container.removeView(imageslist.get(position));
            }

            @Override
            public int getCount() {
                return imageids.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }
}
