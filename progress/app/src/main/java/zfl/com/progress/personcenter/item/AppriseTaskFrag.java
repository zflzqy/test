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
import android.widget.Toast;

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
    private Task changetask;
    private Handler handler;//handler更新ui
    private List<Task> mTask;//任务集合
    private User user;//用户对象
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
        //获取任务
        thread t = new thread(handler,user.getAccount());
        t.start();

    }
    private void init(View view){
        title =view.findViewById(R.id.pershow_tv);
        mListview =view.findViewById(R.id.pershow_lv);
        btn_refresh=view.findViewById(R.id.pershow_refresh);

        //初始化数据
        handler =new Handler();
        mTask =new ArrayList<>();
        user =new User();
    }
    private void initEvent() {
        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取任务
                thread t = new thread(handler,user.getAccount());
                t.start();
                btn_refresh.setRefreshing(false);//停止刷新
            }
        });
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //处理handler消息
                switch (msg.what){
                    case 1:
                        //按钮事件 传递的是引用就会改变原值
                        changetask = (Task) msg.obj;
                        changetask.setFinished(3);
                        log.i("arg1",msg.arg1+"");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                okhttpTask okhttpTask =new okhttpTask();
                                // 评价任务
                                String rs =okhttpTask.dealtask(changetask,constant.URL_appriseTask);
                                if (rs.equals("APPRISE")){
                                    //更新ui
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 通知更新ui
                                            mTask.remove(changetask); // 移除对象
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(),"评价成功",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(getContext(),"评价失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).start();
                        break;
                }
            }
        };
        title.setText("待评价任务");
    }
    //返回键监听
    public void exit() {
        Intent intent =new Intent(getContext(), MainviewActivity.class);
        intent.putExtra("user",user);
//        intent.putExtra("ACTION", constant.ACTION_PERCENTER);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.left,R.anim.left);
    }
    //线程获取任务
    class  thread extends Thread{
        private Handler handler;
        private Integer account;
        private List<Task> tasks;

        public thread(Handler handler,Integer account) {
            this.handler = handler;
            this.account = account;
            tasks =new ArrayList<>();
        }

        @Override
        public void run() {
            okhttpTask  okhttpTask =new okhttpTask();
            tasks =okhttpTask.doGetAlltask(account,constant.URL_appriseTasks);
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
