package zfl.festival.Bean;


import java.io.Serializable;
import java.util.Date;

public class festival implements Serializable {
    private int msgId;
    private String festName;
    private String msg;//节日短信内容
    private Date date;
    //为festivla_blessing准备的
    public festival(String festName) {
        this.festName = festName;
    }

    public festival(int msgId, String festName, String msg, Date date) {
        this.msgId = msgId;
        this.festName = festName;
        this.msg = msg;
        this.date = date;
    }

    public festival() {

    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getFestName() {
        return festName;
    }

    public void setFestName(String festName) {
        this.festName = festName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
