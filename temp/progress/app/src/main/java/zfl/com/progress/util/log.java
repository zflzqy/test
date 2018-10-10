package zfl.com.progress.util;

import android.util.Log;

/*
 * 自定义log日志
 * */
public class log {
    private static final int VERBOSE = 1;//对应verbose级别日志
    private static final int DEBUG = 2;//对应debug级别日志
    private static final int INFO = 3;//对应info级别的日志
    private static final int WARN = 4;//对应warn级别日志
    private static final int ERROR = 5;//对应error级别的日志
    //设置打印哪些级别的日志
    private static final int level = 1;

    //logv
    public static void v(String TAG, String msg) {
        if (level <= VERBOSE)
            Log.v(TAG, msg);
    }

    //logd
    public static void d(String TAG, String msg) {
        if (level <= DEBUG)
            Log.d(TAG, msg);
    }

    //logi
    public static void i(String TAG, String msg) {
        if (level <= INFO)
            Log.i(TAG, msg);
    }

    //logw
    public static void w(String TAG, String msg) {
        if (level <= WARN)
            Log.w(TAG, msg);
    }

    //loge
    public static void e(String TAG, String msg) {
        if (level <= ERROR)
            Log.e(TAG, msg);
    }
}
