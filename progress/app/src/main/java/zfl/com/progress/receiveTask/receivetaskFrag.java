package zfl.com.progress.receiveTask;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.Datechange;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;

public class receivetaskFrag extends Fragment {
    private ListView mListview;
    private SwipeRefreshLayout btn_refresh;
    private List<Task> mTask;
    private Task task;
    private User user;
    private Task changeTask;//改变的任务
    private retaskAdapter adapter;
    private Handler handler =new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receivetask,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Intent intent =getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        init(view);//初始化控件
        intEvent();//事件
        //数据显示
        adapter =new retaskAdapter(getContext(),mTask,handler);
        mListview.setAdapter(adapter);
        //获取任务
        btn_refresh.performClick();//点击刷新
    }

    private void init(View view) {
        mListview =view.findViewById(R.id.retask_lv);
        btn_refresh =view.findViewById(R.id.retask_refresh);
        mTask =new ArrayList<>();
        //下拉刷新设置
        btn_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
    private void intEvent() {
        //刷新
        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取任务
                taskThread tk =new taskThread(user.getAccount());
                tk.start();
                btn_refresh.setRefreshing(false);//停止刷新
            }
        });
         handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                log.i("msgwhat",msg.what+"");
                switch (msg.what){
                    case 1:
                        //删除操作,子线程
                        changeTask = (Task) msg.obj;
                        changeTask.setFinishtime(Datechange.getNow());
                        changeTask.setGiveup(msg.arg1);
                        log.i("handler","这是按钮发送的消息："+changeTask.toString()+"       "+msg.arg1);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                okhttpTask okhttpTask =new okhttpTask(changeTask,constant.ACTION_GIVEUPTASK);
                                String rs = okhttpTask.task();
                                if (rs.equals("giveupsuccess")){
                                    //更新ui
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTask.remove(changeTask);//删除该项
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }).start();
                        break;
                    case 2:
                        changeTask= (Task) msg.obj;
                        changeTask.setFinised(1);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                okhttpTask okhttpTask =new okhttpTask(changeTask,constant.ACTION_FINISHETASK);
                                String rs =okhttpTask.task();
                                if (rs.equals("FINISHTASK")){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //清除该项
                                            mTask.remove(changeTask);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }).start();
                        break;
                }
            }
        };
    }
    //获取任务
    class taskThread extends Thread{
        private int issueaccount;
        private List<Task> tasks;//可领任务集合
        Gson gson ;
        public taskThread(int issueaccount) {
            this.issueaccount = issueaccount;
            gson=new Gson();
        }

        @Override
        public void run() {
            //获取数据
            task =new Task();
            task.setIssue_account(issueaccount);
            okhttpTask okhttpTask =new okhttpTask(task, constant.ACTION_RECEIVE);
            tasks =okhttpTask.doGettask();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTask.clear();
                    mTask.addAll(tasks);
                    log.i("task","这是可领任务集合"+mTask.toString());
                    adapter.notifyDataSetChanged();//更新listview
                    mListview.setAdapter(adapter);
                }
            });
        }
    }
}
