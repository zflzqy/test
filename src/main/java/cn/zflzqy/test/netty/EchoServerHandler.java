package cn.zflzqy.test.netty;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.zflzqy.test.constant.RedisKeyConstant;
import cn.zflzqy.test.controller.MachineInfoController;
import cn.zflzqy.websocket.config.WebSocketEventListener;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler.class);
    private StringRedisTemplate stringRedisTemplate;
    private SimpMessagingTemplate simpMessagingTemplate;

    public EchoServerHandler(StringRedisTemplate stringRedisTemplate,SimpMessagingTemplate simpMessagingTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        String content = in.toString(CharsetUtil.UTF_8);
        LOGGER.info("获取到数据：{}",content);
        JSONObject parseObject = JSONObject.parseObject(content);
        String macAddress = parseObject.getString("macAddress");
        // redis写入数据
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.key+"::"+macAddress, content,
                40, TimeUnit.SECONDS);
        // 注册客户端
        ChannelMap.addChannel(macAddress,ctx.channel());
        // 向web端推送主机信息 todo 需要获取信息
        ConcurrentHashMap<String, Principal> users = WebSocketEventListener.getUsers();
        LOGGER.info("当前连接的用户信息：{}",JSONObject.toJSONString(users));
        users.entrySet().forEach(u->{
            // 推送主机信息
            simpMessagingTemplate.convertAndSendToUser(u.getValue().getName(),"/topic/machine/info",content);
        });
        // 获取关机信息
        String shutDown = stringRedisTemplate.opsForValue().get(RedisKeyConstant.shutDownKey + macAddress);
        JSONObject rs = new JSONObject();
        rs.put("date", DateUtil.now());
        if (StrUtil.isNotBlank(shutDown)){
            rs.put("shutDown",true);
        }
        ByteBuf buf = Unpooled.copiedBuffer(rs.toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

}