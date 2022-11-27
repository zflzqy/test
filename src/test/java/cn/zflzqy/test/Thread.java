package cn.zflzqy.test;

import cn.hutool.core.util.RandomUtil;

/**
 * @Author: zfl
 * @Date: 2022-09-24-10:22
 * @Description:
 */
public class Thread implements Runnable{
    private PeoPle index;

    public Thread(PeoPle index) {
        this.index = index;
    }

    @Override
    public void run() {
//            java.lang.Thread.sleep(RandomUtil.randomInt(200,500));
            index.setName(index+" 1231123123123");
            System.out.println(index.hashCode());
            System.out.println("内容："+index.toString());

    }
}
