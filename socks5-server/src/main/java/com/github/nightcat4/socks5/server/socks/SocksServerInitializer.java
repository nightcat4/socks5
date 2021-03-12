package com.github.nightcat4.socks5.server.socks;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;

public class SocksServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(
                new Socks5InitialRequestDecoder(),
                Socks5ServerEncoder.DEFAULT,
                SocksServerHandler.INSTANCE);
    }

}
