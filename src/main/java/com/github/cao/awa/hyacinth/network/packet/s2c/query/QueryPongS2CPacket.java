package com.github.cao.awa.hyacinth.network.packet.s2c.query;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ClientQueryPacketListener;

public class QueryPongS2CPacket
        implements Packet<ClientQueryPacketListener> {
    private final long startTime;

    public QueryPongS2CPacket(long startTime) {
        this.startTime = startTime;
    }

    public QueryPongS2CPacket(PacketByteBuf buf) {
        this.startTime = buf.readLong();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.startTime);
    }

    @Override
    public void apply(ClientQueryPacketListener clientQueryPacketListener) {
        clientQueryPacketListener.onPong(this);
    }

    public long getStartTime() {
        return this.startTime;
    }
}

