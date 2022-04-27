package com.github.cao.awa.hyacinth.network.handler.splitter;

import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class SplitterHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects) {
        buf.markReaderIndex();
        byte[] bs = new byte[buf.writerIndex()];
        for (int i = 0; i < bs.length; ++ i) {
            if (! buf.isReadable()) {
                buf.resetReaderIndex();
                return;
            }
            bs[i] = buf.readByte();
            if (bs[i] < 0)
                continue;
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.wrappedBuffer(bs));
            try {
                int j = packetByteBuf.readVarInt();
                if (buf.readableBytes() < j) {
                    buf.resetReaderIndex();
                    return;
                }
                objects.add(buf.readBytes(j));
                return;
            } finally {
                packetByteBuf.release();
            }
        }
        if (ctx.channel().isOpen()) {
            throw new CorruptedFrameException("length wider than 21-bit");
        }
    }
}
