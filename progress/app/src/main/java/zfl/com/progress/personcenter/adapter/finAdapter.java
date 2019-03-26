package zfl.com.progress.personcenter.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.R;

public class finAdapter extends BaseAdapter {
    private List<Task> mTask;
    private LayoutInflater mInflater;
    private Task task;
    private viewHodler hodler;

    public finAdapter(Context context,List<Task> mTask) {
        mInflater =LayoutInflater.from(context);
        this.mTask = mTask;
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
        if (convertView==null){
            hodler =new viewHodler();
            convertView =mInflater.inflate(R.layout.frg_finadapter,null);
            hodler.tv_request =convertView.findViewById(R.id.fint_itrq);
            hodler.tv_type =convertView.findViewById(R.id.fint_ittp);
            hodler.tv_appriseLevel = convertView.findViewById(R.id.fint_itapplevel);
            hodler.tv_price = convertView.findViewById(R.id.fint_itpr);
            hodler.tv_finaccount =convertView.findViewById(R.id.fint_itaccount);
            hodler.tv_apprise =convertView.findViewById(R.id.fint_apprise);
            convertView.setTag(hodler);
        }else {
            hodler = (viewHodler) convertView.getTag();
        }
        task =mTask.get(position);//取出对应的item
        hodler.tv_request.setText("要求："+task.getRequest());
        hodler.tv_type.setText("类型："+task.getType());
        hodler.tv_price.setText("价格："+task.getPrice());
        if (task.getAppraiselevel()==null){
            hodler.tv_appriseLevel.setText("评价等级：无");
        }else {
            hodler.tv_appriseLevel.setText("评价等级："+task.getAppraiselevel());
        }
        hodler.tv_finaccount.setText("完成人账号："+task.getReceive_account());//完成人账号
        if (task.getAppraise()==null){
            // 转换null
            hodler.tv_apprise.setText("评价内容：");
        }else {
            hodler.tv_apprise.setText("评价内容："+task.getAppraise());
        }
        return convertView;
    }
    class viewHodler{
        TextView tv_request,tv_price,tv_appriseLevel,tv_type,tv_finaccount,tv_apprise;
    }
}
