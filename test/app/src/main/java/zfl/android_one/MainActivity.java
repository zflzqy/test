package zfl.android_one;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import zfl.Adapter.AsyncTask.ansyTaskActivity;
import zfl.com.myapplication.R;
import zfl.four.downloadService.downloadActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button ansyTask,service_download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity);
        ansyTask = findViewById(R.id.btn_ansyTask);
        ansyTask.setOnClickListener(this);
        service_download = findViewById(R.id.btn_servicedownload);
        service_download.setOnClickListener(this);


        SQLiteDatabase db= openOrCreateDatabase("user.db",MODE_PRIVATE,null);
        db.execSQL("create table if not exists usertb (_id integer primary key autoincrement not null," +
                "name text not null,sex text, age integer)");
        db.execSQL("insert into usertb (name,sex,age) values ('第一个','男',20)");
        db.execSQL("insert into usertb (name,sex,age) values ('第二个','女',20)");
        db.execSQL("insert into usertb (name,sex,age) values ('第三个','男',30)");
        Cursor cursor =db.rawQuery("select *from usertb",null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                Log.i("xyz",cursor.getString(cursor.getColumnIndex("name")));
                Log.i("xyz",cursor.getString(cursor.getColumnIndex("sex")));
                Log.i("xyz",""+cursor.getInt(cursor.getColumnIndex("age")));
            }
        }
        cursor.close();
        db.close();

        SQLiteDatabase db1=openOrCreateDatabase("stu.db",MODE_PRIVATE,null);
        db1.execSQL("create table if not exists stutb(_id integer primary key autoincrement not null," +
                "name text not null, sex text,age integer)");
        ContentValues values = new ContentValues();
        values.put("name","one");
        values.put("sex","男");
        values.put("age",20);
        db1.insert("stutb",null,values);
        values.clear();
        values.put("name","two");
        values.put("sex","女");
        values.put("age",20);
        db1.insert("stutb",null,values);
        values.clear();
        values.put("name","three");
        values.put("sex","男");
        values.put("age",99);
        db1.insert("stutb",null,values);
        values.clear();
        values.put("sex","女");
        db1.update("stutb",values,"sex = ? ",new  String[]{"男"});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ansyTask:
                Intent intent = new Intent(MainActivity.this,ansyTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_servicedownload:
                Intent intent1 = new Intent(MainActivity.this,downloadActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
