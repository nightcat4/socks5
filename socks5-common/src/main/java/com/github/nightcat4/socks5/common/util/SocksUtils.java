package com.github.nightcat4.socks5.common.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

public final class SocksUtils {

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    public static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static ByteBuf getConnectRequest(Socks5CommandRequest request) {
        ByteBuf buff = Unpooled.buffer();
        buff.writeByte(request.version().byteValue());
        buff.writeByte(request.type().byteValue());
        buff.writeByte(0x00);
        byte addressType = request.dstAddrType().byteValue();
        buff.writeByte(addressType);
        String host = request.dstAddr();
        int port = request.dstPort();
        switch (addressType) {
            case 0x01: {
                buff.writeBytes(NetUtil.createByteArrayFromIpAddressString(host));
                buff.writeShort(port);
                break;
            }
            case 0x03: {
                buff.writeByte(host.length());
                buff.writeBytes(host.getBytes(CharsetUtil.US_ASCII));
                buff.writeShort(port);
                break;
            }
            case 0x04: {
                buff.writeBytes(NetUtil.createByteArrayFromIpAddressString(host));
                buff.writeShort(port);
                break;
            }
        }
//        byte[] data = new byte[buff.readableBytes()];
//        buff.getBytes(0, data);
        return buff;
    }

    public static ByteBuf getConnectResponse(Socks5CommandRequest request) {
        ByteBuf buff = Unpooled.buffer();
        // VER
        buff.writeByte(request.version().byteValue());
        // REP 0x00=succeeded
        buff.writeByte(0x00);
        // RSV 0x00
        buff.writeByte(0x00);
        // ATYP 0x01=ipv4
        byte addressType = request.dstAddrType().byteValue();
        buff.writeByte(addressType);
        String host = request.dstAddr();
        int port = request.dstPort();
        switch (addressType) {
            case 0x01: {
                buff.writeBytes(NetUtil.createByteArrayFromIpAddressString(host));
                buff.writeShort(port);
                break;
            }
            case 0x03: {
                buff.writeByte(host.length());
                buff.writeBytes(host.getBytes(CharsetUtil.US_ASCII));
                buff.writeShort(port);
                break;
            }
            case 0x04: {
                buff.writeBytes(NetUtil.createByteArrayFromIpAddressString(host));
                buff.writeShort(port);
                break;
            }
        }
        return buff;
    }

    private SocksUtils() { }
}
