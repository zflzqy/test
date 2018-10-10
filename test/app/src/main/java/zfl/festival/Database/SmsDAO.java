package zfl.festival.Database;


import java.util.List;

import zfl.festival.Bean.festival;

public interface SmsDAO {
    public void insert(festival festival);//增加短信记录
    public void delete(String FestName, int msgId);//删除短信记录
    public void update(festival festival);//修改短信记录
    public List<festival> query(String FestName);//查找短信记录
    public festival queryOne(String festName,int msgId);//查找单条短信
}
