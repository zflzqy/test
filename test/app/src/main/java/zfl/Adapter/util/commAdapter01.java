package zfl.Adapter.util;

import android.content.Context;

import java.util.List;

import zfl.Adapter.bean.commbean;
import zfl.Adapter.util.commAdapter;
import zfl.Adapter.util.viewHolder;
import zfl.com.myapplication.R;

public class commAdapter01 extends commAdapter<commbean> {

    public commAdapter01(Context context, List<commbean> mLists,int item_id) {
        super(context,mLists,item_id);
    }

    @Override
    public void convert(viewHolder holder, commbean commbean) {
        holder.setText(R.id.comm_title,commbean.getTitle());
        holder.setText(R.id.comm_content,commbean.getContent());
        holder.setText(R.id.comm_time,commbean.getTime());
        holder.setText(R.id.comm_phone,commbean.getPhone());
    }
}
