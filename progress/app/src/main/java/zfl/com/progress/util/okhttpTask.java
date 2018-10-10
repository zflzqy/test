package zfl.com.progress.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zfl.com.progress.Bean.Task;
import zfl.com.progress.Bean.User;
import zfl.com.progress.Bean.constant;

public class okhttpTask {

    private OkHttpClient okHttpClient;
    private List<Task> mTask;//任务信息
    private String rs;//服务器返回结果
    private String ACTION;//请求动作
    private Task task;//请求任务信息
    private User user;//用户信息
    private String t;//task解析字符串
    private String u;//user解析字符串
    private Gson gson;//gson对象

    public okhttpTask(Task task, String ACTION) {
        this.task =task;
        this.ACTION =ACTION;
        okHttpClient =new OkHttpClient();

        user =new User();
        //解析task
        gson =new Gson();
        t =gson.toJson(task);
    }

    public okhttpTask(User user,String ACTION) {
        this.user = user;
        this.ACTION=ACTION;
        okHttpClient =new OkHttpClient();
        //解析task
        gson =new Gson();
        u =gson.toJson(user);
    }

    //task
    public List<Task> doGettask() {
        RequestBody requestBody = new FormBody.Builder()
                .add("ACTION", ACTION)
                .add("task", t).build();
        //创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(constant.URL_task).post(requestBody).build();
        //创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            rs = response.body().string();//服务器返回的数据，只调用一次
            log.i("okhttpTask","这是结果"+rs);
            mTask =new ArrayList<>();
            mTask =gson.fromJson(rs,new TypeToken<List<Task>>(){}.getType());//解析gson成list集合
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mTask;
    }
    //操作任务（发布，放弃，付款）
    public String task(){
        RequestBody requestBody =new FormBody.Builder()
                .add("ACTION",ACTION)
                .add("task",t).build();
        Request request =new Request.Builder().url(constant.URL_task).post(requestBody).build();
        Call call =okHttpClient.newCall(request);
        try {
            Response  response=call.execute();//执行
            rs =response.body().string();
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }
    //user
    public User doGetuser() {
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
            log.i("okhttpTask","这是结果"+rs);
            user =gson.fromJson(rs,User.class);
//            mTask =new ArrayList<>();
//            mTask =gson.fromJson(rs,new TypeToken<List<Task>>(){}.getType());//解析gson成list集合
        } catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
