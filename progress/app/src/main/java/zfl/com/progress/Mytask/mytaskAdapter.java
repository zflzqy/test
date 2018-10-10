package zfl.com.progress.Mytask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import zfl.com.progress.util.Datechange;
import zfl.com.progress.Bean.Task;
import zfl.com.progress.R;
import zfl.com.progress.util.log;

public class mytaskAdapter extends BaseAdapter {
    private List<Task> mTask;
    private LayoutInflater mInflater;
    private Context context;
    private ViewHodler hoder;
    private Handler handler;
    private Task task;

    public mytaskAdapter(Context context, List<Task> mTask, Handler handler) {
        this.context = context;
        this.mTask = mTask;
        this.handler = handler;
        mInflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        hoder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.frg_myadpter, null);
            hoder = new ViewHodler();
            hoder.tv_request = convertView.findViewById(R.id.myt_itrq);
            hoder.tv_price = convertView.findViewById(R.id.myt_itpr);
            hoder.tv_type = convertView.findViewById(R.id.myt_ittp);
            hoder.tv_starttime = convertView.findViewById(R.id.myt_itst);
            hoder.endtime = convertView.findViewById(R.id.myt_itet);
            hoder.btn_giveup = convertView.findViewById(R.id.myt_itgu);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHodler) convertView.getTag();
        }
        task = mTask.get(position);
        log.i("okhttpTask", "这是结果" + task.getRequest());
        hoder.tv_request.setText("               " + task.getRequest());
        hoder.tv_price.setText("价格：" + task.getPrice());
        hoder.tv_type.setText("类型：" + task.getType());
        if (task.getStarttime() != null) {
            hoder.tv_starttime.setText("开始时间：" +
                    task.getStarttime().substring(0, task.getStarttime().length() - 2));
            //设置结束时间
            try {
                log.i("start", task.getStarttime());
                Date start = Datechange.stringToDate(task.getStarttime());
                Date end =Datechange.getDateAfter(start, task.getEndtime());
                hoder.endtime.setText("结束时间："+Datechange.dateToString(end));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            hoder.tv_starttime.setText("开始时间：未被领取");
        }
//        hoder.endtime
        hoder.btn_giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //放弃逻辑
                log.i("task", "" + position);
                Message message = new Message();
                message.obj = mTask.get(position);
                handler.sendMessage(message);
            }
        });

        return convertView;
    }


    class ViewHodler {
        TextView tv_request, tv_price, tv_type, tv_starttime;
        Button btn_giveup;
        TextView endtime;
    }
}
