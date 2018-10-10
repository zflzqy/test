package zfl.zidingyiview.imageHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import zfl.com.myapplication.R;

/**
 * Created by Administrator on 2018/4/14.
 */

public class imagemziview extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;
    public imagemziview(Context context) {
        super(context);
        init();
    }

    public imagemziview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public imagemziview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.drawBitmap(mBitmap,mMatrix,null);
    }

    public void init(){
        mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        setimagebitmap(new Matrix());
    }

    public void setimagebitmap(Matrix matrix) {
        mMatrix = matrix;
    }
}
