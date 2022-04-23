package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class ChunkLoadDistanceS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int distance;

    public ChunkLoadDistanceS2CPacket(int distance) {
        this.distance = distance;
    }

    public ChunkLoadDistanceS2CPacket(PacketByteBuf buf) {
        this.distance = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.distance);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public int getDistance() {
        return this.distance;
    }
}

