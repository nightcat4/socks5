package com.github.nightcat4.socks5.client.socks;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;

/**
 * ChannelInitializer是一种特殊的ChannelInboundHandler
 * 主要作用是执行一些初始化操作，在完成初始化操作之后，会把自己从pipeline中移除
 */
public class SocksClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(
                new Socks5InitialRequestDecoder(),
                Socks5ServerEncoder.DEFAULT,
                SocksClientHandler.INSTANCE);
    }
}
