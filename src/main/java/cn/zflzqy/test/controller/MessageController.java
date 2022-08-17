package cn.zflzqy.test.controller;

import cn.hutool.core.util.StrUtil;
import cn.zflzqy.shiroclient.config.ShiroConfig;
import cn.zflzqy.test.constant.RedisKeyConstant;
import com.alibaba.fastjson2.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class MessageController {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
 
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // 参考地址
    // https://docs.spring.io/spring-framework/docs/5.0.5.RELEASE/spring-framework-reference/web.html#websocket-stomp-handle-send
 
//    @MessageMapping("/test/{id}")
//    public void test(Message message,
//                     MessageHeaders MessageHeaders,
//                     @Header("destination") String destination,
//                     @Headers Map<String, Object> headers,
//                     @DestinationVariable long id,
//                     @Payload String body) {
//        log.info("[test] Message: {}", message);
//        log.info("[test] MessageHeaders: {}", MessageHeaders);
//        log.info("[test] Header: {}", destination);
//        log.info("[test] Headers: {}", headers);
//        log.info("[test] DestinationVariable: {}", id);
//        log.info("[test] Payload: {}", body);
//    }
 
    // ---------------------- 广播推送 ----------------------
//    @MessageMapping("/send")
//    public void hello(@Payload String body) {
//        print(body);
//        simpMessagingTemplate.convertAndSend("/topic/accept", "reply hello");
//    }

    // ---------------------- 对点推送 ----------------------

    /**
     * 主动向web端推送cpu信息
     * @param body
     */
    @MessageMapping("/send/cpuInfo")
    public void cpu(@Payload String body, Principal principal) {
        JSONObject cpuInfo = JSONObject.parseObject(body);
        // 插入机器信息
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.key+"::"+cpuInfo.getString("macAddress"), body,
                40, TimeUnit.SECONDS);
        // 更新机器会话信息 todo
//        stringRedisTemplate.expire(ShiroConfig.SESSION_KEY+principal.getName(),30,TimeUnit.MINUTES);
        // 向web端推送机器信息 todo

    }
    // 向主机推送关机信息
    @MessageMapping("/sendUser/shutDown")
    public void shutDown(@Payload String body, Principal principal) {
        LOGGER.info(body);
        LOGGER.info(principal.toString());
        JSONObject info = JSONObject.parseObject(body);
        String macAddress = info.getString("macAddress");
        // 机器信息
        String machineInfo = stringRedisTemplate.opsForValue().get(RedisKeyConstant.key + "::" + macAddress);
        JSONObject rs = new JSONObject();
        if (StrUtil.isBlank(machineInfo)){
            rs.put("msg","机器信息未找到：mac地址："+macAddress);
        }else {
            // 推送执行结果 todo
            JSONObject command = new JSONObject();
            command.put("shutDown",true);
            // 将关机信息插入redis中
            stringRedisTemplate.opsForValue().set(RedisKeyConstant.shutDownKey+macAddress,"true",30,TimeUnit.SECONDS);
        }
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/topic/cpuPushShutDownRs",rs);

    }


    private void print(Object data) {
        LOGGER.error("receive message body: {}", data);
    }
}