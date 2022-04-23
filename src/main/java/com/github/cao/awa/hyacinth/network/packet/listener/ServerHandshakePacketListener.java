package com.github.cao.awa.hyacinth.network.packet.listener;

import com.github.cao.awa.hyacinth.network.packet.c2s.handshake.HandshakeC2SPacket;

public interface ServerHandshakePacketListener extends PacketListener {
    void onHandshake(HandshakeC2SPacket var1);
}

