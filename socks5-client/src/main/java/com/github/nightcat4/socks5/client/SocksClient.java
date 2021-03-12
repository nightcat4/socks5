package com.github.nightcat4.socks5.client;

import com.github.nightcat4.socks5.client.socks.SocksClientInitializer;
import com.github.nightcat4.socks5.common.ConstantDef;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SocksClient {

    public static void main(String[] args) {

        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new SocksClientInitializer());
            bootstrap.bind(ConstantDef.CLIENT_PORT).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
