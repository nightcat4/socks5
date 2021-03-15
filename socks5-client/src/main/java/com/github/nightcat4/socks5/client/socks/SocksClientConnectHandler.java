package com.github.nightcat4.socks5.client.socks;

import com.github.nightcat4.socks5.common.ConstantDef;
import com.github.nightcat4.socks5.common.util.DirectHandler;
import com.github.nightcat4.socks5.common.util.RelayHandler;
import com.github.nightcat4.socks5.common.util.SocksUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequest;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;

@ChannelHandler.Sharable
public class SocksClientConnectHandler extends SimpleChannelInboundHandler<Socks5CommandRequest> {

    private final Bootstrap b = new Bootstrap();

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Socks5CommandRequest message) throws Exception {
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
                new FutureListener<Channel>() {
                    @Override
                    public void operationComplete(final Future<Channel> future) throws Exception {
                        final Channel outboundChannel = future.getNow();
                        if (future.isSuccess()) {
                            outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                            // 先add再remove，否则fire不能传递数据
                            ctx.pipeline().addLast(new RelayHandler(outboundChannel));
                            ctx.pipeline().remove(SocksClientConnectHandler.this);
                            ctx.fireChannelRead(message);
                        } else {
                            SocksUtils.closeOnFlush(ctx.channel());
                        }
                    }
                });

        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new DirectHandler(promise));

        b.connect(ConstantDef.SEVER_HOST, ConstantDef.SEVER_PORT).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection established use handler provided results
                } else {
                    // Close the connection if the connection attempt has failed.
                    SocksUtils.closeOnFlush(ctx.channel());
                }
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocksUtils.closeOnFlush(ctx.channel());
    }
}