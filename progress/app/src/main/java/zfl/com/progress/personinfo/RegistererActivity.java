package zfl.com.progress.personinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.okhttpUser;

public class RegistererActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_account, et_password, et_confirm;
    private Button btn_register;
    private TextView tv_haveaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerer);
        //初始化控件
        init();
    }

    //初始化控件
    private void init() {
        et_account = findViewById(R.id.register_et_account);
        et_password = findViewById(R.id.register_et_password);
        btn_register = findViewById(R.id.register_btn_register);
        tv_haveaccount = findViewById(R.id.register_tv_haveaccount);
        et_confirm = findViewById(R.id.register_et_confirmpassword);

        //监听控件
        btn_register.setOnClickListener(this);
        tv_haveaccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn_register:
                reg();//注册
                break;
            //返回登录
            case R.id.register_tv_haveaccount:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();//结束当前活动
                break;
        }
    }

    private void reg() {
        final User user = new User();
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        final String confirm = et_confirm.getText().toString();
        if (account.length() <= 10 && account.length() >= 6) {
            user.setAccount(Integer.parseInt(et_account.getText().toString()));
        } else {
            Toast.makeText(this, "账号长度不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() >= 6 && password.length() <= 20) {
            if (password.equals(confirm)) {
                user.setPassword(et_password.getText().toString());
            } else {
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "密码长度不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    okhttpUser okhttpUser = new okhttpUser(user);
                    okhttpUser.register();
                    constant.RS = okhttpUser.getResult();
                    if (4 == constant.RS)//如果成功注册
                    {
                        // 启动个人登录
                        Intent intent = new Intent(RegistererActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();//结束当前活动
                    } else if (5 == constant.RS) {
                        updateUi("账号已存在");
                    } else if (1 == constant.RS) {
                        updateUi("服务器连接失败");
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            constant.RS=0;
        }
    }

    //主线程更新ui
    private void updateUi(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegistererActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
