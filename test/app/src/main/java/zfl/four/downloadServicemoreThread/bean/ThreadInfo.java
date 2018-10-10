package zfl.four.downloadServicemoreThread.bean;


public class ThreadInfo {
    private int id;
    private String url;
    private int start;
    private int stop;//完成--end
    private int finished;//完成-->finished

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url, int start, int stop, int finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.stop = stop;
        this.finished = finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }

    public int getFinished() {
        return finished;
    }
}
