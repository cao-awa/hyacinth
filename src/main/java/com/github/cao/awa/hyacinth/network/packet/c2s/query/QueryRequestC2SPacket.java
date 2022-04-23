package com.github.cao.awa.hyacinth.network.packet.c2s.query;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ServerQueryPacketListener;

public class QueryRequestC2SPacket
        implements Packet<ServerQueryPacketListener> {
    public QueryRequestC2SPacket() {
    }

    public QueryRequestC2SPacket(PacketByteBuf buf) {
    }

    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public void apply(ServerQueryPacketListener serverQueryPacketListener) {
        serverQueryPacketListener.onRequest(this);
    }
}

