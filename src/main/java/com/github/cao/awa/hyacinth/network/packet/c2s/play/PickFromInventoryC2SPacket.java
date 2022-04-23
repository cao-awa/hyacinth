package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class PickFromInventoryC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int slot;

    public PickFromInventoryC2SPacket(int slot) {
        this.slot = slot;
    }

    public PickFromInventoryC2SPacket(PacketByteBuf buf) {
        this.slot = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.slot);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onPickFromInventory(this);
    }

    public int getSlot() {
        return this.slot;
    }
}

