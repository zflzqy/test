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
import zfl.com.progress.personcenter.adapter.appAdapter;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;

public class  AppriseTaskFrag extends Fragment {
    private TextView title;
    private ListView mListview;
    private appAdapter adapter;
    private SwipeRefreshLayout btn_refresh;
    private Handler handler;//handler更新ui
    private List<Task> mTask;//任务集合
    private User user;//用户对象
    private Task task;//任务对象
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
        //获取user对象
        Intent intent =getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        //设置适配器
        adapter =new appAdapter(getContext(),mTask,handler);
        mListview.setAdapter(adapter);
        //子线程获取任务
        btn_refresh.performClick();//点击刷新

    }
    private void init(View view){
        title =view.findViewById(R.id.pershow_tv);
        mListview =view.findViewById(R.id.pershow_lv);
        btn_refresh=view.findViewById(R.id.pershow_refresh);

        //初始化数据
        handler =new Handler();
        mTask =new ArrayList<>();
        user =new User();
        task =new Task();
    }
    private void initEvent() {
        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取任务
                task =new Task();
                task.setIssue_account(user.getAccount());
                thread t = new thread(handler,task);
                t.start();
                btn_refresh.setRefreshing(false);//停止刷新
            }
        });
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //处理handler消息
            }
        };
        title.setText("待评价任务");
    }
    //返回键监听
    public void exit() {
        Intent intent =new Intent(getContext(), MainviewActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("ACTION", constant.ACTION_PERCENTER);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.left,R.anim.left);
    }
    //线程获取任务
    class  thread extends Thread{
        private Handler handler;
        private Task task;
        private List<Task> tasks;

        public thread(Handler handler, Task task) {
            this.handler = handler;
            this.task = task;
            tasks =new ArrayList<>();
        }

        @Override
        public void run() {
            okhttpTask  okhttpTask =new okhttpTask(task,constant.ACTION_APPRISETASK);
            tasks =okhttpTask.doGettask();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //更新ui
                    mTask.clear();
                    log.i("tasks",tasks.toString());
                    mTask.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
