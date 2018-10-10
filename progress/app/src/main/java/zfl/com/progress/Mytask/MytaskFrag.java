package zfl.com.progress.Mytask;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.Datechange;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;

public class MytaskFrag extends Fragment {
    private ListView mListview;
    private SwipeRefreshLayout btn_refresh;//下拉刷新
    private Button btn_add;//添加任务
    private mytaskAdapter adapter;
    private Task task;//请求任务
    private Task changeTask;//改变任务
    private List<Task> mTask;//任务数据源
    private User user;
    private Gson gson;
    private Handler handler =new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mytask,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Intent intent =getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        init(view);//初始化控件
        initEvent();//控件监听
        //数据展示
        String s =mTask.toString();
        adapter =new mytaskAdapter(getContext(),mTask,handler);
        mListview.setAdapter(adapter);

        //获取任务
        btn_refresh.performClick();//点击刷新
    }
    //初始化控件
    @SuppressLint("ResourceAsColor")
    private void init(View view) {
        mListview =view.findViewById(R.id.mytask_lv);
        btn_refresh =view.findViewById(R.id.mytask_srl);
        mTask =new ArrayList<>();
        gson=new Gson();
        //listvie设置
        btn_add =new Button(getContext());
        btn_add.setTextSize(20);
        btn_add.setText("发布任务");
        mListview.addFooterView(btn_add);
        //下拉刷新设置
        btn_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initEvent() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issuetask();//发布任务
            }
        });
        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                taskThread tk =new taskThread();
                tk.start();
                btn_refresh.setRefreshing(false);//停止刷新
            }
        });
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                        //删除操作,子线程
                        changeTask = (Task) msg.obj;//成员变量防止final修饰而无法更改
                        changeTask.setFinishtime(Datechange.getNow());
                        if (changeTask.getAccept()==0){
                            changeTask.setGiveup(3);
                        }else {
                            changeTask.setGiveup(1);
                        }
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
            }
        };
    }


    //获取任务
    class taskThread extends Thread{
         List<Task> tasks;
        @Override
        public void run() {
            //获取数据
            task =new Task();
            task.setIssue_account(user.getAccount());
            okhttpTask okhttpTask =new okhttpTask(task, constant.ACTION_MYTASK);
            tasks =new ArrayList<>();
            tasks=okhttpTask.doGettask();
            log.i("mTask",mTask.size()+"");
            //更新ui
            UpdateUi(tasks);
        }
    }
    //发布任务
    private void issuetask() {
        final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.frg_issuetask,null);
        final TextView tv_type =view.findViewById(R.id.ist_type);
        tv_type.setText("one");//设置默认
        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //类型
                String[] tt =new String[]{"one","two","threee"};
                tasktype(tt,tv_type);
            }
        });
        builder.setView(view);
        builder.setNegativeButton("发布", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                //构造任务对象
                task =new Task();;
                try{

                    //发布任务
                    EditText ed_request, ed_price, ed_endtime;
                    ed_request = view.findViewById(R.id.ist_req);
                    ed_price = view.findViewById(R.id.ist_pri);
                    ed_endtime = view.findViewById(R.id.ist_endtime);
                    if(ed_request.getText().toString().equals("")||ed_request.getText().toString().length()>140){
                        Toast.makeText(getContext(),"要求不能为空或要求超140字数l！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    task.setRequest(ed_request.getText().toString());
                    task.setIssue_account(user.getAccount());
                    if (Float.parseFloat(ed_price.getText().toString())==0.0){
                        Toast.makeText(getContext(),"金额不能为0",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    task.setPrice(Float.parseFloat(ed_price.getText().toString()));
                    task.setType(tv_type.getText().toString());
                    if (Integer.parseInt(ed_endtime.getText().toString())<1){
                        Toast.makeText(getContext(),"天数不能小于1",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    task.setEndtime(Integer.parseInt(ed_endtime.getText().toString()));
                    task.setAccept(0);
                    task.setGiveup(0);
                    task.setFinised(0);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            log.i("taskThread",task.toString());
                            okhttpTask issuetask = new okhttpTask(task, constant.ACTION_ISSUETASK);
                            String rs = issuetask.task();
                            if (rs.equals("success")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTask.add(task);
                                        adapter.notifyDataSetChanged();//更新listview
                                    }
                                });
                            }//if语句
                        }
                    }).start();
                }catch (NumberFormatException e){
                    Toast.makeText(getContext(),"金额异常",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//取消发布
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);//设置点击周围屏幕不消失
        //获取对话框宽高参数
        Window win = dialog.getWindow();
        //防止背景使得对话框宽度无法充满父布局
        win.setBackgroundDrawableResource(android.R.color.white);
        WindowManager.LayoutParams params = win.getAttributes();
        //获取屏幕参数
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        //设置对话框宽高
        params.width =WindowManager.LayoutParams.MATCH_PARENT;
        params.height =WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);     //设置生效
    }
    //更新ui
    public void UpdateUi(final List<Task> tasks) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //list数据更新放在主线程内，防止子线程更新了数据（异步调用）没有通知主线程更新数据
                mTask.clear();
                mTask.addAll(tasks);
                adapter.notifyDataSetChanged();//更新listview
            }
        });
    }
    //类型列表
       private void tasktype(final String[] List, final TextView type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("选择任务类型");
        builder.setSingleChoiceItems(List, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                type.setText(List[i]);
            }
        });
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }
}
