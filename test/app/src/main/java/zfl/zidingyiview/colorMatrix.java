package zfl.zidingyiview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import zfl.com.myapplication.R;

public class colorMatrix extends AppCompatActivity implements View.OnClickListener{
    private ImageView mImageView;
    private GridLayout mGridlayout;
    private Button mChange,mReset;
    private int mEdtWidth,mEdtHight;
    private EditText[] edts = new EditText[20];
    private float[] mColorMatrix = new float[20];
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_color_matrix);
        init();
        mGridlayout.post(new Runnable() {
            @Override
            public void run() {
                mEdtWidth = mGridlayout.getWidth() / 5;
                mEdtHight = mGridlayout.getHeight() / 4;
                addEdt();
                initMatrix();
            }
        });
    }

    private void initMatrix() {
        for (int i = 0;i<20;i++){
            if (i%6==0){
                edts[i].setText(String.valueOf(1));
            }
            else {
                edts[i].setText(String.valueOf(0));
            }
        }
    }

    private void addEdt() {
        for(int i = 0;i<20;i++){
            EditText edt = new EditText(colorMatrix.this);
            edt.setBackground(null);//设置没有下划线
            edts[i] = edt;
            mGridlayout.addView(edt,mEdtWidth,mEdtHight);
        }
    }

    private void init() {
        mImageView = findViewById(R.id.color_image);
        mGridlayout = findViewById(R.id.color_grid);
        mChange = findViewById(R.id.btn_colorchange);
        mReset = findViewById(R.id.btn_colorreset);

        mChange.setOnClickListener(this);
        mReset.setOnClickListener(this);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.primarycolor);

        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
         case  R.id.btn_colorchange:
                getMxtri();
                setBitmap(bitmap);
                break;
          case R.id.btn_colorreset:
                initMatrix();
                getMxtri();
                setBitmap(bitmap);
                break;
        }
    }

    private void setBitmap(Bitmap bitmap) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(mColorMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,0,0,paint);
        mImageView.setImageBitmap(bmp);
    }

    public void getMxtri() {
        for (int i = 0 ;i<20;i++){
            mColorMatrix[i] = Float.valueOf(edts[i].getText().toString());
        }
    }
}
