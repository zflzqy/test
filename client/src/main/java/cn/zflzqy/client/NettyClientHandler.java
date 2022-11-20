package cn.zflzqy.client;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONValidator;
import com.sun.management.OperatingSystemMXBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private NettyClient nettyClient;

    public NettyClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    /**
     * 当客户端连接服务器完成就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendMsg(ctx);
    }

    /**
     * 发送消息
     * @param ctx
     * @throws UnknownHostException
     */
    private void sendMsg(ChannelHandlerContext ctx) throws UnknownHostException {
        JSONObject regester = new JSONObject();
        // todo 获取机器名称和机器信息
        String hostName = InetAddress.getLocalHost().getHostName();
        regester.put("hostName", hostName);
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 总内存：mb
        long totalMemory = osmxb.getTotalPhysicalMemorySize() / (1024 * 1024);
        // 已使用内存
        long userMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / (1024 * 1024);
        regester.put("totalMemory", totalMemory);
        regester.put("userMemory", userMemory);
        // cpu使用
        double systemCpuLoad = osmxb.getSystemCpuLoad() * 100;
        regester.put("cpu", systemCpuLoad);
        regester.put("macAddress", getWindowsMACAddress());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String todayStr = dateTimeFormatter.format(now);
        regester.put("time", todayStr);
        regester.put("name","zflzflzflzf");
        ByteBuf byteBuf = Unpooled.copiedBuffer(regester.toString(), CharsetUtil.UTF_8);
        ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf);
    }


    /**
     * 获取widnows网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getWindowsMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            // windows下的命令，显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ipconfig /all");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[physical
                index = line.toLowerCase().indexOf("physical address");
                if (index<0){
                    index = line.toLowerCase().indexOf("ethernet");
                }
                if (index >= 0) {// 找到了
                    line = bufferedReader.readLine();
                    index = line.indexOf(":");// 寻找":"的位置
                    if (index >= 0) {
                        // 取出mac地址并去除2边空格
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }

        return mac;
    }
    //当通道有读取事件时会触发，即服务端发送数据给客户端
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String content =   buf.toString(CharsetUtil.UTF_8);
        System.out.println("receiver msg:" +content);
        System.out.println("server address： " + ctx.channel().remoteAddress());
        if (JSONValidator.from(content).validate()) {
            JSONObject jsonObject = JSONObject.parseObject(content);
            if (jsonObject.containsKey("shutDown") && jsonObject.getBoolean("shutDown")) {
                Runtime.getRuntime().exec("Shutdown.exe -s -t 0 -f");
            }
        }

        Thread.sleep(500);
        sendMsg(ctx);
    }

    // channel 处于不活动状态时调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("run shutdown .......");
        nettyClient.connect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
