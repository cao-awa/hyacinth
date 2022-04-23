package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

/**
 * This is a packet that is sent by the client during tick after receiving a
 * play ping packet from the server, passing the {@link #parameter} back to the
 * server.
 * 
 * @see net.minecraft.network.packet.s2c.play.PlayPingS2CPacket
 * @see KeepAliveC2SPacket
 * @see net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
 */
public class PlayPongC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int parameter;

    public PlayPongC2SPacket(int parameter) {
        this.parameter = parameter;
    }

    public PlayPongC2SPacket(PacketByteBuf buf) {
        this.parameter = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.parameter);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onPong(this);
    }

    public int getParameter() {
        return this.parameter;
    }
}

