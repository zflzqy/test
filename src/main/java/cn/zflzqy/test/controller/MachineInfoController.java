package cn.zflzqy.test.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.zflzqy.shiroclient.config.ShiroConfig;
import cn.zflzqy.test.constant.RedisKeyConstant;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zfl
 * @Date: 2022-08-14-10:02
 * @Description:
 */
@RequestMapping("/machine")
@Controller
public class MachineInfoController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/list")
    @ResponseBody
    public JSONObject list(){
        // 结果
        JSONObject rs = new JSONObject();
        // 数据
        JSONArray data = new JSONArray();
        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan(RedisKeyConstant.key, ScanOptions.NONE);
        while (cursor.hasNext()){
            String next = cursor.next();
            data.add(JSONObject.parseObject(next));
        }
        return rs;
    }

    @RequestMapping("/register")
    @ResponseBody
    public JSONObject register(@RequestBody JSONObject object){
        // 注册机器信息，向redis中插入机器信息
        JSONObject jsonObject = new JSONObject();
        Snowflake snowflake = new Snowflake();
        String idStr = snowflake.nextIdStr();
        stringRedisTemplate.opsForValue().set(ShiroConfig.SESSION_KEY +idStr,object.toString(),30, TimeUnit.MINUTES);
        jsonObject.put("id",idStr);
        return  jsonObject;
    }

//    // 插入信息结果
//    JSONObject regester = new JSONObject();
//    // todo 获取机器名称和机器信息
//    String hostName = InetAddress.getLocalHost().getHostName();
//        regester.put("hostName",hostName);
//    OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//    // 总内存：mb
//    long totalMemory = osmxb.getTotalPhysicalMemorySize() / (1024 * 1024);
//    // 已使用内存
//    long userMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / (1024 * 1024);
//        regester.put("totalMemory",totalMemory);
//        regester.put("userMemory",userMemory);
//    // cpu使用
//    double systemCpuLoad = osmxb.getSystemCpuLoad()*100;
//        regester.put("cpu",systemCpuLoad);
//    Long addRs = stringRedisTemplate.opsForSet().add(MachineInfoController.key, regester.toString());
//    JSONObject rs = new JSONObject();
//        rs.put("rs",addRs);
    // 获取机器的mac地址
}
