package zfl.com.progress.Bean;

import java.io.Serializable;

public class Task implements Serializable{
    private int  id;//任务唯一标识
    private String request;//任务要求
    private int issue_account;//任务发布者账号
    private int receive_account;//任务领取者账号
    private float price;//任务价格
    private String type;//任务类型
    private String starttime;//任务开始时间
    private int endtime;//任务要求天数
    private String finishtime;//任务要求完成时间
    private int accept;//任务是否被领取
    private int giveup;//任务是否放弃
    private int finised;//任务是否完成
    private String appraise;//任务评价内容
    private int appraiselevel;//任务评价等级

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getIssue_account() {
        return issue_account;
    }

    public void setIssue_account(int issue_account) {
        this.issue_account = issue_account;
    }

    public int getReceive_account() {
        return receive_account;
    }

    public void setReceive_account(int receive_account) {
        this.receive_account = receive_account;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getGiveup() {
        return giveup;
    }

    public void setGiveup(int giveup) {
        this.giveup = giveup;
    }

    public int getFinised() {
        return finised;
    }

    public void setFinised(int finised) {
        this.finised = finised;
    }

	public String getAppraise() {
		return appraise;
	}

	public void setAppraise(String appraise) {
		this.appraise = appraise;
	}

	public int getAppraiselevel() {
		return appraiselevel;
	}

	public void setAppraiselevel(int appraiselevel) {
		this.appraiselevel = appraiselevel;
	}

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", request='" + request + '\'' +
                ", issue_account=" + issue_account +
                ", receive_account=" + receive_account +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime=" + endtime +
                ", finishtime='" + finishtime + '\'' +
                ", accept=" + accept +
                ", giveup=" + giveup +
                ", finised=" + finised +
                ", appraise='" + appraise + '\'' +
                ", appraiselevel=" + appraiselevel +
                '}';
    }
}
