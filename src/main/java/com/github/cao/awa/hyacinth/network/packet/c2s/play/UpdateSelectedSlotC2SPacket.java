package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class UpdateSelectedSlotC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int selectedSlot;

    public UpdateSelectedSlotC2SPacket(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public UpdateSelectedSlotC2SPacket(PacketByteBuf buf) {
        this.selectedSlot = buf.readShort();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeShort(this.selectedSlot);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onUpdateSelectedSlot(this);
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }
}

