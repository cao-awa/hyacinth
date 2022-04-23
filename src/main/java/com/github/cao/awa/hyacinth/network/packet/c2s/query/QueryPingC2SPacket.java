package com.github.cao.awa.hyacinth.network.packet.c2s.query;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ServerQueryPacketListener;

public class QueryPingC2SPacket
        implements Packet<ServerQueryPacketListener> {
    private final long startTime;

    public QueryPingC2SPacket(long startTime) {
        this.startTime = startTime;
    }

    public QueryPingC2SPacket(PacketByteBuf buf) {
        this.startTime = buf.readLong();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.startTime);
    }

    @Override
    public void apply(ServerQueryPacketListener serverQueryPacketListener) {
        serverQueryPacketListener.onPing(this);
    }

    public long getStartTime() {
        return this.startTime;
    }
}

