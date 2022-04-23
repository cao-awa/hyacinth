package com.github.cao.awa.hyacinth.network.packet.listener;

import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginHelloC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginKeyC2SPacket;

public interface ServerLoginPacketListener
        extends PacketListener {
    void onHello(LoginHelloC2SPacket var1);

    void onKey(LoginKeyC2SPacket var1);

    void onQueryResponse(LoginQueryResponseC2SPacket var1);
}

