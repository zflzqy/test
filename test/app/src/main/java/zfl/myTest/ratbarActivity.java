package zfl.myTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import zfl.com.myapplication.R;

public class ratbarActivity extends AppCompatActivity {
    private RatingBar rb;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratbar);
        rb =findViewById(R.id.rb_rb);
        btn =findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ratbarActivity.this,"星星数量"+rb.getRating(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
