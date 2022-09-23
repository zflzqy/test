package cn.zflzqy.test.netty;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.zflzqy.test.constant.RedisKeyConstant;
import cn.zflzqy.websocket.config.WebSocketEventListener;
import com.alibaba.fastjson2.JSONArray;
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

import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import cn.zflzqy.test.email.EmailUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler.class);
    private StringRedisTemplate stringRedisTemplate;
    private SimpMessagingTemplate simpMessagingTemplate;

    public EchoServerHandler(StringRedisTemplate stringRedisTemplate, SimpMessagingTemplate simpMessagingTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String content = in.toString(CharsetUtil.UTF_8);
        LOGGER.info("获取到数据：{}", content);
        // 获取客户端ip
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = insocket.getAddress().getHostAddress();
        JSONObject parseObject = JSONObject.parseObject(content);
        parseObject.put("ip",ip);
        String macAddress = parseObject.getString("macAddress");
        // 重新计算使用和总共内容
        parseObject.put("totalMemory",NumberUtil.round(parseObject.getLong("totalMemory")/1024,2));
        parseObject.put("userMemory",NumberUtil.round(parseObject.getLong("userMemory")/1024,2));
        // redis写入数据
        String redisKey = RedisKeyConstant.key + "::" + macAddress;
        stringRedisTemplate.opsForValue().set(redisKey, parseObject.toString(), 40, TimeUnit.SECONDS);
        String listKey = redisKey + "::list";
        Long size = stringRedisTemplate.opsForList().size(listKey);
        if (size > 60 * 60 * 24 * 3) {
            stringRedisTemplate.opsForList().rightPop(listKey);
        }
        // 登录的用户
        ConcurrentHashMap<String, Principal> users = WebSocketEventListener.getUsers();
        LOGGER.info("当前连接的用户信息：{}", JSONObject.toJSONString(users));

        //  获取redis存储的旧数据
        String oldData = stringRedisTemplate.opsForList().leftPop(listKey);
        if (StrUtil.isNotBlank(oldData)) {
            // 重新入队
            stringRedisTemplate.opsForList().leftPush(listKey, oldData);
            stringRedisTemplate.expire(listKey,7,TimeUnit.DAYS);
            JSONObject jsonObject = JSONObject.parseObject(oldData);
            // 旧时间
            DateTime oldDate = DateUtil.parse(jsonObject.getString("time"));
            // 新时间
            DateTime newDate = DateUtil.parse(parseObject.getString("time"));
            if (DateUtil.between(oldDate, newDate, DateUnit.SECOND) >= 30) {
                pushChartData(parseObject, listKey, users, newDate);
            }
        }else {
            pushChartData(parseObject, listKey, users, new DateTime());
        }

        // 注册客户端
        ChannelMap.addChannel(macAddress, ctx.channel());
        // 获取关机信息
        String shutDown = stringRedisTemplate.opsForValue().get(RedisKeyConstant.shutDownKey + macAddress);
        JSONObject rs = new JSONObject();
        rs.put("date", DateUtil.now());
        if (StrUtil.isNotBlank(shutDown)) {
            rs.put("shutDown", true);
            parseObject.put("shutDown", true);
        }

        // 向web端推送主机信息 todo 需要获取信息
        users.entrySet().forEach(u -> {
            // 推送主机信息
            simpMessagingTemplate.convertAndSendToUser(u.getValue().getName(), "/topic/machine/info", parseObject.toString());
        });
        // 发送登录信息
        boolean sendEmail = EmailUtil.isSendEmail(macAddress, stringRedisTemplate);
        if (sendEmail){
            try {
                EmailUtil.sendEmail(parseObject.getString("hostName"),ip,macAddress);
            } catch (Exception e) {
                LOGGER.error("发送邮件异常：",e);
            }
        }
        ByteBuf buf = Unpooled.copiedBuffer(rs.toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);
    }

    /**
     * 向用户推送图表数据
     * @param parseObject：原始数据
     * @param listKey：rediskey
     * @param users:登录用户
     * @param newDate：当前时间
     */
    private void pushChartData(JSONObject parseObject, String listKey, ConcurrentHashMap<String, Principal> users, DateTime newDate) {
        // 推送图表数据
        String date = DateUtil.format(newDate,"HH:mm:ss");
        JSONArray array = new JSONArray();
        // cpu
        JSONObject cpuObject = new JSONObject();
        cpuObject.put("date",date);
        cpuObject.put("country","cpu占用(%)");
        cpuObject.put("value", NumberUtil.round(parseObject.getString("cpu"),2));
        array.add(cpuObject);
        // 使用内存
        JSONObject userMemoryObject = new JSONObject();
        userMemoryObject.put("date",date);
        userMemoryObject.put("country","已用内存");
        userMemoryObject.put("value", parseObject.getString("userMemory"));
        array.add(userMemoryObject);
        // 总内存
        JSONObject totalMemoryObject = new JSONObject();
        totalMemoryObject.put("date",date);
        totalMemoryObject.put("country","总共内存");
        totalMemoryObject.put("value", parseObject.getString("totalMemory"));
        array.add(totalMemoryObject);
        JSONObject data = new JSONObject();
        // 构建数据
        data.put("time", parseObject.getString("time"));
        data.put("data",array);
        data.putAll(parseObject);
        stringRedisTemplate.opsForList().leftPush(listKey,data.toString());
        stringRedisTemplate.expire(listKey,7,TimeUnit.DAYS);
        users.entrySet().forEach(u -> {
            // 推送主机信息
            simpMessagingTemplate.convertAndSendToUser(u.getValue().getName(), "/topic/machine/info.chart", data.toString());
        });

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}