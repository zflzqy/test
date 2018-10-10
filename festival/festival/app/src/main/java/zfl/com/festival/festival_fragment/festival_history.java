package zfl.com.festival.festival_fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zfl.com.festival.Database.Smscontentprovider;
import zfl.com.festival.R;
import zfl.com.festival.view.FlowLayout;

//extends ListFragment得到setListAdapter();
public class festival_history extends ListFragment {
    private static final int LODER_ID = 1;
    private LayoutInflater mInfalter;
    private CursorAdapter mCursorAdapter;//适配器z

    private Handler mHandler = new Handler();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInfalter =LayoutInflater.from(getActivity());

        initloader();
        setupListAdapter();
    }
    private void setupListAdapter() {
        mCursorAdapter = new CursorAdapter(getActivity(),null,false)
        {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = mInfalter.inflate(R.layout.item_festival_history,parent,false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor)
            {
                TextView msg = view.findViewById(R.id.festival_history_tvmsg);
                FlowLayout fl = view.findViewById(R.id.festival_history_float);
                final TextView festName = view.findViewById(R.id.festival_history_tvname);
                TextView date = view.findViewById(R.id.festival_history_tvdate);

                msg.setText(cursor.getString(cursor.getColumnIndex("msg")));
                festName.setText(cursor.getString(cursor.getColumnIndex("festName")));
                final String id =cursor.getString(cursor.getColumnIndex("id"));//短信记录唯一标识符

                long ldate = cursor.getLong(cursor.getColumnIndex("date"));
                date.setText(parseDate(ldate));
                //查询联系人的姓名
                String names  = cursor.getString(cursor.getColumnIndex("names"));
                isNull(names);//判断是否没有联系人
                for (String name : names.split(",")){
                        addTag(name,fl);
                }
                //长按删除发送记录
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteMsg(id);
                        return false;
                    }
                });
            }
        };
        setListAdapter(mCursorAdapter);
    }
    //添加视图
    private void addTag(String name, FlowLayout fl) {
        View view =mInfalter.inflate(R.layout.tag,fl,false);
        TextView textView = view.findViewById(R.id.tag_tv);
        textView.setText(name);
        fl.addView(view);
    }
    //删除短信发送记录
    private void deleteMsg(final String id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("删除短信");
        dialog.setMessage("确定删除该条短信记录？");
        dialog.setIcon(R.drawable.delete);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(){
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //非ui线程不更新ui
                                ContentResolver c =getActivity().getContentResolver();
                                c.delete(Smscontentprovider.URI_SMS_ONE,"id=?",new String[]{id});
                            }
                        });
                    }
                }.start();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //格式转换
    private String parseDate(long date) {
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(date);
    }
    //判断联系人数据是否为空
    private  void isNull(String data){
        if (TextUtils.isEmpty(data)){
            return;//为空直接返回
        }
    }
    //初始化数据
    private void initloader() {
        getLoaderManager().initLoader(LODER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cl = new CursorLoader(getActivity(), Smscontentprovider.URI_SMS_ALL,null,null,null,null);
                return cl;
            }
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() ==LODER_ID){
                    mCursorAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mCursorAdapter.swapCursor(null);
            }
        });
    }
}
