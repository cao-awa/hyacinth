package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class ClearTitleS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final boolean reset;

    public ClearTitleS2CPacket(boolean reset) {
        this.reset = reset;
    }

    public ClearTitleS2CPacket(PacketByteBuf buf) {
        this.reset = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.reset);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public boolean shouldReset() {
        return this.reset;
    }
}

