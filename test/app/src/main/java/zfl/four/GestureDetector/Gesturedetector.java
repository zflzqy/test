package zfl.four.GestureDetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import zfl.com.myapplication.R;


public class Gesturedetector extends AppCompatActivity {
    private ImageView btn_image;
    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gesturedetector);
        init();
        mGestureDetector = new GestureDetector(new myTochlistener());
        btn_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }
    class  myTochlistener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX()-e2.getX()>30){
                Toast.makeText(Gesturedetector.this,"往左滑动",Toast.LENGTH_SHORT).show();
            }else if(e2.getX()-e1.getX()>30)
            {
                Toast.makeText(Gesturedetector.this,"往右滑动",Toast.LENGTH_SHORT).show();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    private void init() {
        btn_image = findViewById(R.id.btn_image);
    }

}
