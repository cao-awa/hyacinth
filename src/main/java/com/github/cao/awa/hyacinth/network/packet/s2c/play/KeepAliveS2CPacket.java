package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class KeepAliveS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final long id;

    public KeepAliveS2CPacket(long id) {
        this.id = id;
    }

    public KeepAliveS2CPacket(PacketByteBuf buf) {
        this.id = buf.readLong();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.id);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public long getId() {
        return this.id;
    }
}

