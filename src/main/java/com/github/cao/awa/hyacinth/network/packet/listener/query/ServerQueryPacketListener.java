package com.github.cao.awa.hyacinth.network.packet.listener.query;

import com.github.cao.awa.hyacinth.network.packet.c2s.query.QueryPingC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.query.QueryRequestC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;

public interface ServerQueryPacketListener extends PacketListener {
    void onPing(QueryPingC2SPacket var1);

    void onRequest(QueryRequestC2SPacket var1);
}

