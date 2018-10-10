package zfl.four.downloadServicemoreThread.bean;


import java.io.Serializable;

public class FileInfo implements Serializable{
    private int id;
    private String name;
    private String url;//文件的url
    private int length;//文件的长度
    private int finished;//完成的进度

    public FileInfo() {
    }
    public FileInfo(int id, String name, String url, int length, int finished) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.length = length;
        this.finished = finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getLength() {
        return length;
    }

    public int getFinished() {
        return finished;
    }
}
