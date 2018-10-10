package zfl.four.downloadServicemoreThread.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zfl.four.downloadServicemoreThread.bean.FileInfo;
import zfl.four.downloadServicemoreThread.bean.ThreadInfo;


public class downloadThread {
    private FileInfo mFileInfo;
    private Context mContext;
    private ThreadDAOThread mThreadDAO;
    private int mFinished;//下载进度
    public boolean isPause = false;//是否暂停
    private int mThreadCount = 1;//线程数
    private List<downloadthread> dts;//线程集合
    public static ExecutorService sExecutorService = Executors.newCachedThreadPool();//线程池，线程优化

    public downloadThread(FileInfo mFileInfo, Context mContext,int mThreadCount) {
        this.mFileInfo = mFileInfo;
        this.mContext = mContext;
        this.mThreadCount = mThreadCount;
        mThreadDAO = new ThreadDAOImplThread(mContext);
    }
    //启动下载
    public void download(){
        List<ThreadInfo> threadInfos = mThreadDAO.query(mFileInfo.getUrl());
        if (threadInfos.size() == 0){
            //获取每个线程下载长度
            int len = mFileInfo.getLength() / mThreadCount;
            for (int i = 0; i < mThreadCount; i++) {
                //创建线程信息
                ThreadInfo threadInfo = new ThreadInfo(i,mFileInfo.getUrl(),len*i,(i+1)*len-1,0);
                if (i == mThreadCount -1){
                    threadInfo.setStop(mFileInfo.getLength());//如果为最后一个线程则将文件长度赋值给结束位
                }
                threadInfos.add(threadInfo);
                //插入线程信息，数据库操作不放在线程内
                    mThreadDAO.insert(threadInfo);
            }
        }
        //启动多线程下载
        dts = new ArrayList<>();
        for (ThreadInfo threadInfo : threadInfos){
            downloadthread dt = new downloadthread(threadInfo);
//            dt.start();
            sExecutorService.execute(dt);//线程池启动线程
            dts.add(dt);//添加线程到线程集合
        }
    }
    //检查所有线程是否完毕
    private  synchronized  void  checkedAllThread(){
        boolean allFinished =true;//m默认全部完毕
        for (downloadthread dt : dts){
            //一个未完成，标识有线程未完成，完成isFinised为true,未完成为false !dt.isFinished= true代表未完成为真
            if (!dt.isFinished){
                allFinished = false;
            }
        }
        if (allFinished){
            //所有线程完毕，广播更新ui
            Intent intent = new Intent(downloadServiceThread.ACTION_FINISHED);
            intent.putExtra("fileInfo",mFileInfo);
            mContext.sendBroadcast(intent);
        }
        //删除线程信息
        mThreadDAO.delete(mFileInfo.getUrl());
    }
    class  downloadthread extends Thread{
        private ThreadInfo threadInfo;
        private boolean isFinished = false;//标识线程是否完毕
        public downloadthread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream is = null;
            try {
                URL url =new URL(threadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = threadInfo.getStart()+threadInfo.getFinished();
                conn.setRequestProperty("Range","bytes"+start+"-"+threadInfo.getStop());
                //设置文件位置
                File file = new File(downloadServiceThread.DOWNLOAD_PATAH,mFileInfo.getName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);
                //开始下载
                Intent intent = new Intent(downloadServiceThread.ACTION_UPDATE);
                mFinished += threadInfo.getFinished();
                //HttpURLConnection.HTTP_OK==200,用206出错，206详见 download
                if (conn.getResponseCode() == 200){
                    //读取数据
                    is = conn.getInputStream();
                    byte[] bytes = new byte[1024*4];//每次读取文件长度
                    int len = -1;//文件的长度
                    long time = System.currentTimeMillis();//获取当前线程毫秒时间
                    while ((len = is.read(bytes))!=-1){
                        // 写入文件
                        raf.write(bytes,0,len);
                        //把下载进度广播给activity
                        mFinished +=len;//累计整个文件进度
                        threadInfo.setFinished(threadInfo.getFinished()+len);//单个线程进度
                        if (System.currentTimeMillis() - time>500){
                            //间隔超过多少毫秒执行ui更新
                            time = System.currentTimeMillis();//更新时间
                            //进度值不转化会遭遇int溢出
                            intent.putExtra("finished",(int)(1.00*mFinished*100/(1.00*mFileInfo.getLength())));
                            intent.putExtra("id",mFileInfo.getId());
                            mContext.sendBroadcast(intent);
                        }
                        //在暂停时保存进度
                        if (isPause){
                            mThreadDAO.update(threadInfo.getUrl(),threadInfo.getId(),threadInfo.getFinished());
                            return;
                        }
                    }
                    isFinished =true;//标识线程完毕
                    checkedAllThread();//检查所有线程是否完毕
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    raf.close();
                    is.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//finally
        }//run
    }
}
