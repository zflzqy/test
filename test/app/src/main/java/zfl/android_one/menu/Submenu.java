package zfl.android_one.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import zfl.com.myapplication.R;

public class Submenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_submenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu flie = menu.addSubMenu("文件");
        flie.add(1,1,1,"复制");
        flie.add(1,2,1,"粘贴");
        flie.add(1,3,1,"剪切");
        flie.add(1,4,1,"重命名");
        flie.setHeaderTitle("文件操作");//子菜单标题
        flie.setHeaderIcon(R.mipmap.ic_launcher);

        SubMenu editor = menu.addSubMenu("编辑");
        editor.add(2,1,1,"操作一");
        editor.add(2,2,1,"操作二");
        editor.add(2,3,1,"操作三");
        editor.setHeaderTitle("编辑操作");
        editor.setHeaderIcon(R.mipmap.ic_launcher);

        return super.onCreateOptionsMenu(menu);

    }
    //item菜单与context菜单不同
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId()==1){
            switch (item.getItemId()){
                case 1:
                    Toast.makeText(Submenu.this,"复制",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Submenu.this,"粘贴",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Submenu.this,"剪切",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(Submenu.this,"重命名",Toast.LENGTH_SHORT).show();
                    break;
            }
        }else if (item.getGroupId()==2) {
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(Submenu.this, "操作一", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Submenu.this, "操作二", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Submenu.this, "操作三", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
