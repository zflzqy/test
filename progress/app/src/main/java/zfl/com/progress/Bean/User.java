package zfl.com.progress.Bean;

import java.io.Serializable;

public class User implements Serializable{
    private Integer account; // 账户

    private String name; // 名字

    private Integer age; // 年龄

    private String sex; // 性别

    private String school; // 所属学校

    private String password; // 密码

    private String path; // 付款码地址

    private Integer credit; // 信用分

    private String otheraccount; // 其他账号

    private Integer issueCount; // 发布量

    private Integer receiveCount; // 领取量

    private Integer type; // 用户的属性

    private String salt; // 盐

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getOtheraccount() {
        return otheraccount;
    }

    public void setOtheraccount(String otheraccount) {
        this.otheraccount = otheraccount == null ? null : otheraccount.trim();
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "account=" + account +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", school='" + school + '\'' +
                ", password='" + password + '\'' +
                ", path='" + path + '\'' +
                ", credit=" + credit +
                ", otheraccount='" + otheraccount + '\'' +
                ", issueCount=" + issueCount +
                ", receiveCount=" + receiveCount +
                ", type=" + type +
                ", salt='" + salt + '\'' +
                '}';
    }
}