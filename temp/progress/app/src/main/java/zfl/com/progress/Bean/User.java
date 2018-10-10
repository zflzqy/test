package zfl.com.progress.Bean;

import java.io.Serializable;

public class User implements Serializable{
    private int account;//账号
    private String password;//密码
    private String name;//姓名
    private int age;//年龄
    private String sex;//性别
    private String school;//所属学校
    private String path;//二维码图片地址
    private String otheraccout;//第三方登录

    public User() {
    }


    public String getOtheraccout() {
        return otheraccout;
    }

    public void setOtheraccout(String otheraccout) {
        this.otheraccout = otheraccout;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "User{" +
                "account=" + account +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", school='" + school + '\'' +
                ", path='" + path + '\'' +
                ", otheraccout='" + otheraccout + '\'' +
                '}';
    }
}
