package zfl.android_one.http;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import zfl.com.myapplication.R;

public class http_01 extends AppCompatActivity {
    private WebView webView;
    private Handler handler = new Handler();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_http_01);
        webView = findViewById(R.id.webview1);
        //android:largeHeap="true"在manifest文件中加入获取大内存以显示百度图片
        new http_webview("http://www.baidu.com",webView,handler).start();
    }
}
