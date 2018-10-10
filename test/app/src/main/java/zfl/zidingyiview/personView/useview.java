package zfl.zidingyiview.personView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import zfl.com.myapplication.R;

public class useview extends AppCompatActivity {
    private useviewex t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_useview);
        t = findViewById(R.id.t);

}

}
