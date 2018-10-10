package zfl.zidingyiview.imageHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import zfl.com.myapplication.R;

/**
 * Created by Administrator on 2018/4/15.
 */

public class imagejingxiang extends View {
    private Bitmap mBitmap,outBitmap;
    private Paint paint;
    public imagejingxiang(Context context) {
        super(context);
        initView();
    }

    public imagejingxiang(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public imagejingxiang(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.primarycolor);
        Matrix  matrix = new Matrix();
        matrix.setScale(1,-1);//关于x轴对称
//        matrix.setScale(-1,1);//关于y轴对称
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0,mBitmap.getHeight(),mBitmap.getWidth(),mBitmap.getHeight()*2,
                0XDD000000,0X10000000, Shader.TileMode.CLAMP));
        outBitmap = Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(),mBitmap.getHeight(),matrix,true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.drawBitmap(outBitmap,0,mBitmap.getHeight(),null);
        canvas.drawRect(0,mBitmap.getHeight(),mBitmap.getWidth(),mBitmap.getHeight()*2,paint);
    }
}
