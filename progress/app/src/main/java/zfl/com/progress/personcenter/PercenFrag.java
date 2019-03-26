package zfl.com.progress.personcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.personcenter.item.PectshowActivity;
import zfl.com.progress.personinfo.LoginActivity;

public class PercenFrag extends Fragment implements View.OnClickListener {
    private ImageView iv_perfect;
    private TextView  tv_exctask,tv_appraise,tv_pay,tv_finished,tv_chglogin,tv_perfect,tv_updatapassword;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_personcenter,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);//初始化控件
        initEvent();//监听控件
        Intent intent=getActivity().getIntent();
        try {
            user = (User) intent.getSerializableExtra("user");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (user.getSchool()==null || user.getPath()==null) {
            constant.perfect=false;
            tv_perfect.setTextSize(20);
            tv_perfect.setText("请点击右上角完善必要信息");
        }else{
            constant.perfect=true;
        }
    }


    private void init(View view) {
        iv_perfect =view.findViewById(R.id.percen_perfect);//个人信息
        tv_exctask =view.findViewById(R.id.percen_tv_exceptiontask);//异常任务
        tv_appraise =view.findViewById(R.id.percen_tv_appraise);//待评价
        tv_pay = view.findViewById(R.id.percen_tv_pay);
        tv_finished =view.findViewById(R.id.percen_tv_finish);//已完成任务
        tv_chglogin = view.findViewById(R.id.percen_tv_chalogin);//切换账号登录
        tv_perfect =view.findViewById(R.id.percen_tv_perfectInfo);//显示用户信息是否完善
        tv_updatapassword = view.findViewById(R.id.percen_tv_password); // 修改密码
    }
    private void initEvent() {
        iv_perfect.setOnClickListener(this);
        tv_exctask.setOnClickListener(this);
        tv_appraise.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        tv_finished.setOnClickListener(this);
        tv_chglogin.setOnClickListener(this);
        tv_updatapassword.setOnClickListener(this);
    }
    // code代表pectshow去启动哪个fragment
    // 0--->个人信息完善，1--》代表异常任务 2--》待评价 3--》已完成任务
    // 4--》退出登录（跳转login界面数字并没有什么用处）
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.percen_perfect:
                // 个人信息完善
                skip(0);
                break;
            case R.id.percen_tv_exceptiontask:
                // 跳转异常任务
                skip(1);
                break;
            case R.id.percen_tv_appraise:
                // 跳转待评价
                skip(2);
                break;
            case R.id.percen_tv_pay:
                skip(3);
                break;
            case R.id.percen_tv_finish:
                // 跳转已完成任务界面
                skip(4);
                break;
            case  R.id.percen_tv_password:
                // 跳转修改密码
                skip(5);
            case R.id.percen_tv_chalogin:
                // 切换登录
                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                builder.setMessage("确定退出当前账号么？");
                builder.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startOtherpage(4,LoginActivity.class);
                    }
                });
                builder.show();
                break;
        }
    }

    private void skip(int code) {
        startOtherpage(code,PectshowActivity.class);
        getActivity().overridePendingTransition(R.anim.right,R.anim.right);
    }

    //启动其他活动页面
    private void startOtherpage(int code,Class<?> activity){
        Intent intent =new Intent(getContext(),activity);
        intent.putExtra("code", code);
        intent.putExtra("user",user);
        getActivity().finish();
        startActivity(intent);
    }
}
