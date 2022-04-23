package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;

public class OverlayMessageS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final Text message;

    public OverlayMessageS2CPacket(Text message) {
        this.message = message;
    }

    public OverlayMessageS2CPacket(PacketByteBuf buf) {
        this.message = buf.readText();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeText(this.message);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public Text getMessage() {
        return this.message;
    }
}

