package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class UpdateSelectedSlotS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int selectedSlot;

    public UpdateSelectedSlotS2CPacket(int slot) {
        this.selectedSlot = slot;
    }

    public UpdateSelectedSlotS2CPacket(PacketByteBuf buf) {
        this.selectedSlot = buf.readByte();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(this.selectedSlot);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public int getSlot() {
        return this.selectedSlot;
    }
}

