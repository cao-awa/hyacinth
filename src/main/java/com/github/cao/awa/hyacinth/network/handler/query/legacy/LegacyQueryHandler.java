package com.github.cao.awa.hyacinth.network.handler.query.legacy;

import com.github.cao.awa.hyacinth.network.ServerNetworkIo;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
    public static final int field_29771 = 127;
    private static final Logger LOGGER = LogManager.getLogger("Handler:LegacyQuery");
    private final ServerNetworkIo networkIo;

    public LegacyQueryHandler(ServerNetworkIo networkIo) {
        this.networkIo = networkIo;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byteBuf.markReaderIndex();
        boolean bl = true;
        try {
            if (byteBuf.readUnsignedByte() != 254) {
                return;
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            MinecraftServer minecraftServer = this.networkIo.getServer();
            int i = byteBuf.readableBytes();
            switch (i) {
                case 0 -> {
                    LOGGER.debug("Ping: (<1.3.x) from {}:{}", inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    String string = String.format("%s\u00a7%d\u00a7%d", minecraftServer.getServerMotd(), minecraftServer.getCurrentPlayerCount(), minecraftServer.getMaxPlayerCount());
                    this.reply(ctx, this.toBuffer(string));
                }
                case 1 -> {
                    if (byteBuf.readUnsignedByte() != 1) {
                        return;
                    }
                    LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    String string = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, minecraftServer.getVersion(), minecraftServer.getServerMotd(), minecraftServer.getCurrentPlayerCount(), minecraftServer.getMaxPlayerCount());
                    this.reply(ctx, this.toBuffer(string));
                }
                default -> {
                    boolean string = byteBuf.readUnsignedByte() == 1;
                    string &= byteBuf.readUnsignedByte() == 250;
                    string &= "MC|PingHost".equals(new String(byteBuf.readBytes(byteBuf.readShort() * 2).array(), StandardCharsets.UTF_16BE));
                    int j = byteBuf.readUnsignedShort();
                    string &= byteBuf.readUnsignedByte() >= 73;
                    string &= 3 + byteBuf.readBytes(byteBuf.readShort() * 2).array().length + 4 == j;
                    string &= byteBuf.readInt() <= 65535;
                    if (! (string & byteBuf.readableBytes() == 0)) {
                        return;
                    }
                    LOGGER.debug("Ping: (1.6) from {}:{}", inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    String string2 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, minecraftServer.getVersion(), minecraftServer.getServerMotd(), minecraftServer.getCurrentPlayerCount(), minecraftServer.getMaxPlayerCount());
                    ByteBuf byteBuf2 = this.toBuffer(string2);
                    try {
                        this.reply(ctx, byteBuf2);
                    } finally {
                        byteBuf2.release();
                    }
                }
            }
            byteBuf.release();
            bl = false;
        } catch (RuntimeException runtimeException) {
        } finally {
            if (bl) {
                byteBuf.resetReaderIndex();
                ctx.channel().pipeline().remove("legacy_query");
                ctx.fireChannelRead(msg);
            }
        }
    }

    private void reply(ChannelHandlerContext ctx, ByteBuf buf) {
        ctx.pipeline().firstContext().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf toBuffer(String s) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(255);
        char[] cs = s.toCharArray();
        byteBuf.writeShort(cs.length);
        for (char c : cs) {
            byteBuf.writeChar(c);
        }
        return byteBuf;
    }
}