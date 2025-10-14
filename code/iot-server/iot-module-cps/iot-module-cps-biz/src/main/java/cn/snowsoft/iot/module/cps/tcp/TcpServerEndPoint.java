package cn.snowsoft.iot.module.cps.tcp;

import cn.snowsoft.iot.module.cps.emqx.client.PahoClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;

/**
 * @author: chen_gang
 * @date: 2024/10/16 17:55
 * @description: TCP服务端，供外部设备连接
 */
@RequiredArgsConstructor
public class TcpServerEndPoint implements Runnable {
    private final PahoClient pahoClient;

    private final String agreementCode;
    private final int port;

    @Override
    public void run() {
        //创建tcp服务端并进行监听
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加自定义的业务处理器
                            // 解码，将接收到的tcp包进行解码
                            pipeline.addLast(new MessageDecoder(agreementCode));
                            // 将解码后的数据进行业务处理
                            pipeline.addLast(new MessageHandler(pahoClient));
                            // 将业务数据编码为自定义格式的tcp包
                            pipeline.addLast(new MessageEncoder());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
