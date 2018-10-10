package zfl.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import zfl.com.myapplication.R;

public class ObjectAnimation extends AppCompatActivity implements View.OnClickListener{
    private ImageView image;
    private Button anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_object_animation);
        image = findViewById(R.id.anim_image);
        anim = findViewById(R.id.anim);
        anim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //ObjectAnimator.ofFloat方式加载动画
//        ObjectAnimator.ofFloat(image,"rotation",0f,360f).setDuration(1000).start();
//        ObjectAnimator.ofFloat(image,"translationX",0f,300f).setDuration(1000).start();
//        ObjectAnimator.ofFloat(image,"translationY",0f,300f).setDuration(1000).start();

//        //PropertyValuesHolder方式加载动画
//        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation",0f,360f);
//        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationX",0f,300f);
//        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationY",0f,300f);
//        ObjectAnimator.ofPropertyValuesHolder(image,p1,p2,p3).setDuration(1000).start();

        //AnimatorSet方式加载数据
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(image,"rotation",0f,360f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(image,"translationX",0f,300f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(image,"translationY",0f,300f);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(image,"translationX",300f,0f);
        //按规则播放
        set.play(anim4);
//        set.play(anim2).with(anim3);
//        set.play(anim1).after(anim3);

        //顺序播放动画
//        set.playSequentially(anim1,anim2,anim3);

        //同时播放动画
//        set.playTogether(anim1,anim2,anim3);
        set.setDuration(1000);
        set.start();

    }
}
