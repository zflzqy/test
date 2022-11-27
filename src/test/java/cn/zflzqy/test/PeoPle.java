package cn.zflzqy.test;

/**
 * @Author: zfl
 * @Date: 2022-09-24-10:25
 * @Description:
 */
public class PeoPle {
    private String name;

    public PeoPle(String name) {
        this.name = name;
    }

    public PeoPle() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PeoPle{" +
                "name='" + name + '\'' +
                '}';
    }
}
