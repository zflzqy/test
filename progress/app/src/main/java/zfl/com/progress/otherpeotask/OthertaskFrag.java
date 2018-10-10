package zfl.com.progress.otherpeotask;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.Datechange;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;

public class OthertaskFrag extends Fragment {
    private Spinner sp_pri, sp_type;
    private Button btn_credit;//信用排序
    private SwipeRefreshLayout btn_refresh;
    private ListView mList;
    private User user;
    private List<Task> mTask;//可领任务集合
    private Task task;//请求的任务
    private Task changeTask;//改变任务
    private othertaskAdapter adapter;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other, container, false);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        init(view);//初始化
        initEvent();//事件
        //数据展示
        adapter = new othertaskAdapter(getContext(), mTask, handler);
        mList.setAdapter(adapter);
        //获取数据
        btn_credit.performClick();//首先按信用排序

    }

    private void init(View view) {
        sp_pri = view.findViewById(R.id.othtask_sp_pri);
        mList = view.findViewById(R.id.otttask_lv);
        sp_type = view.findViewById(R.id.othtask_sp_type);
        btn_credit =view.findViewById(R.id.othtask_btn_credit);
        btn_refresh =view.findViewById(R.id.othtask_refresh);
        mTask = new ArrayList<>();
        //下拉刷新设置
        btn_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initEvent() {
        //刷新
        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取数据
                taskThread tk = new taskThread(user.getAccount());
                tk.start();
                btn_refresh.setRefreshing(false);//停止刷新
            }
        });
        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                btn_credit.setTextColor(getContext().getResources().getColor(R.color.red));
                //信用排序
                taskThread tk =new taskThread(user.getAccount());
                tk.start();
            }
        });
        sp_pri.setSelection(0,true);//取消默认监听调用
        sp_pri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    reset();
                    TextView textView = (TextView) view;
                    textView.setTextColor(getContext().getResources().getColor(R.color.red));
                    String s =parent.getItemAtPosition(position).toString();
                    if (s.equals("价格升序")){
                        Collections.sort(mTask, new Comparator<Task>() {
                            @Override
                            public int compare(Task o1, Task o2) {
                                return (int) (o1.getPrice()-o2.getPrice());
                            }
                        });
                    }else if (s.equals("价格降序")){
                        Collections.sort(mTask, new Comparator<Task>() {
                            @Override
                            public int compare(Task o1, Task o2) {
                                return (int) (o2.getPrice()-o1.getPrice());
                            }
                        });
                    }
                    adapter =new othertaskAdapter(getContext(),mTask,handler);
                    mList.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_type.setSelection(0,true);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    reset();
                    TextView textView = (TextView) view;
                    textView.setTextColor(getContext().getResources().getColor(R.color.red));
                    //分类筛选
                    String s =parent.getItemAtPosition(position).toString();
                    List<Task>  ordertask =new ArrayList<>();
                    for (Task task:mTask){
                        if (task.getType()!=null){
                            if (task.getType().equals(s)){
                                ordertask.add(task);
                            }
                        }
                    }
                    adapter =new othertaskAdapter(getContext(),ordertask,handler);
                    mList.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //领取任务
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                changeTask = (Task) msg.obj;
                changeTask.setReceive_account(user.getAccount());
                changeTask.setStarttime(Datechange.getNow());//设置开始任务时间
                changeTask.setAccept(1);
                log.i("other", "这是领取任务" + changeTask.toString() + msg.what);
                Toast.makeText(getContext(), "这是将要领取的任务", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpTask okhttpTask = new okhttpTask(changeTask, constant.ACTION_GETTASK);
                        String rs = okhttpTask.task();
                        if (rs.equals("gettasksuccess")) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //更新数据
                                    mTask.remove(changeTask);
//                                    adapter.notifyDataSetChanged();
                                    adapter =new othertaskAdapter(getContext(),mTask,handler);
                                    mList.setAdapter(adapter);
                                }
                            });
                        }
                    }
                }).start();
            }
        };
    }
    private  void reset(){
        //重置颜色
        btn_credit.setTextColor(getContext().getResources().getColor(R.color.black));
        ((TextView)sp_pri.getSelectedView()).setTextColor(getContext().getResources().getColor(R.color.black));
        ((TextView)sp_type.getSelectedView()).setTextColor(getContext().getResources().getColor(R.color.black));
    }
    //获取任务
    class taskThread extends Thread {
        private int issueaccount;
        private List<Task> tasks;
        public taskThread(int issueaccount) {
            this.issueaccount = issueaccount;
            tasks =new ArrayList<>();
        }

        @Override
        public void run() {
            //获取数据
            task = new Task();
            task.setIssue_account(issueaccount);
            okhttpTask okhttpTask = new okhttpTask(task, constant.ACTION_OTHERTASK);
            tasks = okhttpTask.doGettask();
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
