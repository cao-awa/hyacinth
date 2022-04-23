package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;

public class SubtitleS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final Text subtitle;

    public SubtitleS2CPacket(Text subtitle) {
        this.subtitle = subtitle;
    }

    public SubtitleS2CPacket(PacketByteBuf buf) {
        this.subtitle = buf.readText();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeText(this.subtitle);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public Text getSubtitle() {
        return this.subtitle;
    }
}

