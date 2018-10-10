package zfl.zidingyiview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import zfl.com.myapplication.R;

import zfl.zidingyiview.imageHelp.imageHelper;

public class pixelseffect extends AppCompatActivity {
    private ImageView image1,image2,image3,image4;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pixelseffect);
        init();
        image1.setImageResource(R.drawable.zqy);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zqy);
        image2.setImageBitmap(imageHelper.imagedipian(bitmap));
        image3.setImageBitmap(imageHelper.iamgeold(bitmap));
        image4.setImageBitmap(imageHelper.fudiao(bitmap));
    }

    private void init() {
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
    }


}
