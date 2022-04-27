package com.github.cao.awa.hyacinth.network.handler.decode;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.state.NetworkSide;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.List;

public class DecoderHandler extends ByteToMessageDecoder {
    private static final Logger LOGGER = LogManager.getLogger();
        private static final Marker MARKER = MarkerManager.getMarker("PACKET_RECEIVED", ClientConnection.NETWORK_PACKETS_MARKER);
    private final NetworkSide side;

    public DecoderHandler(NetworkSide side) {
        this.side = side;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects) throws Exception {
        int i = buf.readableBytes();
        if (i == 0) {
            return;
        }
        PacketByteBuf packetByteBuf = new PacketByteBuf(buf);
        int j = packetByteBuf.readVarInt();
        Packet<?> packet = ctx.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get().getPacketHandler(this.side, j, packetByteBuf);
        if (packet == null) {
            throw new IOException("Bad packet id " + j);
        }
        int k = ctx.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get().getId();
//        FlightProfiler.INSTANCE.onPacketReceived(k, j, ctx.channel().remoteAddress(), i);
        if (packetByteBuf.readableBytes() > 0) {
            throw new IOException("Packet " + ctx.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get().getId() + "/" + j + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetByteBuf.readableBytes() + " bytes extra whilst reading packet " + j);
        }
        objects.add(packet);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MARKER, " IN: [{}:{}] {}", ctx.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get(), j, packet.getClass().getName());
        }
    }
}
