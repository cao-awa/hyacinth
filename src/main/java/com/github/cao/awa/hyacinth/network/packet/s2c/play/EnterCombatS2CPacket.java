package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class EnterCombatS2CPacket
implements Packet<ClientPlayPacketListener> {
    public EnterCombatS2CPacket() {
    }

    public EnterCombatS2CPacket(PacketByteBuf buf) {
    }

    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }
}

