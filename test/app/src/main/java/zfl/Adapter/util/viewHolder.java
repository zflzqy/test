package zfl.Adapter.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class viewHolder {
    private SparseArray<View> mViews;//存储控件集合
    private int mPosition;//位置
    private View mConverview;//convertview
    public viewHolder(Context context, ViewGroup parent, int position, int layoutId){
        //layoutId，item布局文件id
        mViews = new SparseArray<>();
        this.mPosition = position;
        mConverview = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConverview.setTag(this);//设置tag对比传统listview写法
    }
    //得到一个viewhoider对象
    public static viewHolder get(Context context, ViewGroup parent, int position, int layoutId, View convertview){
        if (convertview ==null){
            return new viewHolder(context,parent,position,layoutId);//为空则返回一个新的
        }
        else {
            viewHolder holder = (viewHolder) convertview.getTag();//不为空则通过getTag取出
            holder.mPosition = position;//复用也要更新位置
            return holder;
        }
    }

    public View getConverview() {
        return mConverview;
    }

    //通过viewid得到控价
    public  <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if (view == null){
            view = mConverview.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
    //设置textviewd的文本信息
    public viewHolder setText(int viewId, String text){
        TextView tv = getView(viewId);
        //必须通过getView将控件传如mViews不然在mViews中将不存在控件
//        TextView tv1 = (TextView) mViews.get(viewId);
        tv.setText(text);
        return  this;//返回viewholder
    }
}
