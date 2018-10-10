package zfl.zidingyiview.imageHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import zfl.com.myapplication.R;

/**
 * Created by Administrator on 2018/4/15.
 */

public class drawbitampmesh extends View {
    private int WIDTH = 200,HEIGHT = 200;//两个数值过大会引起崩溃
    private int count = (WIDTH+1)*(HEIGHT+1);
    private float[] verts = new float[count*2];
    private float[] orign = new float[count*2];
    private Bitmap mBitmap;
    private float k = 1;
    public drawbitampmesh(Context context) {
        super(context);
        initView();
    }

    public drawbitampmesh(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public drawbitampmesh(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.primarycolor);
        float mWidth = mBitmap.getWidth();
        float mHeight = mBitmap.getHeight();
        int index =0;//坐标点
        for (int i = 0; i <HEIGHT+1 ; i++) {
            float fy = mHeight*i/HEIGHT;
            for (int j = 0; j < WIDTH+1; j++) {
                float fx =mWidth*j/HEIGHT;
                orign[index*2+0]=verts[index*2+0]=fx;
                orign[index*2+1]=verts[index*2+1]=fy+200;
                index+=1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                verts[(i * (WIDTH + 1) + j) * 2 + 0] += 0;
                float offsetY = (float) Math.sin((float) j / WIDTH * 2 * Math.PI + k * 2 * Math.PI);
                verts[(i * (WIDTH + 1) + j) * 2 + 1] =
                        orign[(i * (WIDTH + 1) + j) * 2 + 1] + offsetY * 50;
            }
        }
        k +=0.1f;
        canvas.drawBitmapMesh(mBitmap,WIDTH,HEIGHT,verts,0,null,0,null);
        invalidate();
    }
}
