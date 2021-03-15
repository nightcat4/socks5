package com.github.nightcat4.socks5.client;

import com.github.nightcat4.socks5.client.socks.SocksClientInitializer;
import com.github.nightcat4.socks5.common.ConstantDef;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SocksClient {

    public static void main(String[] args) {

        // EventLoopGroup是netty的调度模块，包含一组EventLoop，Channel通过注册到EventLoop中执行操作
        // bossGroup就是parentGroup，是负责处理TCP/IP连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // workerGroup就是childGroup，是负责处理Channel（通道）的I/O事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                // 使用Nio模型
                .channel(NioServerSocketChannel.class)
                // 地址可复用
                .option(ChannelOption.SO_REUSEADDR, true)
                // 服务器可监听客户端是否存活，以便即时释放资源
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 减少延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 指定ChannelInitializer
                .childHandler(new SocksClientInitializer());
        ChannelFuture f = null;
        try {
            f = bootstrap.bind(ConstantDef.CLIENT_PORT).sync();
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // 如果缺失，则会直接执行finally代码块，程序运行结束
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            if (f != null)
                f.channel().close();
        }

    }

}
