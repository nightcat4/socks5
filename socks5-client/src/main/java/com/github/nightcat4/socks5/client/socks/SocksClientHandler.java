package com.github.nightcat4.socks5.client.socks;

import com.github.nightcat4.socks5.common.util.SocksUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.*;

@ChannelHandler.Sharable
public class SocksClientHandler extends SimpleChannelInboundHandler<Socks5Message> {

    public static final SocksClientHandler INSTANCE = new SocksClientHandler();

    private SocksClientHandler() { }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Socks5Message socksRequest) throws Exception {
        if (socksRequest instanceof Socks5InitialRequest) {
            // auth support example
            //ctx.pipeline().addFirst(new Socks5PasswordAuthRequestDecoder());
            //ctx.write(new DefaultSocks5AuthMethodResponse(Socks5AuthMethod.PASSWORD));
            ctx.pipeline().addFirst(new Socks5CommandRequestDecoder());
            ctx.write(new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH));
        } else if (socksRequest instanceof Socks5PasswordAuthRequest) {
            // Socks5AuthMethod.NO_AUTH 的场合走不到这里
            ctx.pipeline().addFirst(new Socks5CommandRequestDecoder());
            ctx.write(new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS));
        } else if (socksRequest instanceof Socks5CommandRequest) {
            Socks5CommandRequest socks5CmdRequest = (Socks5CommandRequest) socksRequest;
            if (socks5CmdRequest.type() == Socks5CommandType.CONNECT) {
                ctx.pipeline().addLast(new SocksClientConnectHandler());
                ctx.pipeline().remove(this);
                ctx.fireChannelRead(socksRequest);
            } else {
                ctx.close();
            }
        } else {
            ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
        SocksUtils.closeOnFlush(ctx.channel());
    }
}