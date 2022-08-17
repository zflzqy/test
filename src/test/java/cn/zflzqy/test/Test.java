package cn.zflzqy.test;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import org.codehaus.plexus.interpolation.os.OperatingSystemUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.http.WebSocket;
import java.util.Properties;

/**
 * @Author: zfl
 * @Date: 2022-08-14-12:26
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
//        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
//                OperatingSystemMXBean.class);
//        System.out.println();
        JSONObject body = new JSONObject();
        body.put("macAddress","123");
        HttpResponse execute = HttpUtil.createPost("https://dev.zflzqy.cn:20023/machine/register").body(body.toString()).execute();
        JSONObject parseObject = JSONObject.parseObject(execute.body());
        System.out.println(parseObject.getString("id"));
//        HttpResponse response = HttpUtil.createPost("https://dev.zflzqy.cn:20023/machine/list")
//                .header("id",parseObject.getString("id"))
//                .execute();
        System.out.println(execute.getStatus());
//        System.out.println(response.body());
//        WebSocket webSocket = new WebSocketImpl();

    }
}
