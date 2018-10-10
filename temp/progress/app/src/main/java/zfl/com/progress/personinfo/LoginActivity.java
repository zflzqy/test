package zfl.com.progress.personinfo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.util.okhttp;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_account, et_password;
    private Button btn_login;
    private TextView tv_newregister, tv_loading;
    private User user;
    private Handler handler;
    private int rs;
    public static boolean finised = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        init();
    }

    //初始化控件
    private void init() {
        et_account = findViewById(R.id.login_et_useraccount);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        tv_newregister = findViewById(R.id.login_tv_newregister);
        tv_loading = findViewById(R.id.login_loading);
        handler = new Handler();
        rs = 0;

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
                login();//登录'
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
                        okhttp okhttp = new okhttp(user, constant.ACTION_LOGIN);
                        user = okhttp.dogPostuser();
                        rs = okhttp.getResult();
                        if (2 == rs)//如果成功
                        {
                            // 启动个人中心
                            Intent intent = new Intent(LoginActivity.this, MainviewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ACTION", constant.ACTION_PERCENTER);
                            bundle.putSerializable("user", user);//获取新的ueer对象
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();//结束当前活动
                        } else if (3 == rs) {
                            updateUi("账号或密码错误");
                        } else if (1 == rs) {
                            updateUi("服务器连接失败");
                        }
                    }
                }).start();
            } finally {
                rs = 0;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    //主线程更新ui
    private void updateUi(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_loading.setCompoundDrawables(null, null, null, null);//设置左边图片不显示
                tv_loading.setText(content);
            }
        });
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
