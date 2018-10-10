package zfl.com.book.view.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import zfl.com.book.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> mList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里的root写成parent会只有一条数据
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        //布局点击事件,当item布局内的控件无法处理监听事件的时候就会调用布局监听事件
        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点击了item布局事件",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点击了item布局内的控件监听事件",Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    //  viewhodler类
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        private View mItemView;//item布局视图
        public ViewHolder(View itemView) {
            super(itemView);
             mItemView = itemView;
            tv = itemView.findViewById(R.id.item_recyler_tv);
        }
    }

    public MyAdapter(List<String> mList) {
        this.mList = mList;
    }

}
