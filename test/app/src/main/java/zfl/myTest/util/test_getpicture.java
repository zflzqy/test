package zfl.myTest.util;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class test_getpicture {
    private Handler handler;//handler更新ui
    //线程池
    private Executor threadPool = Executors.newFixedThreadPool(3);

    public test_getpicture(Handler handler) {
        this.handler = handler;
    }
    public void  downloadpicture(String url){
        try {
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            int content = conn.getContentLength();//获取文件长度
            int block  = content/3;//分成三份，每个线程下一份
            String fileName = getFilename(url);
            File parent = Environment.getExternalStorageDirectory();//得到父目录
            File file = new File(parent,fileName);//创建目录为parent,文件为fileName
            Log.i("zxc", "downloadpicture: "+file.getAbsolutePath());
            for (int i = 0;i<3;i++){
                long start = i*block;//开始位置
                long end = block*(i+1)-1;//结束位置
                if (i==2){
                    end = content;//最后一个线程的结束位置
                }
                downloadRun dr = new downloadRun(url,file.getAbsolutePath(),start,end,handler);

                threadPool.execute(dr);//线程池开启线程
            }//for循环
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }
    //实际下载操作
    static  class downloadRun implements Runnable{
        private  String url;
        private  String fileName;
        private  long start;
        private  long end;
        private  Handler handler;
        //构造函数
        public downloadRun() {
        }

        public downloadRun(String url, String fileName, long start, long end,Handler handler) {
            this.url = url;
            this.fileName = fileName;
            this.start = start;
            this.end = end;
            this.handler = handler;
        }

        @Override
        public void run() {
            InputStream in = null;
            try {
                URL httpurl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Range","bytes="+start+"-"+end);//设置读存的位置
                RandomAccessFile file= new RandomAccessFile(new File(fileName),"rwd");//随机读写文件
                file.seek(start);//每个线程先置于该线程的开始位置
                byte[] bytes = new  byte[4*1024];//字节流缓冲数组
                in = conn.getInputStream();//得到读入流
                int len =0;//读取的长度
                while ((len=in.read(bytes))!=-1)//-1代表读取完成，当没有内容时返回-1
                {
                    file.write(bytes,0,len);//从bytes中读，从bytes的0开始读，读len个长度
                }
                if (file!=null) {
                    file.close();
                }
                //文件下载完成后向handle发送消息
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                    if (in!=null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }//finally语句块
        }
    }
    //得到文件名
    private String getFilename(String url){
        return url.substring(url.lastIndexOf("/")+1);
    }
}
