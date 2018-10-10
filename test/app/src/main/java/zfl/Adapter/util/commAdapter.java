package zfl.Adapter.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import zfl.com.myapplication.R;


public abstract class commAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflate;//protected子类可继承
    protected int item_id;//item布局
    protected List<T> mLists;//泛型数据集
    protected Context mContext;//上下文环境

    public commAdapter(Context context, List<T> mLists ,int item_id) {
        mInflate = LayoutInflater.from(context);
        //初始化三个对象，为viewholdert提供
        this.item_id = item_id;
        this.mContext = context;
        this.mLists = mLists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
        public  T getItem(int position) {
        return  mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder = zfl.Adapter.util.viewHolder.get(
                mContext,parent,position,item_id,convertView);
        convert(viewHolder,getItem(position));
        return viewHolder.getConverview();
    }
    public abstract void convert(viewHolder holder,T t);
}
