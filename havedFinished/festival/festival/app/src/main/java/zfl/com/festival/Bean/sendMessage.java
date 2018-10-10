package zfl.com.festival.Bean;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sendMessage {
    private String id;//唯一标识符
    private String msg;
    private String numbers;
    private String names;
    private String festName;
    private Date date;
    private String dateStr;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFestName() {
        return festName;
    }

    public void setFestName(String festName) {
        this.festName = festName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateStr() {
        dateStr = df.format(date);
        return dateStr;
    }

}
