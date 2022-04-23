package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class RenameItemC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final String name;

    public RenameItemC2SPacket(String name) {
        this.name = name;
    }

    public RenameItemC2SPacket(PacketByteBuf buf) {
        this.name = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.name);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onRenameItem(this);
    }

    public String getName() {
        return this.name;
    }
}

