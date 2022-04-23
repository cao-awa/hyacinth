package com.github.cao.awa.hyacinth.network.handler.prepender;

import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SizePrepender
        extends MessageToByteEncoder<ByteBuf> {
    /**
     * The max length, in number of bytes, of the prepending size var int permitted.
     * Has value {@value}.
     */
    private static final int MAX_PREPEND_LENGTH = 3;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) {
        int i = byteBuf.readableBytes();
        int j = PacketByteBuf.getVarIntLength(i);
        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + i + " into 3");
        }
        PacketByteBuf packetByteBuf = new PacketByteBuf(byteBuf2);
        packetByteBuf.ensureWritable(j + i);
        packetByteBuf.writeVarInt(i);
        packetByteBuf.writeBytes(byteBuf, byteBuf.readerIndex(), i);
    }
}


