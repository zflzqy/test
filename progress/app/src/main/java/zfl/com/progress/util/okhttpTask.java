package zfl.com.progress.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;

public class okhttpTask {

    private OkHttpClient okHttpClient;
    private List<Task> mTask;//任务信息
    private String rs;//服务器返回结果

    public okhttpTask() {
        // 初始化okhttp
        okHttpClient =new OkHttpClient();
    }

    // 获取可领取(已领,我的任务，已完成任务)任务
    public List<Task> doGetAlltask(int account,String url) {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", String.valueOf(account))
                .build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            log.i("okhttpTask","这是结果"+rs);
            mTask =new ArrayList<>();// 将数据解析成list
            rs = JSONObject.parseObject(rs).getString("allTasks");
            mTask = JSONObject.parseArray(rs,Task.class);
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mTask;
    }

    // 发布(放弃，领取，完成,同意放弃,评价)任务
    public String dealtask(Task task,String url){
        // 解析成字符串发送，后台逆转
        String t = JSONObject.toJSONString(task);
        RequestBody requestBody =new FormBody.Builder()
                .add("task",t).build();
        Request request =new Request.Builder().url(url).post(requestBody).build();
        Call call =okHttpClient.newCall(request);
        try {
            Response  response=call.execute();//执行
            rs = response.body().string();//服务器返回的数据，只调用一次
            rs = JSONObject.parseObject(rs).getString("tasks");
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }
    // 获取user信息
    public User doGetuser(int account,String url) {
        User user = new User();
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account+"").build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            log.i("okhttpTask","这是结果"+rs);
            // 获取user
            rs =JSONObject.parseObject(rs).get("user").toString();
            user = JSONObject.parseObject(rs,User.class);
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
    // 获取支付信息
    public String getPayInfo(Task task,String url){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", String.valueOf(task.getReceive_account()))
                .add("tid",String.valueOf(task.getId()))
                .build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            // 获取支付信息
            rs =JSONObject.parseObject(rs).get("payInfo").toString();
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  rs;
    }
    // 上传支付结果
    public String uploadPayResult(String result,String tid, String url){
        RequestBody requestBody = new FormBody.Builder()
                .add("result", result)
                .add("tid",tid)
                .build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            // 获取支付信息
            rs =JSONObject.parseObject(rs).get("payInfo").toString();
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  rs;
    }
    // 获取用户二维码图片url
//    public String doGetImageUrl(int account,int tid, String url) {
//        RequestBody requestBody = new FormBody.Builder()
//                .add("account", String.valueOf(account))
//                .add("tid", String.valueOf(tid))
//                .build();
//        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        //创建一个call对象,参数就是Request请求对象
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            log.i("okhttpTask","这是结果"+rs);
//            rs =response.body().string();
//            rs = JSONObject.parseObject(rs).getString("pic").toString();
//            // 获取图片的解析的url，服务端解析
//        } catch (SocketException e){
//            e.printStackTrace();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return rs;
//    }
//    // 获取用户二维码图片
//    public Bitmap doGetImage(int account, String url) {
//        Bitmap bitmap = null;
//        RequestBody requestBody = new FormBody.Builder()
//                .add("account", account+"").build();
//        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        //创建一个call对象,参数就是Request请求对象
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            log.i("okhttpTask","这是结果"+rs);
//            bitmap= BitmapFactory.decodeStream(response.bodpuy().byteStream());
//            // 获取图片的解析的url，服务端解析
//        } catch (SocketException e){
//            e.printStackTrace();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
}
