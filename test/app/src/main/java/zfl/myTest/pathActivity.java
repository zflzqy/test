package zfl.myTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import zfl.com.myapplication.R;

public class pathActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_cache,btn_files;
    private TextView tv_cache,tv_files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        init();
    }

    private void init() {
        btn_cache =findViewById(R.id.test_btn_getcache);
        btn_files = findViewById(R.id.test_btn_getfiles);
        tv_cache = findViewById(R.id.test_tv_cache);
        tv_files = findViewById(R.id.test_tv_files);

        btn_cache.setOnClickListener(this);
        btn_files.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_btn_getcache:
                String cache = String.valueOf(this.getCacheDir());
                Log.i("zxc", "getCacheDir:          "+cache);
                cache =this.getExternalCacheDir().getAbsolutePath();
                Log.i("zxc", "getCacheDir:          "+cache);
                break;
            case R.id.test_btn_getfiles:
                String files = String.valueOf(this.getFilesDir());
                Log.i("zxc", "getfiles:          "+files);
                files =this.getExternalFilesDir(null).getAbsolutePath();
                Log.i("zxc", "getfiles:          "+files);
                break;
        }
    }
    /*
    * Environment.getDataDirectory() = /data
    Environment.getDownloadCacheDirectory() = /cache
    Environment.getExternalStorageDirectory() = /mnt/sdcard
    Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
    Environment.getRootDirectory() = /system
    getPackageCodePath() = /data/app/com.my.app-1.apk
    getPackageResourcePath() = /data/app/com.my.app-1.apk
    getCacheDir() = /data/data/com.my.app/cache
    getDatabasePath(“test”) = /data/data/com.my.app/databases/test
    getDir(“test”, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
    getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
    getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/com.my.app/files/test
    getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
    getFilesDir() = /data/data/com.my.app/files
    * */
}
