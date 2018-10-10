package zfl.four.downloadService.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import zfl.four.downloadService.bean.FileInfo;
import zfl.four.downloadService.bean.ThreadInfo;


public class download {
    private FileInfo mFileInfo;
    private Context mContext;
    private ThreadDAO mThreadDAO;
    private int mFinished;//下载进度
    public boolean isPause = false;//是否暂停

    public download(FileInfo mFileInfo, Context mContext) {
        this.mFileInfo = mFileInfo;
        this.mContext = mContext;
        mThreadDAO = new ThreadDAOImpl(mContext);
    }
    //启动下载
    public void download(){
        List<ThreadInfo> threadInfos = mThreadDAO.query(mFileInfo.getUrl());
        ThreadInfo threadInfo ;
        if (threadInfos.size() == 0){
            //初始化线程
             threadInfo = new ThreadInfo(0,mFileInfo.getUrl(),0,mFileInfo.getLength(),0);
        }else {
            threadInfo = threadInfos.get(0);
            mFileInfo.setFinished(threadInfo.getFinished());
        }
        downloadThread dt = new downloadThread(threadInfo);
        dt.start();
    }
    class  downloadThread extends Thread{
        private ThreadInfo threadInfo;
        private boolean isFinished  =false;//标识线程结束
        public downloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            //插入线程信息
            if (!mThreadDAO.isExists(threadInfo.getUrl(),threadInfo.getId())){
                mThreadDAO.insert(threadInfo);
            }
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
                File file = new File(downloadService.DOWNLOAD_PATAH,mFileInfo.getName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);
                //开始下载
                Intent intent = new Intent(downloadService.ACTION_UPDATE);
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
                        mFinished +=len;
                        if (System.currentTimeMillis() - time>100){
                            //间隔超过100毫秒执行ui更新
                            time = System.currentTimeMillis();//更新时间
                            //进度值不转化会遭遇int溢出
                            intent.putExtra("finished",(int)(1.00*mFinished*100/(1.00*mFileInfo.getLength())));
                            mContext.sendBroadcast(intent);
                        }
                        //在暂停时保存进度
                        if (isPause){
                            mThreadDAO.update(threadInfo.getUrl(),threadInfo.getId(),mFinished);
                            return;
                        }
                    }
                    //删除线程信息
                    mThreadDAO.delete(threadInfo.getUrl(),threadInfo.getId());
                    isFinished =true;//线程结束，任务结束
                    if (isFinished==true){
//                        Log.i("zfl", "结束了");
                        Intent intent1 = new Intent(downloadService.ACTION_FINISHED);
                        intent.putExtra("fileInfo",mFileInfo);
                        mContext.sendBroadcast(intent1);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    conn.disconnect();
                    is.close();
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//finally
        }//run
    }
}
