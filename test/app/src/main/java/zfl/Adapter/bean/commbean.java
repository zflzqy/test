package zfl.Adapter.bean;



public class commbean {
    private String title;
    private String content;
    private String time;
    private String phone;

    public commbean(){

    }
    public commbean(String title, String content, String time, String phone) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.phone = phone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getPhone() {
        return phone;
    }
}
