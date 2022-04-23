package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class UnloadChunkS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int x;
    private final int z;

    public UnloadChunkS2CPacket(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public UnloadChunkS2CPacket(PacketByteBuf buf) {
        this.x = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}

