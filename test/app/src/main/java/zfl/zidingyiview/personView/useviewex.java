package zfl.zidingyiview.personView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import zfl.com.myapplication.R;

public  class useviewex extends LinearLayout {
    private Button mBack,mMenu;
    private TextView text;
    private listner listner;
    public  interface listner{
         void right();
         void left();
    };
    public   void setOnclicklistner(listner listner){
        this.listner = listner;
    }
    public useviewex(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_usemoreview,this);
        mBack = findViewById(R.id.left);
        mMenu = findViewById(R.id.right);
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.left();
            }
        });
        mMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.right();
            }
        });
    }
}
