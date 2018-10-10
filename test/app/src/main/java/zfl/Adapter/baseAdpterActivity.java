package zfl.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zfl.Adapter.bean.basebean;
import zfl.com.myapplication.R;

public class baseAdpterActivity extends AppCompatActivity {
    private ListView mListview;
    private List<basebean> mBasebean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_adpter);
        init();
        baseAapter adapter = new baseAapter(this,mBasebean);
        mListview.setAdapter(adapter);
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void init() {
        mListview = findViewById(R.id.baseAlist);
        mBasebean = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            mBasebean.add(new basebean
                    (R.drawable.ic_launcher,"第"+i+"title","第"+i+"content"));
        }
    }
}
