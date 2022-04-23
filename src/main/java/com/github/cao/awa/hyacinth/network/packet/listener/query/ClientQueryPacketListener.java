package com.github.cao.awa.hyacinth.network.packet.listener.query;

import com.github.cao.awa.hyacinth.network.packet.s2c.query.QueryPongS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.query.QueryResponseS2CPacket;

public interface ClientQueryPacketListener extends PacketListener {
    void onResponse(QueryResponseS2CPacket var1);

    void onPong(QueryPongS2CPacket var1);
}

