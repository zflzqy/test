package zfl.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zfl.Adapter.bean.commbean;
import zfl.Adapter.util.commAdapter;
import zfl.Adapter.util.commAdapter01;
import zfl.Adapter.util.viewHolder;
import zfl.com.myapplication.R;

public class commAdapterActivity extends AppCompatActivity {
    private ListView mCommList;
    private List<commbean> Listbean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comm_adapter);
        init();
        commAdapter Adapter = new commAdapter<commbean>(this, Listbean,R.layout.item_commadapter) {
            @Override
            public void convert(viewHolder holder, commbean commbean) {
                holder.setText(R.id.comm_title,commbean.getTitle());
                holder.setText(R.id.comm_content,commbean.getContent());
                holder.setText(R.id.comm_time,commbean.getTime());
                holder.setText(R.id.comm_phone,commbean.getPhone());
            }
        };
        commAdapter01 Adapter01 = new commAdapter01(this,Listbean,R.layout.item_commadapter);
        mCommList.setAdapter(Adapter01);
    }

    private void init() {
        mCommList = findViewById(R.id.commAdapter_list);
        Listbean = new ArrayList<>();
        commbean cb = new commbean("one","one content","2018-5-7","110");
        Listbean.add(cb);
        cb = new commbean("two","two content","2018-5-8","110");
        Listbean.add(cb);
        cb = new commbean("three","three content","2018-5-9","110");
        Listbean.add(cb);
        cb = new commbean("four","four content","2018-5-10","110");
        Listbean.add(cb);

    }
}
