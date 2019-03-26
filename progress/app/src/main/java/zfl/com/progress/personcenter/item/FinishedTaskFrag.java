package zfl.com.progress.personcenter.item;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.personcenter.adapter.finAdapter;
import zfl.com.progress.util.okhttpTask;
public class FinishedTaskFrag extends Fragment {
    private ListView mListview;
    private TextView tv_title;
    private List<Task> mTask;
    private Task task;
    private User user;
    private SwipeRefreshLayout btn_refresh;
    private finAdapter adapter;
    private Handler handler =new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_pershow,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view); // 初始化
        initEvent(); // 事件
        mListview.setAdapter(adapter);
        taskThread tk =new taskThread();
        tk.start();
    }

    private void init(View view) {
        mListview =view.findViewById(R.id.pershow_lv);
        tv_title =view.findViewById(R.id.pershow_tv);

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mTask =new ArrayList<>();
        adapter =new finAdapter(getContext(),mTask);
    }

    private void initEvent() {
        tv_title.setText("已完成");
    }
    //返回键监听
    public void exit() {
        Intent intent =new Intent(getContext(), MainviewActivity.class);
        intent.putExtra("user",user);
//       intent.putExtra("ACTION",constant.ACTION_PERCENTER);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.left,R.anim.left);
    }
    //获取任务
    class taskThread extends Thread {
        List<Task> tasks;
        @Override
        public void run() {
            //获取数据
            okhttpTask okhttpTask = new okhttpTask();
            tasks = okhttpTask.doGetAlltask(user.getAccount(),constant.URL_finshedTasks);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTask.clear();
                    mTask.addAll(tasks);
                    adapter.notifyDataSetChanged();//更新listview
                }
            });
        }
    }
}
