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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.personcenter.adapter.excAdapter;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;

public class ExceTaskFrag extends Fragment {
    private ListView mListview;
    private TextView tv_title;
    private SwipeRefreshLayout btn_refresh;
    private excAdapter adapter;
    private List<Task> mTask;
    private Task changetask;
    private User user;
    private Handler handler =new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frg_pershow,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent =getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        init(view); // 初始化控件
        initEvent(); // 事件
        adapter =new excAdapter(getContext(),mTask,handler,user.getAccount());
        mListview.setAdapter(adapter);
        //线程获取任务
        taskThread tk = new taskThread(user.getAccount(),handler);
        tk.start();
    }

    private void init(View view) {
        mListview =view.findViewById(R.id.pershow_lv);
        btn_refresh =view.findViewById(R.id.pershow_refresh);
        tv_title =view.findViewById(R.id.pershow_tv);

        mTask = new ArrayList<>();
    }
    private void initEvent() {
        tv_title.setText("异常任务");
        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新，线程获取任务
                taskThread tk = new taskThread(user.getAccount(), handler);
                tk.start();
                btn_refresh.setRefreshing(false);// 停止刷新
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        //按钮事件 传递的是引用就会改变原值
                        changetask = (Task) msg.obj;
                        changetask.setGiveup(msg.arg1);
                        log.i("arg1",msg.arg1+"");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                okhttpTask okhttpTask =new okhttpTask();
                                String rs =okhttpTask.dealtask(changetask,constant.URL_giveupMyTask);
                                if (rs.equals("giveupsuccess")){
                                    //更新ui
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 通知更新ui
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(),"放弃成功",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(getContext(),"放弃失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).start();
                        break;
                }
            }
        };
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
    class  taskThread extends Thread{
        private int account;
        private List<Task> tasks;
        private Handler handler;
        public taskThread(int account,Handler handler) {
            this.account =account;
            this.handler =handler;
            tasks =new ArrayList<>();
        }
        @Override
        public void run() {
            okhttpTask okhttpTask =new okhttpTask();
            tasks =okhttpTask.doGetAlltask(account,constant.URL_getMyExceptionTask);
            log.i("exce",tasks.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mTask.clear();
                    mTask.addAll(tasks);
                    adapter.notifyDataSetChanged();//更新数据
                }
            });
        }
    }
}
