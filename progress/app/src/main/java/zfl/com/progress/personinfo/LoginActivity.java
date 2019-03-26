package zfl.com.progress.personinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_account, et_password;
    private Button btn_login;
    private TextView tv_newregister,tv_loading;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        init();
        // 获取权限
        request_permissions();

    }
    //初始化控件
    private void init() {
        et_account = findViewById(R.id.login_et_useraccount);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        tv_newregister = findViewById(R.id.login_tv_newregister);
        tv_loading = findViewById(R.id.login_loading);

        //控件监听
        btn_login.setOnClickListener(this);
        tv_newregister.setOnClickListener(this);

    }

    //控件点击处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录处理
            case R.id.login_btn_login:
                tv_loading.setVisibility(View.VISIBLE);//显示加载tv
                tv_loading.setText("");
                login();//登录
                break;
            case R.id.login_tv_newregister:
                Intent intent = new Intent(this, RegistererActivity.class);
                startActivity(intent);
                finish();//结束当前活动
                break;
        }
    }

    private void login() {
        try {
            user = new User();
            String account = "", password = "";
            account = et_account.getText().toString();
            password = et_password.getText().toString();
            if (account.equals("") && password.equals("")) {
                updateUi("账号或密码不能为空");
                return;
            }
            user.setAccount(Integer.parseInt(account));
            user.setPassword(password);
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpUser okhttpUser = new okhttpUser(user);
                        user = okhttpUser.login();
                        log.i("RS",constant.RS+"");
                        constant.RS = okhttpUser.getResult();
                        log.i("RS",constant.RS+"");
                        if (2 == constant.RS)//如果成功
                        {
                            // 启动个人中心
                            Intent intent = new Intent(LoginActivity.this, MainviewActivity.class);
                            Bundle bundle = new Bundle();
//                            bundle.putString("ACTION", constant.ACTION_PERCENTER);
                            bundle.putSerializable("user",user);//获取新的ueer对象
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();//结束当前活动
                        } else if (3 == constant.RS) {
                            updateUi("账号或密码错误");
                        } else if (1 == constant.RS) {
                            updateUi("服务器连接失败");
                        }
                    }
                }).start();
            } finally {
                constant.RS = 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();//抛出int为空无法转换异常
        }

    }

    //主线程更新ui
    private void updateUi(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //先置空
                tv_loading.setText("");
                //再设置
                tv_loading.setText(content);
            }
        });
    }
    // 请求多个权限
    private void request_permissions() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        List<String> permissionList = new ArrayList<>();

        // 判断权限是否已经授予，没有就把该权限添加到列表中
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.CAMERA);
//        }
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
        // 支付接口申请手机读写权限和外部存储卡写权限
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
            // 获取存储卡写入权限
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)
                !=PackageManager.PERMISSION_GRANTED){
            // 手机通讯读取权限
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), 1002);
        } else {
//            Toast.makeText(this, "多个权限你都有了，不用再次申请", Toast.LENGTH_LONG).show();
        }
    }
    // 请求权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                // 1002请求码对应的是申请多个权限
                if (grantResults.length > 0) {
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                Toast.makeText(this, permissions[i] + "存储卡读写权限被拒绝了，请手动赋予", Toast.LENGTH_LONG).show();
                            }else if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)){
                                Toast.makeText(this, permissions[i] + "手机通讯录权限被拒绝了，请手动赋予", Toast.LENGTH_LONG).show();
                            }else if (permissions[i].equals(Manifest.permission.CAMERA)){
                                Toast.makeText(this, permissions[i] + "相机调用权限被拒绝了，请手动赋予", Toast.LENGTH_LONG).show();
                            }
                            // 执行返回个人中心函数
                            finish();
                        }
                    }
                }
                break;
        }
    }
}
