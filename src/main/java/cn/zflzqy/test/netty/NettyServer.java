package cn.zflzqy.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private int port = 12315;
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup work = new NioEventLoopGroup();

    @PostConstruct
    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoServerHandler(stringRedisTemplate,simpMessagingTemplate));
                    }
                });
        ChannelFuture channelFuture = b.bind().sync();
        if (channelFuture.isSuccess()) {
            LOGGER.info("启动成功server");
        }


    }

    @PreDestroy
    private void destory() throws Exception {
        boss.shutdownGracefully();
        work.shutdownGracefully();
        LOGGER.info("关闭server");
    }
}