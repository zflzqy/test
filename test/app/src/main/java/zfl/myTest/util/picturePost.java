package zfl.myTest.util;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
* 文件上传类
* */
public class picturePost {
    private static final int TIME_OUT = 10000;//超时时间
    private static final String CHARSET ="utf-8";//编码方式
    private static final String METHOD ="POST";//请求方式
    private static final boolean SUCCESS = true;//成功标志
    private static final boolean fail = false;//失败标志

    public picturePost() {
        //静态块先于构造函数执行,静态块直接用类名调用
    }
    //上传文件操作,
    public  static  boolean uploadFile(String url,String fileName){
        boolean issuccess = false;
        url=url+"?name="+getFileName(fileName)+"&password="+"123456";//带参上传文件
        String content_type ="application/x-www-form-urlencoded";//发送文件类型
        HttpURLConnection conn =null;//连接
        OutputStream out =null;//文件输出流
        BufferedOutputStream bos =null;//缓冲区
        InputStream in =null;//文件输入流
        try {
            URL htppurl = new URL(url);
            conn = (HttpURLConnection) htppurl.openConnection();
            conn.setConnectTimeout(TIME_OUT);
            conn.setRequestMethod(METHOD);
            conn.setRequestProperty("Charset",CHARSET);
            //在一次TCP连接中可以持续发送多份数据而不会断开连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content_type",content_type);//标准拼接格式
            if (fileName!=null)//文件存在
            {
                out =conn.getOutputStream();//获得输出流
                bos = new BufferedOutputStream(out);//获取输出缓冲
                //开始从文件中读取数据并写入到服务器
                in = new FileInputStream(new File(fileName));
                byte[] bytes = new  byte[4*1024];//输入缓冲区
                int len =0;
                while ((len=in.read(bytes))!=-1)//读到byets缓冲数组内
                {
                    bos.write(bytes,0,len);//写到服务器内
                }
                bos.flush();//缓冲区数据全部输出
                //获得文件上传结果码
                int result = conn.getResponseCode();
                if (result==200){
                    issuccess = true;
                }
                else {
                    issuccess =false;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //释放资源
            if (conn!=null)
                conn.disconnect();
            try {
                if (out!=null)
                    out.close();
                if (bos!=null)
                    bos.close();
                if (in!=null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//finally语句块
        return issuccess;
    }
    //获取文件名
    private static String getFileName(String path){
        return path.substring(path.lastIndexOf("/")+1);
    }
    /*
    * http上传文件格式    *
    * */
//    Accept: text/plain, */*
//Accept-Language: zh-cn
//Host: 192.168.24.56
//Content-Type:multipart/form-data;boundary=-----------------------------7db372eb000e2
//User-Agent: WinHttpClient
//Content-Length: 3693
//Connection: Keep-Alive
//
//-------------------------------7db372eb000e2
//
//Content-Disposition: form-data; name="file"; filename="kn.jpg"
//
//Content-Type: image/jpeg
//
//(此处省略jpeg文件二进制数据...）
//
//-------------------------------7db372eb000e2--
}
