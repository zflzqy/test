package zfl.zidingyiview.personView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zfl.com.myapplication.R;


public class Topbar extends RelativeLayout {
    //定义控件
    private Button leftButton,rightButton;
    private TextView Title;

    //定义属性
    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private String title;
    private float titleTextSize;
    private int titleTextColor;

    //定义布局
    private LayoutParams leftParams,rightParams,titleParams;

    //接口回调

    private topbarClickListener listener;
    public interface topbarClickListener{
        void leftClick();
        void rightClick();
    }
    public void setOnTopbarClickListener(topbarClickListener listener){
        this.listener=listener;
    }

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);//TypedArray存储我们自定义XML中的属性值,TypedArray是通过键值对存储的
        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor,0);//第二个是默认值
        leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = ta.getString(R.styleable.Topbar_leftText);

        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor,0);
        rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = ta.getString(R.styleable.Topbar_rightText);

        title = ta.getString(R.styleable.Topbar_title);
        titleTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize,0);
        titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor,0);

        ta.recycle();//释放资源

        //初始化控件
        leftButton = new Button(context);
        rightButton = new Button(context);
        Title = new TextView(context);

        //给控件赋属性值
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        Title.setText(title);
        Title.setTextColor(titleTextColor);
        Title.setTextSize(titleTextSize);
        Title.setGravity(Gravity.CENTER);

        setBackgroundColor(0xFFF59563);
        //布局填充
        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton,leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton,rightParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(Title,titleParams);
        //左右button点击事件
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.leftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.rightClick();
            }
        });
    }
    public void setLeftIsVisable(boolean flag){
        if(flag){
            leftButton.setVisibility(View.VISIBLE);
        }
        else{
            leftButton.setVisibility(View.GONE);
        }
    }
    public void  setLeftButtonText(String s){
        leftButton.setText(s);
    }
}
