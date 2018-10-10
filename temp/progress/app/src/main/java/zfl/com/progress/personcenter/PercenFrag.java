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
import android.widget.Toast;

import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;
import zfl.com.progress.MainviewActivity;
import zfl.com.progress.R;
import zfl.com.progress.personinfo.LoginActivity;
import zfl.com.progress.util.log;

public class PercenFrag extends Fragment implements View.OnClickListener {
    private ImageView iv_perfect;
    private TextView  tv_mytask,tv_receive,tv_appraise,tv_chglogin,tv_perfect;
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
        user = (User) intent.getSerializableExtra("user");
        if (user.getSchool()==null || user.getPath()==null) {
            MainviewActivity.perfect=false;
            tv_perfect.setTextSize(20);
            tv_perfect.setText("请完善必要信息");
        }
        log.i("user_pf",user.toString());
    }

    private void initEvent() {
        iv_perfect.setOnClickListener(this);
        tv_mytask.setOnClickListener(this);
        tv_chglogin.setOnClickListener(this);
        tv_perfect.setOnClickListener(this);
    }

    private void init(View view) {
        iv_perfect =view.findViewById(R.id.percen_perfect);
        tv_mytask=view.findViewById(R.id.percen_tv_mytask);
        tv_receive =view.findViewById(R.id.percen_tv_receive);//接收的任务
        tv_appraise =view.findViewById(R.id.percen_tv_appraise);//待评价
        tv_chglogin = view.findViewById(R.id.percen_tv_chalogin);
        tv_perfect =view.findViewById(R.id.percen_tv_perfectInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.percen_perfect:
                startOtherpage(constant.ACTION_PERTINFO,PectshowActivity.class);
                getActivity().overridePendingTransition(R.anim.right,R.anim.right);
                break;
            case R.id.percen_tv_chalogin:
                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                builder.setMessage("确定退出当前账号么？");
                builder.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startOtherpage("",LoginActivity.class);
                    }
                });
                builder.show();
                break;
        }
    }
    //启动其他活动页面
    private void startOtherpage(String ACTION,Class<?> activity){
        Intent intent =new Intent(getContext(),activity);
        intent.putExtra("ACTION", ACTION);
        intent.putExtra("user",user);
        getActivity().finish();
        startActivity(intent);
    }
}
