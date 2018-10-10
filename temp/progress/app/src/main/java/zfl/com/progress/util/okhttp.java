package zfl.com.progress.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;

public class okhttp {
    private OkHttpClient okHttpClient;
    private User user;//user对象
    private String rs;//服务器返回结果
    private String ACTION;
    private String u;//user解析成的字符串
    private static String failOrsuccess = "fail";//是否成功登录
    private static String URL = "http://10.0.2.2:8080/progress/getPost";//本机地址
    private static String URL_s = "http://192.168.43.92:8080/progress/getPost";//共享地址
    private static String URL_yun = "http://132.232.41.248:8080/progress/getPost";//腾讯云地址
    private Map<String, User> result;//异步调用返回的数据

    public okhttp(User user, String ACTION) {
        this.user = user;
        this.ACTION = ACTION;
        okHttpClient = new OkHttpClient();
        //解析user
        Gson gson = new Gson();
        u = gson.toJson(user);
        URL =URL_yun;
    }

    //上传user
    public User dogPostuser() {
        RequestBody requestBody = new FormBody.Builder()
                .add("ACTION", ACTION)
                .add("user", u).build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(URL).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //请求加入调度,重写回调方法
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            //登录
            if (ACTION.equals(constant.ACTION_LOGIN)) {
                if (rs.equals("loginfail")) {
                    setFailOrsuccess("loginfail");//未查询到
                } else {
                    setFailOrsuccess("successlogin");//查询成功
                    Gson g = new Gson();
                    user = g.fromJson(rs, User.class);
                }
            }
            //注册
            else if (ACTION.equals(constant.ACTION_REGISTER)) {
                if (rs.equals("exists")) {
                    setFailOrsuccess("exists");//账号已存在
                } else if (rs.equals("successregister")) {
                    setFailOrsuccess("successregister");//注册成功

                }
            }
            //完善
            else if (ACTION.equals(constant.ACTION_PERFECT)) {
                if (rs.equals("successperfect")) {
                    dopostfile();//上传图片
                    setFailOrsuccess("successperfect");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    //上传文件+信息
    public void dopostfile() {
        log.i("okhttp", "" + ACTION + "USER:" + u);
        File file = new File(user.getPath());
        if (!file.exists()) {
            log.i("okhttp", "文件不存在");
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回结果
    public int getResult() {
        log.i("rs_fos", "" + failOrsuccess);
        //接收返回结果
        if (failOrsuccess.equals("successlogin")) {
            return 2;//成功登录
        } else if (failOrsuccess.equals("loginfail")) {
            return 3;//连接成功但账号密码不对
        } else if (failOrsuccess.equals("successregister")) {
            return 4;//成功注册
        } else if (failOrsuccess.equals("exists")) {
            return 5;//连接成功但账号已存在
        } else if (failOrsuccess.equals("successperfect")) {
            return 6;//成功修改
        } else {
            return 1;//未成功连接
        }
    }

    public static String getFailOrsuccess() {
        return failOrsuccess;
    }

    public static void setFailOrsuccess(String failOrsuccess) {
        okhttp.failOrsuccess = failOrsuccess;
    }

    //获取文件名
    private static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
