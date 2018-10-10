package zfl.android_one;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import zfl.com.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class file extends AppCompatActivity {
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity);
        //用的Genymotion，文件也默认创建在/mnt/shell/emulated/0目录下
        File file = new File("/mnt/zfl");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(file.this,"文件已存在",Toast.LENGTH_LONG);
        }
        //得到当前文件目录
        file =this.getFilesDir();
        //当前程序下创建一个自己的目录app_zfl
        /*
        * MODE_PRIVATE:只能被当前程序访问，并且重新写入会覆盖原有的数据
        * MODE_APPEND：只能被当前程序访问，并且重新写入追加在原有数据后边
        * */
        File file1= this.getDir("zfl",MODE_ENABLE_WRITE_AHEAD_LOGGING);


        //文件在打他目录，往文件写东西
        FileOutputStream is;
        try {
            is = openFileOutput("zfl01",MODE_PRIVATE);
            String nmae = "赵飞龙";
            String age = "20";
            is.write(nmae.getBytes());
            is.write(age.getBytes());
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从文件读
        FileInputStream os;
        try {
            os = openFileInput("zfl01");
            byte[] buffer = new byte[os.available()];
            os.read(buffer);
            Log.i("xyz",new String(buffer));
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
