package zfl.myTest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLEncoder;

import zfl.com.myapplication.R;
import zfl.myTest.util.AlipayUtil;
import zfl.myTest.util.picturePost;
import zfl.myTest.util.testThread;
import zfl.myTest.util.test_getpicture;

public class myTest extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_name;
    private EditText edt_password;
    private Button commit;

    private Button test_btn_getpicture;
    private TextView getpicturesuccess;
    private int count =0;
    private Handler handler ;

    private Button test_btn_post;
    private TextView test_tv_post;
    private Handler post_handler;
    private Button btn_pay,btn_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        init();
        handler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int r = msg.what;
                count +=r;
                if (count==3){
                    getpicturesuccess.setText("success");
                }
            }
        };
    }
    //初始化控件
    private void init() {
        //往服务器上传数据(字符，int)控件
        edt_name = findViewById(R.id.test_name);
        edt_password = findViewById(R.id.test_password);
        commit =findViewById(R.id.test_commit);
        //从服务器下载数据控件
        getpicturesuccess =findViewById(R.id.picturesuccess);
        test_btn_getpicture = findViewById(R.id.test_getpicture);
        //往服务器上传文件
        test_btn_post =findViewById(R.id.test_btn_post);
        test_tv_post =findViewById(R.id.test_postpicture);

        btn_pay =findViewById(R.id.test_pay);
        btn_intent =findViewById(R.id.test_Intent);
        //控件监听以及初始化
        commit.setOnClickListener(this);
        test_btn_getpicture.setOnClickListener(this);
        test_btn_post.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_intent.setOnClickListener(this);
        post_handler = new Handler();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.test_commit:
                String url ="http://10.0.2.2:8080/test/test" ;
                testThread tt1=new testThread(
                        edt_name.getText().toString(),edt_password.getText().toString(),url);//顺序不能乱
                tt1.start();
                break;
            case  R.id.test_getpicture:
                    new Thread(){//主线程无法执行网络操作
                        @Override
                        public void run() {
                            String url_picture = "http://10.0.2.2:8080/test/images/test_picture.jpg";
                            test_getpicture tg = new test_getpicture(handler);
                            tg.downloadpicture(url_picture);
                        }

                    }.start();

                break;
            case R.id.test_btn_post:
                new Thread(){
                    @Override
                    public void run() {
                        String url_post = "http://10.0.2.2:8080/test/test_get";
                        File file = Environment.getExternalStorageDirectory();
                        File downloadfile = new File(file,"123123.jpg");
                        String fileName =downloadfile.getAbsolutePath();
                        Log.i("zxc", "run: "+fileName);
                        boolean success;
                        success =picturePost.uploadFile(url_post,fileName);
                        if (success){
                            post_handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    test_tv_post.setText("successs");
                                }
                            });
                        }
                    }
                }.start();
                break;
            case  R.id.test_pay:
//                支付宝操作
//                if(AlipayUtil.hasInstalledAlipayClient(this)){
//                AlipayUtil.startAlipayClient(this, "https://QR.ALIPAY.COM/FKX07350DRTTIO1IV0L15F?t=1534813624785");
//                //第二个参数代表要给被支付的二维码code  可以在用草料二维码在线生成
//            }else{
//                Toast.makeText(this,"跳转失败",Toast.LENGTH_SHORT).show();
////                    SnackbarUtil.showShort(getActivity().getWindow().getDecorView(), "没有检测到支付宝客户端");
//            }
//            d第二种
                openAliPay2Pay("https://QR.ALIPAY.COM/FKX07350DRTTIO1IV0L15F?t=1534813624785");
                break;
            case R.id.test_Intent:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent =new Intent(myTest.this,pathActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
                break;
        }
    }


    /**
     * 支付
     *
     * @param qrCode
     */
    private void openAliPay2Pay(String qrCode) {
        if (openAlipayPayPage(this, qrCode)) {
            Toast.makeText(this, "跳转成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean openAlipayPayPage(Context context, String qrcode) {
        try {
            qrcode = URLEncoder.encode(qrcode, "utf-8");
        } catch (Exception e) {
        }
        try {
            final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode;
            openUri(context, alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private static void openUri(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        context.startActivity(intent);
    }


}
