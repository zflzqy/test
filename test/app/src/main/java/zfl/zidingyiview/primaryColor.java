package zfl.zidingyiview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.SeekBar;

import zfl.com.myapplication.R;

import zfl.zidingyiview.imageHelp.imageHelper;


public class primaryColor extends Activity implements SeekBar.OnSeekBarChangeListener{
    private ImageView mImageView;
    private SeekBar mSeekbarhue,mSeekbarsaturation,mSeekbarlum;
    private int MAX_VALUE = 255,MID_VALUE = 127;
    //mHue = 0,mSaturation = 1,mLum = 1;三个变量初始化，防止拖动变黑
    private float mHue = 0,mSaturation = 1,mLum = 1;
    private Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_primarycolor);
        init();
        initEvent();
    }

    private void initEvent() {
        mSeekbarhue.setOnSeekBarChangeListener(this);
        mSeekbarsaturation.setOnSeekBarChangeListener(this);
        mSeekbarlum.setOnSeekBarChangeListener(this);
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zqy);
        mImageView = findViewById(R.id.prim_image);
        mSeekbarhue = findViewById(R.id.prim_seekbarhue);
        mSeekbarsaturation = findViewById(R.id.prim_seekbarsaturation);
        mSeekbarlum = findViewById(R.id.prim_seekbarlum);

        mSeekbarhue.setMax(MAX_VALUE);
        mSeekbarsaturation.setMax(MAX_VALUE);
        mSeekbarlum.setMax(MAX_VALUE);
        mSeekbarhue.setProgress(MID_VALUE);
        mSeekbarsaturation.setProgress(MID_VALUE);
        mSeekbarlum.setProgress(MID_VALUE);

        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()){
            case R.id.prim_seekbarhue:
                mHue = (i - MID_VALUE) * 1.0f/ MID_VALUE * 180;
                break;
            case R.id.prim_seekbarsaturation:
                mSaturation =  i*1.0f/MID_VALUE;
                break;
            case R.id.prim_seekbarlum:
                mLum = i*1.0f/MID_VALUE;
                break;
        }
        mImageView.setImageBitmap(imageHelper.imageHandler(bitmap,mHue,mSaturation,mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
