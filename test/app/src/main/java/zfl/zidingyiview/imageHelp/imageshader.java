package zfl.zidingyiview.imageHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.nfc.NfcEvent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import zfl.com.myapplication.R;

/**
 * Created by Administrator on 2018/4/14.
 */

public class imageshader extends View {
    private Bitmap mBitamap;
    private Paint paint;
    private BitmapShader mBitamapshader;
    public imageshader(Context context) {
        super(context);
    }

    public imageshader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public imageshader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBitamap = BitmapFactory.decodeResource(getResources(), R.drawable.primarycolor);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitamapshader = new BitmapShader(mBitamap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(mBitamapshader);
        canvas.drawCircle(300,300,200,paint);
    }
}
