package zfl.android_one.http;

import android.os.Handler;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class http_webview extends Thread {
    private String url;
    private WebView webView;
    private Handler handler;



    public http_webview(String url, WebView webView, Handler handler) {
        this.url = url;
        this.webView = webView;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            URL httpurl = new URL(url);
            URLConnection conn = httpurl.openConnection();
//            conn.setReadTimeout(1000);//请求超时时间
            final StringBuffer s = new StringBuffer();
            BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String r;
            while ((r=read.readLine())!=null){
                s.append(r);
            }
            webView.post(new Runnable() {
                @Override
                public void run() {
                webView.loadData(s.toString(),"text/html;charset=utf-8",null);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
