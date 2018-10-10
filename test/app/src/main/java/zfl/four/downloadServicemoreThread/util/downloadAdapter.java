package zfl.four.downloadServicemoreThread.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import zfl.com.myapplication.R;
import zfl.four.downloadServicemoreThread.bean.FileInfo;
import zfl.four.downloadServicemoreThread.downloadActivityThread;


public class downloadAdapter extends BaseAdapter {
    private Context mContext;
    private List<FileInfo> mFileInfos;
    public downloadAdapter(Context mContext, List<FileInfo> mFileInfos) {
        this.mContext = mContext;
        this.mFileInfos = mFileInfos;
    }

    @Override
    public int getCount() {
        return mFileInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        final FileInfo fileInfo = mFileInfos.get(position);
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_downloadthread,null);
            viewHodler.tv = convertView.findViewById(R.id.tv_downloadthread);
            viewHodler.pb = convertView.findViewById(R.id.thread_progress);
            viewHodler.start = convertView.findViewById(R.id.thread_start);
            viewHodler.stop = convertView.findViewById(R.id.thread_stop);
            viewHodler.tv.setText(fileInfo.getName());
            viewHodler.pb.setMax(100);
            viewHodler.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, downloadServiceThread.class);
                    intent.setAction(downloadServiceThread.ACTION_START);
                    intent.putExtra("fileInfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            viewHodler.stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, downloadServiceThread.class);
                    intent.setAction(downloadServiceThread.ACTION_STOP);
                    intent.putExtra("fileInfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            convertView.setTag(viewHodler);
        }
        else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        //设置进度条更新，通过updateprogress调用getView
        viewHodler.pb.setProgress(fileInfo.getFinished());
        return convertView;
    }
    public void  updateprogress(int id , int progress){
        FileInfo fileInfo = mFileInfos.get(id);
        fileInfo.setFinished(progress);
        notifyDataSetInvalidated();
    }

    static class ViewHodler{
        TextView tv;
        ProgressBar pb;
        Button start,stop;
    }
}
