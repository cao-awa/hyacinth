package com.github.cao.awa.hyacinth.network.handler.encode;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.state.NetworkSide;
import com.github.cao.awa.hyacinth.network.state.NetworkState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.*;

import java.io.IOException;

public class PacketEncoder
        extends MessageToByteEncoder<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger("Packet:Encoder");
    private static final Marker MARKER = MarkerManager.getMarker("PACKET_SENT", ClientConnection.NETWORK_PACKETS_MARKER);
    private final NetworkSide side;

    public PacketEncoder(NetworkSide side) {
        this.side = side;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf) throws Exception {
        NetworkState networkState = channelHandlerContext.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get();
        if (networkState == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + packet);
        }
        Integer integer = networkState.getPacketId(this.side, packet);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(MARKER, "OUT: [{}:{}] {}", channelHandlerContext.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get(), (Object)integer, (Object)packet.getClass().getName());
        }
        if (integer == null) {
            throw new IOException("Can't serialize unregistered packet");
        }
        PacketByteBuf packetByteBuf = new PacketByteBuf(byteBuf);
        packetByteBuf.writeVarInt(integer);
        try {
            int i = packetByteBuf.writerIndex();
            packet.write(packetByteBuf);
            int j = packetByteBuf.writerIndex() - i;
            if (j > 8388608) {
                throw new IllegalArgumentException("Packet too big (is " + j + ", should be less than 8388608): " + packet);
            }
            channelHandlerContext.channel().attr(ClientConnection.PROTOCOL_ATTRIBUTE_KEY).get().getId();
        }
        catch (Throwable i) {
            LOGGER.error(i);
            if (packet.isWritingErrorSkippable()) {
                throw new PacketEncoderException(i);
            }
            throw i;
        }
    }
}