package zfl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zfl.Adapter.bean.basebean;
import zfl.com.myapplication.R;
public class baseAapter extends BaseAdapter {
    private List<basebean> mBasebean;
    private LayoutInflater mInflater;
    public baseAapter(Context context,List<basebean> mBasebean){
        this.mBasebean = mBasebean;
        mInflater = LayoutInflater.from(context);
    }
    //baseAadpter
    @Override
    public int getCount() {
        return mBasebean.size();
    }

    @Override
    public Object getItem(int position) {
        return mBasebean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHouder viewHouder;
        if (convertView==null){
            viewHouder = new viewHouder();
            convertView = mInflater.inflate(R.layout.item_base,null);
            viewHouder.image = convertView.findViewById(R.id.item_base_image);
            viewHouder.title =convertView.findViewById(R.id.item_base_title);
            viewHouder.content = convertView.findViewById(R.id.item_base_content);
            convertView.setTag(viewHouder);
        }
        else {
           viewHouder= (viewHouder) convertView.getTag();
        }


        basebean bb = mBasebean.get(position);
        viewHouder.image.setImageResource(bb.imgae);
        viewHouder.title.setText(bb.title);
        viewHouder.content.setText(bb.content);
        return convertView;

    }
    class viewHouder{
        ImageView image;
        TextView title,content;
    }
}
