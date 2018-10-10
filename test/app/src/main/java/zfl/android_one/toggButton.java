package zfl.android_one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import zfl.com.myapplication.R;

public class toggButton extends AppCompatActivity {
    private ToggleButton tb;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_togg_button);
        tb = findViewById(R.id.toggleButton);
        image = findViewById(R.id.togg_image);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                image.setImageResource(b?R.drawable.off:R.drawable.on);
            }
        });
    }
}
