package zfl.com.progress.personcenter.item;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zfl.com.progress.Bean.constant;
import zfl.com.progress.R;
import zfl.com.progress.util.log;


public class PectshowActivity extends AppCompatActivity {
    private int code;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private pertInfonFrag perinfo;//个人信息完善
    private ExceTaskFrag exceTaskFrag;//异常任务
    private AppriseTaskFrag appriseTaskFrag;//待评价
    private PayTaskFrag payTaskFrag;// 待付款
    private FinishedTaskFrag finishedTaskFrag;//已完成任务
    private UpdatePasswordFrag updatePasswordFrag;// 修改密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_pectshow);
        Intent intent = getIntent();//获取意图，启动对应页面
        // 第二个参数代表默认值
        code = intent.getIntExtra("code",-1);
        fm = getSupportFragmentManager();//获得fragment管理器对象
        ft = fm.beginTransaction();
        if (code==0){
            // 获取权限
            // 获取权限
            request_permissions();
            //个人信息
            perinfo =new pertInfonFrag();
            ft.replace(R.id.pectshow,perinfo);
            ft.commit();
        }else if (code==1){
            //异常任务
            exceTaskFrag =new ExceTaskFrag();
            ft.replace(R.id.pectshow,exceTaskFrag);
            ft.commit();
        }else if (code==2){
            //待评价
            appriseTaskFrag =new AppriseTaskFrag();
            ft.replace(R.id.pectshow,appriseTaskFrag);
            ft.commit();
        }
        else if (code==3){
            //待付款
            payTaskFrag = new PayTaskFrag();
            ft.replace(R.id.pectshow,payTaskFrag);
            ft.commit();
        }
        else if (code==4){
            //已完成任务
            finishedTaskFrag = new FinishedTaskFrag();
            ft.replace(R.id.pectshow,finishedTaskFrag);
            ft.commit();
        }else if (code==5){
            // 修改密码
            updatePasswordFrag = new UpdatePasswordFrag();
            ft.replace(R.id.pectshow,updatePasswordFrag);
            ft.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                // 监听到返回按钮点击事件,后退
                if (code==0) {
                    perinfo.exit();//这里我们调用的perinfoFrament中的exit
                    return true;    //已处理
                }
                if (code==1) {
                    exceTaskFrag.exit();//这里我们调用的exceFrament中的exit
                    return true;    //已处理
                }

                if (code==2){
                    appriseTaskFrag.exit();//这里我们调用的appriseFrament中的exit
                    return  true;
                }
                if (code==3){
                    // 待付款
                    payTaskFrag.exit();//这里我们调用的appriseFrament中的exit
                    return  true;
                }
                if (code==4){
                    finishedTaskFrag.exit();//这里我们调用的finishedFrament中的exit
                    return true;
                }
                if (code==5){
                    updatePasswordFrag.exit();
                    return  true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
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
                            perinfo.exit();
                        }
                    }
                }
                break;
        }
    }

}
