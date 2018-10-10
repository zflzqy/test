package zfl.com.progress.personcenter.item;

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

import java.util.ArrayList;
import java.util.List;
import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.personcenter.adapter.finAdapter;
import zfl.com.progress.util.okhttpTask;
public class FinishedTaskFrag extends Fragment {
    private ListView mListview;
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
        init(view);//初始化
        initEvent();//事件
        mListview.setAdapter(adapter);
        taskThread tk =new taskThread();
        tk.start();
    }

    private void init(View view) {
        mListview =view.findViewById(R.id.pershow_lv);

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mTask =new ArrayList<>();
        adapter =new finAdapter(getContext(),mTask);
    }

    private void initEvent() {
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };
    }
    //获取任务
    class taskThread extends Thread {
        List<Task> tasks;
        @Override
        public void run() {
            //获取数据
            task = new Task();
            task.setIssue_account(user.getAccount());
            okhttpTask okhttpTask = new okhttpTask(task, constant.ACTION_FINISHEDTASK);
//            tasks = okhttpTask.doGettask();
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
