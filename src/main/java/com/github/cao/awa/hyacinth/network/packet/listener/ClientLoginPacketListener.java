package com.github.cao.awa.hyacinth.network.packet.listener;

import com.github.cao.awa.hyacinth.network.packet.s2c.login.*;

public interface ClientLoginPacketListener
        extends PacketListener {
    void onHello(LoginHelloS2CPacket var1);

    void onSuccess(LoginSuccessS2CPacket var1);

    void onDisconnect(LoginDisconnectS2CPacket var1);

    void onCompression(LoginCompressionS2CPacket var1);

    void onQueryRequest(LoginQueryRequestS2CPacket var1);
}

