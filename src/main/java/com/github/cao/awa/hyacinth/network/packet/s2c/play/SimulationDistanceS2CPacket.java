package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public record SimulationDistanceS2CPacket(int simulationDistance) implements Packet<ClientPlayPacketListener>
{
    public SimulationDistanceS2CPacket(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.simulationDistance);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }
}

