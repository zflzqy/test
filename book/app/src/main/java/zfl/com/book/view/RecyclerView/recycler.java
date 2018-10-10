package zfl.com.book.view.RecyclerView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.List;
import zfl.com.book.R;

public class recycler extends AppCompatActivity{
    private RecyclerView mRecycler;
    private List<String> mList;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler);
        init();
        LinearLayoutManager  llm =new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向滚动
        StaggeredGridLayoutManager sm = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);//设置九宫格布局
//        mRecycler.setLayoutManager(sm);
        mRecycler.setLayoutManager(llm);
        mAdapter =new MyAdapter(mList);
        mRecycler.setAdapter(mAdapter);
    }

    private void init() {
        mRecycler =findViewById(R.id.recyler);

        mList =new ArrayList<>();
        //添加数据
        for (int i = 0; i <20 ; i++) {
            mList.add("第"+i+"个");
        }
    }
}
