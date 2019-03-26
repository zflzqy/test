package zfl.com.progress.personcenter.item;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.util.log;
import zfl.com.progress.util.okhttpTask;
import zfl.com.progress.util.okhttpUser;

import static android.app.Activity.RESULT_OK;

public class pertInfonFrag extends Fragment implements View.OnClickListener {
    private EditText et_name,et_gather;
    private RadioGroup rg_sex;
    private SeekBar sb_age;
    private ImageView iv_back;
    private Button btn_perfect;
    private TextView age_show, tv_account, tv_school;
    private User user;
    private RadioButton rb;//单选按钮
    private boolean isPerfect = false;//是否提交修改
    private int image_code;//打开图库请求码
    private Bitmap bitmap; // 显示的图片
    private String[] school;
    private Handler handler;
    private File cacheFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_per_info, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);//初始化控件
        initEvent(view);//事件监听
        Intent intent = getActivity().getIntent();
        user = new User();
        if (intent.getSerializableExtra("user") != null) {
            user = (User) intent.getSerializableExtra("user");
            tv_account.setText("账号：" + user.getAccount());
            et_name.setText(user.getName());
            et_gather.setText(user.getPath());
            if (user.getAge() != null) {
                sb_age.setProgress(user.getAge());
            }
            age_show.setText(user.getAge() + "");
            if (user.getSex() != null && user.getSex().equals("女")) {
                rg_sex.check(R.id.info_rd_woman);//不为空，且为女
            } else {
                age_show.setText("0");
            }
            tv_school.setText(user.getSchool());

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_btn_perfect:
                perfect();//完善信息
                break;
            case R.id.info_iv_gather:
//                Intent intent_image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//系统图库
//                startActivityForResult(intent_image, image_code);//完善个人付款码
                break;
            case R.id.info_iv_back:
                //返回事件
                Intent intent = new Intent(getContext(), MainviewActivity.class);
                log.i("sex", "" + isPerfect);
                if (isPerfect) {//如果提交修改，//跳转个人中心
                    startActivity(intent, getvalue(user), R.anim.left);
                } else {//未提交修改
                    startActivity(intent, user, R.anim.left);
                }
                break;
            case R.id.info_tv_school:
                school = new String[]{"one", "two", "three", "four"};
                tv_school.setText("one");//设置默认
                schoollist(school);
                break;
        }
    }
    //控件初始化
    private void init(View view) {
        et_name = view.findViewById(R.id.info_ed_name);
        tv_school = view.findViewById(R.id.info_tv_school);
        rg_sex = view.findViewById(R.id.info_rg_sex);
        et_gather = view.findViewById(R.id.info_iv_gather);
        btn_perfect = view.findViewById(R.id.info_btn_perfect);
        sb_age = view.findViewById(R.id.info_sb_age);
        age_show = view.findViewById(R.id.info_tv_showage);
        rb = view.findViewById(R.id.info_rd_man);
        iv_back = view.findViewById(R.id.info_iv_back);
        tv_account = view.findViewById(R.id.info_tv_account);
        image_code = 1;
        handler = new Handler();
        //监听控件
        et_gather.setOnClickListener(this);
        btn_perfect.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_school.setOnClickListener(this);

    }

    //事件监听
    private void initEvent(final View view) {
        //年龄选择监听
        sb_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                age_show.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //性别选择监听
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();//获取选择的id
                rb = view.findViewById(id);//获取控件实例
            }
        });
    }

    //完善信息
    private void perfect() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //上传
                    log.i("okhttp_user", user.toString());
                    //更新user对象
                    okhttpUser okhttpUser = new okhttpUser(getvalue(user));
                    okhttpUser.perfectInfo();
                    constant.RS = okhttpUser.getResult();
                    if (6 == constant.RS)//如果成功修改
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "信息已完善", Toast.LENGTH_SHORT).show();
                            }
                        });
                        isPerfect = true;//成功修改
                        constant.perfect = true;
                    } else if (1 == constant.RS) {
                        Toast.makeText(getContext(), "connect fail", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            constant.RS = 0;
        }
    }

    //完善user对象
    private User getvalue(User user) {
        user.setAge(Integer.parseInt(age_show.getText().toString()));
        if (et_name.getText().toString().length() >= 2 && et_name.getText().toString().length() <= 10) {
            user.setName(et_name.getText().toString());
        } else {
            showTip("名字长度不合法");
        }
        log.i("school", tv_school.getText().toString());
        if (tv_school.getText() == "") {
            showTip("未选择学校");
        } else {
            user.setSchool(tv_school.getText().toString());
        }
        if (et_gather.getText().toString().length()>0){
            user.setPath(et_gather.getText().toString());
        }else {
            showTip("未填写支付宝账号");
        }
        user.setSex(rb.getText().toString());
        return user;
    }

    // 显示提示信息
    private void showTip(final String content) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //跳转页面
    private void startActivity(Intent intent, User user, int anim) {
        intent.putExtra("user", user);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(anim, anim);
    }

    //返回键监听
    public void exit() {
        iv_back.performClick();//代码实现被点击,返回键效果等同左上角返回按钮
    }

    //学校列表
    private void schoollist(final String[] List) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("选择你所在的学校");
        builder.setSingleChoiceItems(List, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tv_school.setText(List[i]);
            }
        });
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }


}

