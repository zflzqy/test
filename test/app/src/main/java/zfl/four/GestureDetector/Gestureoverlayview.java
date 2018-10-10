package zfl.four.GestureDetector;

import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import zfl.com.myapplication.R;

import java.util.ArrayList;

public class Gestureoverlayview extends AppCompatActivity {
    private GestureOverlayView Gesture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gestureoverlayview);
        init();
        final GestureLibrary gestureLibrary = GestureLibraries.fromRawResource(Gestureoverlayview.this,R.raw.gestures);
        gestureLibrary.load();

        Gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, android.gesture.Gesture gesture) {
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                Prediction prediction = predictions.get(0);
                if (prediction.score>=1){
                    if (prediction.name.equals("exit")){
                        finish();
                    }
                    else if (prediction.name.equals("next")){
                        Toast.makeText(Gestureoverlayview.this,"下一个页面",Toast.LENGTH_SHORT).show();
                    }
                    else if (prediction.name.equals("pervious")){
                        Toast.makeText(Gestureoverlayview.this,"上一个页面",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Gestureoverlayview.this,"没有这个手势",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        Gesture = (GestureOverlayView) findViewById(R.id.Gesture);
    }
}
