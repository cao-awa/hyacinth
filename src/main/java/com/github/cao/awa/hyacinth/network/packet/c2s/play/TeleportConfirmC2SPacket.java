package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class TeleportConfirmC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int teleportId;

    public TeleportConfirmC2SPacket(int teleportId) {
        this.teleportId = teleportId;
    }

    public TeleportConfirmC2SPacket(PacketByteBuf buf) {
        this.teleportId = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.teleportId);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onTeleportConfirm(this);
    }

    public int getTeleportId() {
        return this.teleportId;
    }
}

