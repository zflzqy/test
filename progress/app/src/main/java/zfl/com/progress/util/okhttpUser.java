package zfl.com.progress.util;

import com.google.gson.Gson;

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
    private String rs;//服务器返回结果
    private String ACTION;
    private String u;//user解析成的字符串
    private static String failOrsuccess = "fail";//是否成功登录
    private Gson gson;

    public okhttpUser(User user, String ACTION) {
        this.user = user;
        this.ACTION = ACTION;
        okHttpClient = new OkHttpClient();
        //解析user
        gson = new Gson();
        u = gson.toJson(user);
    }

    //上传user
    public User dogPostuser() {
        RequestBody requestBody = new FormBody.Builder()
                .add("ACTION", ACTION)
                .add("user", u).build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(constant.URL_user).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            log.i("okhttpUser","这是结果码"+response.code());
            if(response.code()!=200){
                setFailOrsuccess("服务器连接错误");
                return null;
            }
            //登录
            if (ACTION.equals(constant.ACTION_LOGIN)) {
                if (rs.equals("loginfail")) {
                    setFailOrsuccess("loginfail");//未查询到
                } else {
                    setFailOrsuccess("successlogin");//查询成功
                    user = gson.fromJson(rs, User.class);
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
                rs = dopostfile();//上传图片
                if (rs.equals("successperfect")) {
                    setFailOrsuccess("successperfect");
                }
            }

        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    //上传文件+信息
    private String dopostfile() {
        log.i("okhttpUser", "" + ACTION + "USER:" + u);
        File file = new File(user.getPath());
        if (!file.exists()) {
            log.i("okhttpUser", "文件不存在");
        }
        RequestBody filetBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        log.i("file",file.getName());
        RequestBody requestBody =new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ACTION",ACTION)
                .addFormDataPart("user",u)
                .addFormDataPart("file",file.getName(),filetBody)
                .build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder()
                .url(constant.URL_user)
                .post(requestBody)
                .build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  rs;
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
        okhttpUser.failOrsuccess = failOrsuccess;
    }

    //获取文件名
    private static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
