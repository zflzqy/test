package zfl.android_one;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import zfl.com.myapplication.R;

public class Artdialog extends AppCompatActivity implements View.OnClickListener{
    private Button btn_dialog;
    private Button btn_dialog1;
    private Button btn_dialog2;
    private Button btn_dialog3;
    private Button btn_dialog4;
    //单选对话框数组
    private  String[] danList={"男","女"};
    //多选对话框数组
    private  String[] duoList={"上网","睡觉","吃饭"};
    //列表
    private  String[] liebiaoList={"策划","测试","开发"};
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_artdialog);
        init();

    }


    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.btn_dialog:
                dialog();
                break;
            case R.id.btn_dialog1:
                dialog1();
                break;
            case R.id.btn_dialog2:
                dialog2();
                break;
            case R.id.btn_dialog3:
                dialog3();
                break;
            case R.id.btn_dialog4:
                dialog4();
                break;
        }
    }

    //初始化
    private void init() {
        btn_dialog = findViewById(R.id.btn_dialog);
        btn_dialog1 = findViewById(R.id.btn_dialog1);
        btn_dialog2 = findViewById(R.id.btn_dialog2);
        btn_dialog3 = findViewById(R.id.btn_dialog3);
        btn_dialog4 = findViewById(R.id.btn_dialog4);
        //监听
        btn_dialog.setOnClickListener(this);
        btn_dialog1.setOnClickListener(this);
        btn_dialog2.setOnClickListener(this);
        btn_dialog3.setOnClickListener(this);
        btn_dialog4.setOnClickListener(this);
    }
    //普通对话框
    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("对护框");
        builder.setIcon(R.drawable.a);
        builder.setMessage("普通的一个对话框");
        builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Artdialog.this,"点击确定",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Artdialog.this,"点击取消",Toast.LENGTH_SHORT).show();
            }
        });
        //两种写法
        builder.show();
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }
    //单选对话框
    private void dialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("单选对话框");
        builder.setIcon(R.drawable.b);
        //单选按钮对话框估计不能显示内容，要把setMessage去掉，显示的内容要放在数组里。
        builder.setSingleChoiceItems(danList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(Artdialog.this,"选择了"+danList[i],Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }
    //多选
    private void dialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("多选对话框");
        builder.setIcon(R.drawable.c);
        builder.setMultiChoiceItems(duoList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
           if (b){
            Toast.makeText(Artdialog.this,"喜欢"+duoList[i],Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(Artdialog.this,"不喜欢"+duoList[i],Toast.LENGTH_SHORT).show();
           }
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
               //关闭对话框
                dialog.dismiss();
            }
        });
        AlertDialog dialog2 = builder.create();
        dialog2.show();
    }
    //列表对话框
    private void dialog3() {
        AlertDialog.Builder builder =new  AlertDialog.Builder(this);
        builder.setTitle("列表对话框");
        builder.setIcon(R.drawable.ic_launcher_backgroun);
        builder.setItems(liebiaoList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Artdialog.this,"选择了"+liebiaoList[i],Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog3 = builder.create();
        dialog3.show();
    }
    //自定义对话框
    private void dialog4(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("自定义");
        builder.setIcon(R.drawable.ic_launcher_backgroun);
        builder.setView(view);
        AlertDialog dialog4 = builder.create();
        dialog4.show();
    }
}
