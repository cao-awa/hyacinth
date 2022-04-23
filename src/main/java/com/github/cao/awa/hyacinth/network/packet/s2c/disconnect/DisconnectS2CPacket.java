package com.github.cao.awa.hyacinth.network.packet.s2c.disconnect;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;

public class DisconnectS2CPacket implements Packet<ClientPlayPacketListener> {
    private final Text reason;

    public DisconnectS2CPacket(Text reason) {
        this.reason = reason;
    }

    public DisconnectS2CPacket(PacketByteBuf buf) {
        this.reason = buf.readText();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeText(this.reason);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        clientPlayPacketListener.onDisconnect(this);
    }

    public Text getReason() {
        return this.reason;
    }
}

