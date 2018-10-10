package zfl.four.downloadServicemoreThread.util;


import java.util.List;

import zfl.four.downloadServicemoreThread.bean.ThreadInfo;

public interface ThreadDAOThread {
    public void insert(ThreadInfo threadInfo);//插入
    public void delete(String url);//删除
    public void update(String url, int thread_id, int finished);//更新
    public List<ThreadInfo> query(String url);//查询
    public boolean isExists(String url, int thread);//是否存在该线程
}
