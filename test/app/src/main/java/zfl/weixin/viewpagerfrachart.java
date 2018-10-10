package zfl.weixin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zfl.com.myapplication.R;
/**
 * Created by Administrator on 2018/4/9.
 */

public class viewpagerfrachart extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_viewprfrachart,container,false);
    }
}
