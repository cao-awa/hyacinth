package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class CloseHandledScreenC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int syncId;

    public CloseHandledScreenC2SPacket(int syncId) {
        this.syncId = syncId;
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onCloseHandledScreen(this);
    }

    public CloseHandledScreenC2SPacket(PacketByteBuf buf) {
        this.syncId = buf.readByte();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(this.syncId);
    }

    public int getSyncId() {
        return this.syncId;
    }
}

