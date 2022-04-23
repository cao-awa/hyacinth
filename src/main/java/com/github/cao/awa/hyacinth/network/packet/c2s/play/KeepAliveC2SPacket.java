package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class KeepAliveC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final long id;

    public KeepAliveC2SPacket(long id) {
        this.id = id;
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onKeepAlive(this);
    }

    public KeepAliveC2SPacket(PacketByteBuf buf) {
        this.id = buf.readLong();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.id);
    }

    public long getId() {
        return this.id;
    }
}

