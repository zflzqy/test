package zfl.zidingyiview;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import zfl.com.myapplication.R;

import zfl.zidingyiview.imageHelp.imagemziview;

public class ImageMatrixview extends AppCompatActivity implements View.OnClickListener{
    private imagemziview mImagematrixview;
    private GridLayout mGridlayout;
    private Button btn_change,btn_reset;
    private int mEdtwidth,mEdtheight;
    private EditText[] edts = new EditText[9];
    private float[] mImagematrix = new float[9];
    private Matrix matrix ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_matrixview);
        init();
        mGridlayout.post(new Runnable() {
            @Override
            public void run() {
                mEdtwidth = mGridlayout.getWidth()/3;
                mEdtheight = mGridlayout.getHeight()/3;
                addEts();
                intMtrix();
            }
        });
    }

    @Override
    public void onClick(View view) {
        matrix = new Matrix();
        switch (view.getId()){
            case R.id.btn_imagezichange:
                getMatrix();
              matrix.setValues(mImagematrix);
//                matrix.setRotate(90);//设置旋转
//                matrix.setScale(2,2);
//                matrix.postTranslate(100,100);//post可以让两个效果同时进行，不用post后边动作覆盖前边动作

                mImagematrixview.setimagebitmap(matrix);
                mImagematrixview.invalidate();
                break;
            case R.id.btn_imagezireset:
                intMtrix();//复原要先初始化矩阵数组
                getMatrix();
                matrix = new Matrix();
                matrix.setValues(mImagematrix);
                mImagematrixview.setimagebitmap(matrix);
                mImagematrixview.invalidate();
                break;
        }
    }

      private void init() {
        mImagematrixview = findViewById(R.id.imageziview);
        mGridlayout = findViewById(R.id.imagezi_gird);
        btn_change = findViewById(R.id.btn_imagezichange);
        btn_reset = findViewById(R.id.btn_imagezireset);

        btn_change.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

    }

    private void addEts() {
        for (int i = 0;i<9;i++){
            EditText ed = new EditText(this);
            ed.setBackground(null);
            ed.setGravity(Gravity.CENTER);
            edts[i]=ed;
            mGridlayout.addView(ed,mEdtwidth,mEdtheight);
        }
    }
    private void intMtrix(){
        for (int i = 0;i<9;i++){
            if (i%4==0){
                edts[i].setText(String.valueOf(1));
            }
            else {
                edts[i].setText(String.valueOf(0));
            }
        }
    }
    private void getMatrix() {
        for (int i=0;i<9;i++){
            mImagematrix[i] = Float.valueOf(edts[i].getText().toString());
        }
    }

}
