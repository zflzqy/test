package zfl.com.progress.personcenter.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.okhttpTask;

public class appAdapter extends BaseAdapter {
    private List<Task> mTask;
    private Handler handler;
    private Task task;
    private User user;
    private viewHodler hodler;
    private LayoutInflater mInflater;//界面载入
    public appAdapter(Context context, List<Task> mTask, Handler handler){
        this.mTask=mTask;
        this.handler=handler;
        mInflater =LayoutInflater.from(context);
        user =new User();
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
        hodler=null;
        if (convertView==null){
            hodler =new viewHodler();
            convertView =mInflater.inflate(R.layout.frg_appmyadapter,null);
            hodler.tv_name =convertView.findViewById(R.id.apr_itname);
            hodler.tv_account=convertView.findViewById(R.id.apr_itaccount);
            hodler.ed_appcontent=convertView.findViewById(R.id.apr_appcont);
            hodler.rb_appriselevel=convertView.findViewById(R.id.apr_ratingbar);
            hodler.btn_commit=convertView.findViewById(R.id.apr_itcomm);
            convertView.setTag(hodler);
        }else {
            hodler = (viewHodler) convertView.getTag();
        }
        task=mTask.get(position);
        //子线程获取完成人信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                user.setAccount(task.getIssue_account());
                okhttpTask okhttpTask =new okhttpTask(user, constant.ACTION_GETUSERBYTASK);
                user =okhttpTask.doGetuser();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hodler.tv_name.setText("完成人名："+user.getName());
                        hodler.tv_account.setText("完成人账号："+user.getAccount());
                    }
                });
            }
        }).start();
        hodler.btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //子线程提交评价
                String content = hodler.ed_appcontent.getText().toString();//获得评价内容
                int level = hodler.rb_appriselevel.getRight();//获得评价等级
                task.setAppraise(content);
                task.setAppraiselevel(level);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpTask okhttpTask =new okhttpTask(task,constant.ACTION_APPRISECOT);
                        String rs =okhttpTask.task();

                    }
                }).start();
            }
        });
        return convertView;
    }
    class viewHodler{
        TextView tv_name,tv_account;
        EditText ed_appcontent;
        Button btn_commit;
        RatingBar rb_appriselevel;
    }
}
