package zfl.animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import zfl.com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class listview extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview);
        //用于测试aniMation的跳转显示
        listView=(ListView) findViewById(R.id.listview);
        List<String> list=new ArrayList<String>();
        for(int i=0;i<20;i++)
        {
            list.add("第"+i+"项");
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        LayoutAnimationController lac=new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }
}
