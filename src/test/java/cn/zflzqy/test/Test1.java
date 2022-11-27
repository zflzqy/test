package cn.zflzqy.test;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @Author: zfl
 * @Date: 2022-09-24-10:19
 * @Description:
 */
public class Test1 {
    public static void main(String[] args) {
        List<PeoPle> rs = new ArrayList<>();
        ExecutorService executorService = ThreadUtil.newExecutor(12);
        PeoPle peoPle = null;
        List<PeoPle> peoPle1 = new ArrayList<>();
        for (int i=0;i<100;i++){
            peoPle = new PeoPle(i+"");
            peoPle1.add(peoPle);
        }
        List c = new ArrayList();
        peoPle1.parallelStream().forEach(e->{
//            System.out.println(e.hashCode());
//            executorService.submit(new Thread(e));
//            c.add(e.hashCode());1
            PeoPle peoPle2 = new PeoPle(e.getName());
            peoPle2.setName(peoPle2.getName()+"1");
            System.out.println(peoPle2);
        });
//        System.out.println(JSONObject.toJSONString(peoPle1));


    }
}
