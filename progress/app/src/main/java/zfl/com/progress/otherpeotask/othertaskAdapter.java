package zfl.com.progress.otherpeotask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.R;
import zfl.com.progress.util.log;

public class othertaskAdapter extends BaseAdapter {
    private List<Task> mTask;
    private Handler handler;
    private LayoutInflater mInflater;
    private Context context;
    private Task task;
    public othertaskAdapter(Context context, List<Task> mTask, Handler handler) {
        this.context =context;
        this.mTask = mTask;
        this.handler =handler;
        mInflater =LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler hoder = null;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.frg_otherdapter,null);
            hoder =new ViewHodler();
            hoder.tv_request =convertView.findViewById(R.id.ott_itrq);
            hoder.tv_price =convertView.findViewById(R.id.ott_itpr);
            hoder.tv_type =convertView.findViewById(R.id.ott_ittp);
            hoder.tv_finishetime =convertView.findViewById(R.id.ott_itfintime);
            hoder.btn_receive =convertView.findViewById(R.id.ott_itre);
            convertView.setTag(hoder);
        }else {
            hoder = (ViewHodler) convertView.getTag();
        }
        task =mTask.get(position);
        log.i("okhttpTask","这是结果other"+task.toString());
        hoder.tv_request.setText("               "+task.getRequest());
        hoder.tv_price.setText("价格："+task.getPrice());
        hoder.tv_type.setText("类型："+task.getType());
        hoder.tv_finishetime.setText("完成期限:      "+task.getEndtime());
        hoder.btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //领取监听
                log.i("task_info",
                        task.getId()+"      "+
                        task.getPrice()+"     "+task.getType()+"      "+task.getEndtime());
                Message message =new Message();
                message.obj = mTask.get(position);
                handler.sendMessage(message);
            }
        });
        return convertView;
    }
    class ViewHodler{
        TextView tv_request,tv_price,tv_type,tv_finishetime;
        Button btn_receive;
    }
}
