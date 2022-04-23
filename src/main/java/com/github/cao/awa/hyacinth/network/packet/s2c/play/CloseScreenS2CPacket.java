package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class CloseScreenS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int syncId;

    public CloseScreenS2CPacket(int syncId) {
        this.syncId = syncId;
    }

    public CloseScreenS2CPacket(PacketByteBuf buf) {
        this.syncId = buf.readUnsignedByte();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(this.syncId);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public int getSyncId() {
        return this.syncId;
    }
}

