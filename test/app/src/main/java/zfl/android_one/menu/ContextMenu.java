package zfl.android_one.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import zfl.com.myapplication.R;

import java.util.ArrayList;

public class  ContextMenu extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_context_menu);
        init();
        this.registerForContextMenu(listView);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("上下文菜单项");
        menu.setHeaderIcon(R.mipmap.ic_launcher);
        //men.add四个参数，1：父容器默认，2：id值，不一样 3：排序 4；标题
//        menu.add(1,1,1,"复制");
//        menu.add(1,2,1,"粘贴");
//        menu.add(1,3,1,"剪切");
//        menu.add(1,4,1,"重命名");
          MenuInflater inflater = getMenuInflater();
          inflater.inflate(R.menu.menu_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //switch语句不放在return下
        switch (item.getItemId()){
            case R.id.context_item1:
                Toast.makeText(ContextMenu.this,"复制",Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_item2:
                Toast.makeText(ContextMenu.this,"粘贴",Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_item3:
                Toast.makeText(ContextMenu.this,"剪切",Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_item4:
                Toast.makeText(ContextMenu.this,"重命名",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void init() {
        listView = findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getdata());
        listView.setAdapter(adapter);
    }

    public ArrayList<String> getdata() {
        ArrayList<String> data = new ArrayList<>();
        for (int i= 0;i<5;i++){
            data.add("第"+i+"项");
        }
            return data;
    }
}
