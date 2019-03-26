package zfl.com.progress.util;


import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;

public class okhttpUser {
    private OkHttpClient okHttpClient;
    private User user;//user对象
    private String rs ="";//服务器返回结果
    private String u;//user解析成的字符串
    private static String failOrsuccess = "fail";//是否成功登录

    public okhttpUser(User user) {
        this.user = user;
        okHttpClient = new OkHttpClient();
        //解析user
        u = JSONObject.toJSONString(user);
    }

    // 登录
    public User login() {
        String rs = dogPostuser(constant.URL_login);
        if (rs.equals("loginfail")) {
            setFailOrsuccess("loginfail");//未查询到
        } else {
            setFailOrsuccess("successlogin");//查询成功
            user = JSONObject.parseObject(rs,User.class);
        }
        return user;
    }

    // 注册
    public void register() {
        String rs = dogPostuser(constant.URL_register);
        if (rs.equals("exists")) {
            setFailOrsuccess("exists");//账号已存在
        } else if (rs.equals("successregister")) {
            setFailOrsuccess("successregister");//注册成功

        }
    }
    // 修改密码
    public void  updatePassword(){
     String rs =dogPostuser(constant.URL_updatepassword);
     if (rs.equals("upfail")){
         setFailOrsuccess("upfail"); // 修改失败
     }else {
         setFailOrsuccess("upsuccess"); // 修改成功
     }
    }

    // 完善
    public void perfectInfo() {
        String rs = dogPostuser(constant.URL_perfectInfo);//上传图片
        if (rs.equals("successperfect")) {
            setFailOrsuccess("successperfect");
        }
        if (rs.equals("picfail")){
            setFailOrsuccess("picfail");
        }
        log.i("perfect",rs);
    }

    //上传user
    public String dogPostuser(String url) {
        RequestBody requestBody = new FormBody.Builder()
                .add("userStr", u).build();


        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {

            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            byte[] bytes = response.body().toString().getBytes();
            String passs = new String(bytes,"utf-8");
            log.i("okhttpUser", "这是结果码" + response.code());
            log.i("rs", rs);
            rs =JSONObject.parseObject(rs).get("user").toString();
            if (response.code() != 200||rs==null) {
                setFailOrsuccess("服务器连接错误");
                return rs;
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    //上传文件+信息
    private String dopostfile(String url) {
        File file = new File(user.getPath());
        if (!file.exists()) {
            log.i("okhttpUser", "文件不存在");
        }
        RequestBody filetBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        log.i("file", file.getName());
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userStr", u)
                .addFormDataPart("file", file.getName(), filetBody)
                .build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            rs =JSONObject.parseObject(rs).get("user").toString();
            if (response.code() != 200||rs==null) {
                setFailOrsuccess("服务器连接错误");
                return rs;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
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
        }else  if (failOrsuccess.equals("picfail")){
            return 7;//图片解析失败
        }else if (failOrsuccess.equals("upfail")){
            return  8; // 修改密码失败
        }else if (failOrsuccess.equals("upsuccess")){
            return  9; // 修改密码成功
        }
        else {
            return 1;// 服务器连接错误
        }
    }

    public static String getFailOrsuccess() {
        return failOrsuccess;
    }

    public static void setFailOrsuccess(String failOrsuccess) {
        okhttpUser.failOrsuccess = failOrsuccess;
    }

    //获取文件名
    private static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
