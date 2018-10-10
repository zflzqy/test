package zfl.zidingyiview.personView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import zfl.com.myapplication.R;

public class Topbaractivity extends AppCompatActivity {
    private Topbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_topbaractivity);
        tb = findViewById(R.id.Topbar);
        tb.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(Topbaractivity.this,"left",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(Topbaractivity.this,"right",Toast.LENGTH_SHORT).show();
            }
        });
        tb.setLeftButtonText("左边的按钮");
        tb.setLeftIsVisable(false);
    }

}
