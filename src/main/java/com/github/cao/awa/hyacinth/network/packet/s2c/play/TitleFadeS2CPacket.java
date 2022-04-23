package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class TitleFadeS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int fadeInTicks;
    private final int remainTicks;
    private final int fadeOutTicks;

    public TitleFadeS2CPacket(int fadeInTicks, int remainTicks, int fadeOutTicks) {
        this.fadeInTicks = fadeInTicks;
        this.remainTicks = remainTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    public TitleFadeS2CPacket(PacketByteBuf buf) {
        this.fadeInTicks = buf.readInt();
        this.remainTicks = buf.readInt();
        this.fadeOutTicks = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.fadeInTicks);
        buf.writeInt(this.remainTicks);
        buf.writeInt(this.fadeOutTicks);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public int getFadeInTicks() {
        return this.fadeInTicks;
    }

    public int getRemainTicks() {
        return this.remainTicks;
    }

    public int getFadeOutTicks() {
        return this.fadeOutTicks;
    }
}

