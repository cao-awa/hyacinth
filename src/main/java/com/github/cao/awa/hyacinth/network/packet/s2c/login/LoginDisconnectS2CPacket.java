package com.github.cao.awa.hyacinth.network.packet.s2c.login;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.ClientLoginPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;

public class LoginDisconnectS2CPacket implements Packet<ClientLoginPacketListener> {
    private final Text reason;

    public LoginDisconnectS2CPacket(Text reason) {
        this.reason = reason;
    }

    public LoginDisconnectS2CPacket(PacketByteBuf buf) {
        this.reason = Text.Serializer.fromLenientJson(buf.readString(PacketByteBuf.MAX_TEXT_LENGTH));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeText(this.reason);
    }

    @Override
    public void apply(ClientLoginPacketListener clientLoginPacketListener) {
        clientLoginPacketListener.onDisconnect(this);
    }

    public Text getReason() {
        return this.reason;
    }
}

