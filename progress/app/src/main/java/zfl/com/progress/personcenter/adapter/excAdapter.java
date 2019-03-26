package zfl.com.progress.personcenter.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;

public class excAdapter extends BaseAdapter {
    private List<Task> mTask;
    private Handler handler;
    private User user;
    private Task task;
    private Message message;
    private Context context;
    private int account;
    private LayoutInflater mInflater;
    private viewHodler hodler;

    public excAdapter(Context context, List<Task> mTask, Handler handler, int account) {
        this.mTask = mTask;
        this.handler = handler;
        this.account = account;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        user = new User();
    }

    @Override
    public int getCount() {
        return mTask.size();
    }

    @Override
    public Object getItem(int position) {
        return mTask.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            hodler = new viewHodler();
            convertView = mInflater.inflate(R.layout.frg_exadapter, null);
            hodler.tv_request = convertView.findViewById(R.id.ext_itrq);
            hodler.tv_price = convertView.findViewById(R.id.ect_itpr);
            hodler.tv_type = convertView.findViewById(R.id.ect_ittp);
            hodler.tv_giveupname = convertView.findViewById(R.id.ect_itname);
            hodler.tv_giveacc = convertView.findViewById(R.id.ect_itaccount);
            hodler.tv_givewho = convertView.findViewById(R.id.ect_ittip);
            hodler.acceptegiveup = convertView.findViewById(R.id.ect_itgu);
            convertView.setTag(hodler);
        } else {
            hodler = (viewHodler) convertView.getTag();
        }
        task = mTask.get(position);
        message =new Message();
        user =new User();
        log.i("task", "这是新的task" + task.toString());
        hodler.tv_request.setText("要求："+task.getRequest());
        hodler.tv_price.setText("价格:"+String.valueOf(task.getPrice()));
        hodler.tv_type.setText("类型:"+task.getType());
        //本人任务
        if (task.getIssue_account() == account) {//本人任务
            if (task.getGiveup() == 1 || task.getGiveup() == 3) {//本人请求放弃的
                hodler.acceptegiveup.setEnabled(false);//禁用同意放弃
                //获取放弃人
                user.setAccount(task.getIssue_account());
                thread t = new thread(user, handler,hodler.tv_giveupname);
                t.start();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hodler.tv_giveupname.setText("这是放弃人"+user.getName());
                    }
                });
                hodler.tv_giveacc.setText("放弃人账号：" + task.getIssue_account() + "");
                if (task.getGiveup()==3){
                    hodler.tv_givewho.setText("已放弃任务");
                }else {
                    hodler.tv_givewho.setText("等待对方同意放弃");
                }
            } else if (task.getGiveup() == 2 || task.getGiveup() == 4) {//对方请求放弃的
                if (task.getGiveup() == 2) {
                    hodler.acceptegiveup.setEnabled(true);
                    message.arg1=4;//同意对方放弃任务
                } else {
                    hodler.acceptegiveup.setEnabled(false);//已放弃
                }
                user.setAccount(task.getReceive_account());
                thread t = new thread(user, handler,hodler.tv_giveupname);
                t.start();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hodler.tv_giveupname.setText("这是放弃人"+user.getName());
                    }
                });
                hodler.tv_giveacc.setText("放弃人账号：" + task.getReceive_account() + "");
                if (task.getGiveup()==4){
                    hodler.tv_givewho.setText("已放弃任务");
                }else {
                    hodler.tv_givewho.setText("领取人放弃任务");
                }
            }
        }
        //他人任务
        else if (task.getReceive_account() == account) {//对方任务
            if (task.getGiveup() == 2 || task.getGiveup() == 4) {//本人请求放弃的,已同意放弃的
                hodler.acceptegiveup.setEnabled(false);
                user.setAccount(task.getReceive_account());
                thread t = new thread(user, handler,hodler.tv_giveupname);
                t.start();
                hodler.tv_giveacc.setText("放弃人账号：" +task.getReceive_account());
                if (task.getGiveup()==2){
                    hodler.tv_givewho.setText("等待对方同意放弃");
                }else {
                    hodler.tv_givewho.setText("已放弃任务");
                }
            } else if (task.getGiveup() == 1 || task.getGiveup() == 3) {//对方请求放弃的
                if (task.getGiveup() == 1) {
                    hodler.acceptegiveup.setEnabled(true);
                    message.arg1=3;//同意放弃，对方放弃对方任务
                } else {
                    hodler.acceptegiveup.setEnabled(false);//已同意放弃
                }
                user.setAccount(task.getIssue_account());
                thread t = new thread(user, handler,hodler.tv_giveupname);
                t.start();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hodler.tv_giveupname.setText("这是新的name"+user.getName());
                    }
                });
                hodler.tv_giveacc.setText("放弃人账号：" +task.getIssue_account());
                if (task.getGiveup()==1){
                    hodler.tv_givewho.setText("发布人放弃任务");
                }else {
                    hodler.tv_givewho.setText("已放弃任务");
                }
            }
        }
        hodler.acceptegiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听同意放弃
                message.obj=task;
                message.what = 1;//按钮的handler
                handler.sendMessage(message);
            }
        });
        return convertView;
    }

    class viewHodler {
        TextView tv_request, tv_price, tv_type, tv_giveupname, tv_giveacc, tv_givewho;
        Button acceptegiveup;
    }

    //获取新的user对象
    class thread extends Thread {
        private User user;
        private Handler handler;
        private TextView tv_givename;

        public thread(User user, Handler handler,TextView tv_givename) {
            this.user = user;
            this.handler = handler;
            this.tv_givename =tv_givename;
        }

        @Override
        public void run() {
            okhttpTask okhttpTask = new okhttpTask();
            user = okhttpTask.doGetuser(user.getAccount(),constant.URL_exceGetUser);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //将texttvie对象传入，防止设置人名错误
                    tv_givename.setText("放弃人名字：" + user.getName());
                    log.i("task", "这是userName" + user.getName());
                }
            });
        }
    }
}
