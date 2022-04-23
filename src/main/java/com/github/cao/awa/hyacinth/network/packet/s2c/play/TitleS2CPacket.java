package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;

public class TitleS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final Text title;

    public TitleS2CPacket(Text title) {
        this.title = title;
    }

    public TitleS2CPacket(PacketByteBuf buf) {
        this.title = buf.readText();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeText(this.title);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public Text getTitle() {
        return this.title;
    }
}

