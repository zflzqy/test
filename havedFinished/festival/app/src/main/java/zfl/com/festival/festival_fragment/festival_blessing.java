package zfl.com.festival.festival_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zfl.com.festival.Bean.festival;
import zfl.com.festival.ChooseMessageActivity;
import zfl.com.festival.R;


public class festival_blessing extends Fragment {
    private GridView mGridview;
    private LayoutInflater inflater;

    private ArrayAdapter<festival> mAdapter;
    private List<festival> festivals;

    public static final String FESTIVAL_NAME ="FESTIVAL_NAME";//节日名称
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_festival_blessing,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mGridview = view.findViewById(R.id.festival_blessing_gv);

        inflater = LayoutInflater.from(getActivity());
        iniData();
        //适配器配置
        mAdapter = new ArrayAdapter<festival>(getActivity(),-1,festivals)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView ==null){
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_festival_blessing,parent,false);
                    viewHolder.tv = convertView.findViewById(R.id.festival_blessing_tv);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tv.setText(getItem(position).getFestName());
                return convertView;
            }
            class ViewHolder
            {
                TextView tv;
            }
        };
        mGridview.setAdapter(mAdapter);//设置适配器
        //节日点击监听
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChooseMessageActivity.class);
                intent.putExtra(FESTIVAL_NAME,mAdapter.getItem(position).getFestName());
                startActivity(intent);
            }
        });
    }

    private void iniData() {
        festivals = new ArrayList<>();
        festivals.add(new festival("元旦"));
        festivals.add(new festival("春节"));
        festivals.add(new festival("清明节"));
        festivals.add(new festival("劳动节"));
        festivals.add(new festival("端午节"));
        festivals.add(new festival("国庆节"));

    }


}
