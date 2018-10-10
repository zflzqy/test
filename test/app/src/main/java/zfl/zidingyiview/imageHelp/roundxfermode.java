package zfl.zidingyiview.imageHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import zfl.com.myapplication.R;

/**
 * Created by Administrator on 2018/4/14.
 */

public class roundxfermode extends View {
    private Bitmap mBitmap,outBitmap;
    private Paint mPaint;
    public roundxfermode(Context context) {
        super(context);
        initview();
    }

    public roundxfermode(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initview();
    }

    public roundxfermode(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview();
    }
    private void initview(){
        setLayerType(LAYER_TYPE_SOFTWARE,null);//禁用硬件加速，不然图片绘制有问题
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.primarycolor);
        outBitmap = Bitmap.createBitmap(mBitmap.getWidth(),mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //(new RectF(0,0,mBitmap.getWidth(),mBitmap.getHeight())防止需要的api版本过高
        //dts
        canvas.drawRoundRect(new RectF(0,0,mBitmap.getWidth(),mBitmap.getHeight()),50,50,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //src
        canvas.drawBitmap(mBitmap,0,0,mPaint);
        mPaint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(outBitmap,0,0,null);
    }
}
