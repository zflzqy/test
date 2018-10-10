package zfl.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import zfl.com.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class animlistner extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    private Button btn_listner;
    private Button btn_value;
    private int[] res = {R.id.image_a,R.id.image_b,R.id.image_c,R.id.image_d,
                            R.id.image_e,R.id.image_f,R.id.image_g,R.id.image_h};
    private List<ImageView> imageViewList = new ArrayList<>();
    private boolean flag = true;//设置是否再次点击
    private float radius = 500f;//卫星菜单的半径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animlistner);
        /*
        * ValueAnimator --数值发生器，可以实现很多很灵活的动画效果；
        ObjectAnimator --继承于ValueAnimator，可以很好滴使用属性对话框架；
        AnimatorUpdateListener -- 用于动画监听器
        AnimatorListenerAdapter-- 用于动画监听器
        PropertyValuesHolder --用于控制动画集合的显示效果
        Animatorset --用于控制动画集合的显示效果
        TypeEvaluators －－－值计算器，用于控制值变化的规律
        Interprolators －－－插值计算器，用于控制值变化的规律
        * */
        imageView  = findViewById(R.id.listner_image);
        btn_listner = findViewById(R.id.btn_listner);
        btn_value = findViewById(R.id.btn_value);

        btn_listner.setOnClickListener(this);
        btn_value.setOnClickListener(this);

        //动画菜单
        for (int i = 0;i<res.length;i++){
            ImageView image = findViewById(res[i]);
            image.setOnClickListener(this);
            imageViewList.add(image);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_a:
                if (flag){
                    startanim();
                }else {
                    closeanim();
                }
                break;
                //判断点击的展开图片
            case R.id.image_b:
//                closeanim();//点击图片后执行回收动作
                 Toast.makeText(animlistner.this,"点击b图片",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_listner:
                listner();
                break;
        }
        //value监听
        switch (view.getId()){
            case R.id.btn_value:
                value();
                break;
        }

    }
    //动画结束之后执行任务
    private void listner() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView,"alpha",0f,10f);
        anim.setDuration(1000);
        //Animator.AnimatorListener会补全所有过程中的监听,AnimatorListenerAdapter可以选择监听
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(animlistner.this,"动画结束",Toast.LENGTH_SHORT).show();
            }
        });
        anim.start();
    }

    private void startanim() {
        for (int i =1;i<res.length;i++){
            //属性动画实现卫星菜单
            float c2 = i *90/(res.length-1);//取得角度，控价个数减一进行计算角度
            float x = (float) Math.cos(c2*Math.PI/180)*radius;//计算图片x的坐标值，Math.PI/180换算成弧度
            float y = (float) Math.sin(c2*Math.PI/180)*radius;//计算图片y的坐标值
            Animator anim = ObjectAnimator.ofFloat(imageViewList.get(i),"translationY",0f,y);
            Animator anim1 = ObjectAnimator.ofFloat(imageViewList.get(i),"translationX",0f,x);
            AnimatorSet set =new AnimatorSet();
            set.setDuration(1000);
            set.setInterpolator(new OvershootInterpolator());
            set.playTogether(anim,anim1);
            set.start();
            //实现普通的下弹效果
//            Animator anim = ObjectAnimator.ofFloat(imageViewList.get(i),"translationY",i*100f,0);
//            anim.setDuration(1000);
            //差值器在apidemon里边有具体的view-animation-interpolators
//            anim.setInterpolator(new OvershootInterpolator());
//            anim.setStartDelay(i+200);
//            anim.start();
        }
        flag = false;
    }
    private void closeanim() {
        for (int i =1;i<res.length;i++){
            float c2 = (i -1)*90/(res.length-2);
            float x = (float) Math.cos(c2*Math.PI/180)*radius;
            float y = (float) Math.sin(c2*Math.PI/180)*radius;
            Animator anim = ObjectAnimator.ofFloat(imageViewList.get(i),"translationY",y,0f);
            Animator anim1 = ObjectAnimator.ofFloat(imageViewList.get(i),"translationX",x,0f);
            AnimatorSet set =new AnimatorSet();
            set.setDuration(1000);
            set.setInterpolator(new OvershootInterpolator());
            set.playTogether(anim,anim1);
            set.start();
//            Animator anim = ObjectAnimator.ofFloat(imageViewList.get(i),"translationY",i*200f,0f);
//            anim.setDuration(1000);
//            anim.setInterpolator(new OvershootInterpolator());
//            anim.setStartDelay(i*200);
//            anim.start();
        }
        flag = true;
    }
    private void value() {
        //ValueAnimator.ofObject<String>()可以插入不同的泛型
        ValueAnimator anim = ValueAnimator.ofInt(0,100);
        anim.setDuration(3000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                btn_value.setText(""+value);
            }
        });
        anim.start();
    }
}
