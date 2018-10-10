package zfl.Adapter.AsyncTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyAnsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private static final String TAG = "MyAnsyncTask";
    private Bitmap mBitmap = null;
    private ImageView mImageView;//传递进来的imageview控件
    private ProgressBar mProgress;//传递进来的ProgressBar控件
    private Handler mHandler = new Handler();//非ui线程无法更新ui
    private TextView mText;
    //创建构造方法执行传递值
    public MyAnsyncTask(ImageView mImageView,ProgressBar mProgress,TextView mText){
        this.mImageView = mImageView;
        this.mProgress = mProgress;
        this.mText = mText;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];//获取传递进来的第一个参数
        Bitmap bitmap = null;//创建需要返回的图片
        URLConnection connection;//创建url链接
        InputStream is;//输入流
        try {
            connection =new  URL(url).openConnection();//根据url打开链接
            is = connection.getInputStream();//输入流获取文件
            BufferedInputStream bis = new BufferedInputStream(is);//包装输入流
            bitmap = BitmapFactory.decodeStream(bis);//将流转化成位图
            is.close();//关闭流
            bis.close();//关闭包装流
        } catch (IOException e) {
            e.printStackTrace();
        }
        //模拟刻度值更新
        for (int i = 0; i <100 ; i++) {
            publishProgress(i);
        }
        return bitmap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "onPreExecute: ");
        mProgress.setVisibility(View.VISIBLE);
        mText.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //如果返回则置空
        if(isCancelled()){
            return;
        }
        final String progress = String.valueOf(values[0]);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mText.setText(progress);
            }
        });

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mProgress.setVisibility(View.GONE);
        mText.setVisibility(View.GONE);
        mImageView.setImageBitmap(bitmap);//更新图片
    }
}
