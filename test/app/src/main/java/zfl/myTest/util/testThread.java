package zfl.myTest.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class testThread extends Thread {
    private String  name;
    private String password;
    private String url;

    public testThread(String name, String password, String url) {
        this.name = name;
        this.password = password;
        this.url = url;
    }
    //无参构造
    public testThread(String url,String name) {
        this.url =url;
        this.name =name;
    }

    @Override
    public void run() {
//        try {
//            connectionDoget();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        connectionDopost();
        postFileName(name);//上传文件名
        Log.i("zxc", "run: "+name );
    }
    //连接测试,dopost方法不用转码，服务器端也不要转码
    private void connectionDopost() {
        HttpURLConnection conn=null;
        BufferedReader br=null;
        try {
            URL httpurl = new URL(url);
            conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            OutputStream out = conn.getOutputStream();
            String content ="name="+name+"&password="+password;
            out.write(content.getBytes());
            br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String s;
            while ((s=br.readLine())!=null){
                sb.append(s);
            }
            Log.i("client", "这是客户端发送的数据"+name+"      "+password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //连接测试,doget用转码，不然会有io异常
    private void connectionDoget() throws UnsupportedEncodingException {
        url +="?name="+URLEncoder.encode(name,"utf-8")+"&password="+password;
        try {
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String s;
            while ((s=br.readLine())!=null){
                sb.append(s);
            }
            Log.i("client", "这是客户端发送的数据"+name+"      "+password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //上传文件名
    private void postFileName(String name) {
        HttpURLConnection conn=null;
        try {
            URL httpurl = new URL(url);
            conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            OutputStream out = conn.getOutputStream();
            String content ="name="+name;
            out.write(content.getBytes());
            conn.getInputStream();
            Log.i("zxc", "postFileName: "+name+"            "+url );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null)
            conn.disconnect();
        }
    }
}
