package zfl.four.downloadService.util;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import zfl.four.downloadService.bean.FileInfo;


public class downloadService extends Service {
    public static final String ACTION_START = "ACTION_START";//开始动作
    public static final String ACTION_STOP = "ACTION_STOP";//停止动作
    public static final String ACTION_UPDATE = "ACTION_UPDATE";//进度
    public static final String ACTION_FINISHED = "ACTION_FINISHED";//进度
    public static final int MSG_INIT = 0;//HANDLER发送的值
    //getExternalStorageDirectory获取sdk根目录，getAbsolutePath//绝对路径，创建下载文件夹
    public static final String DOWNLOAD_PATAH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/";
    private download dlTask;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //必须写在return语句前
        if (ACTION_START.equals(intent.getAction()))
        {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            initThread initThread = new initThread(fileInfo);
            initThread.start();//启动线程
//            Log.i("abc", "ACTION_START"+fileInfo.toString());
        }else if (ACTION_STOP.equals(intent.getAction())){
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            if (dlTask!=null){
                dlTask.isPause = true;
            }
//            Log.i("abc", "ACTION_STOP"+fileInfo.toString());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    dlTask = new download(fileInfo,downloadService.this);
                    dlTask.download();
                    break;
            }
        }
    };
    class  initThread extends Thread{
        FileInfo fileInfo = new FileInfo();
        public initThread(FileInfo fileInfo){
            this.fileInfo = fileInfo;
        }
        @Override
        public void run() {
            //连接网络文件
            HttpURLConnection conn = null;//http连接
            RandomAccessFile raf = null;//随机读取文件位置
            try {
                URL url = new URL(fileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);//设置连接延迟超时
                conn.setRequestMethod("GET");//设置请求码，下载用get其他用post
                int LENGTH = -1;//文件长度
                //HttpURLConnection.HTTP_OK==200,206 httpStutas_sc_partial_content
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    LENGTH = conn.getContentLength();//获取文件长度
                }
                if (LENGTH<=0){
                    return;
                }
                File dir = new File(DOWNLOAD_PATAH);
                if (!dir.exists()){
                    dir.mkdir();
                }
                File file = new File(dir,fileInfo.getName());
                raf = new RandomAccessFile(file,"rwd");//rwd 代表read ，write, delete
                raf.setLength(LENGTH);
                fileInfo.setLength(LENGTH);//将长度设置给fileInfo回传
                mHandler.obtainMessage(MSG_INIT,fileInfo).sendToTarget();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    raf.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//finally
        }//initThread run语句
        class  connraf{
            public HttpURLConnection conn;
            public RandomAccessFile raf;
        }
    }
}
