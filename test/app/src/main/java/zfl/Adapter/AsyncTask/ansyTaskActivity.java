package zfl.Adapter.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import zfl.com.myapplication.R;

public class ansyTaskActivity extends AppCompatActivity {
    private ImageView mImage;
    private ProgressBar mPraogress;
    private TextView mText;//表示刻度值
    private String URL = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2099571514,1384482078&fm=27&gp=0.jpg";
    private MyAnsyncTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ansy_task);
        init();
        task.execute(URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task!=null&task.getStatus() ==MyAnsyncTask.Status.RUNNING){
            task.cancel(true);//仅仅标记为返回了
        }
    }

    private void init() {
        mImage = findViewById(R.id.ansy_image);
        mPraogress = findViewById(R.id.ansy_progress);
        mText = findViewById(R.id.ansy_text);

        task = new MyAnsyncTask(mImage,mPraogress,mText);
    }
}
