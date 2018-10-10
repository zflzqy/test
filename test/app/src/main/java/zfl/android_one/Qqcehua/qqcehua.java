package zfl.android_one.Qqcehua;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import zfl.com.myapplication.R;

public class qqcehua extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_qqcehua);
    }
}
