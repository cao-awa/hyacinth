package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class ChunkRenderDistanceCenterS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int chunkX;
    private final int chunkZ;

    public ChunkRenderDistanceCenterS2CPacket(int x, int z) {
        this.chunkX = x;
        this.chunkZ = z;
    }

    public ChunkRenderDistanceCenterS2CPacket(PacketByteBuf buf) {
        this.chunkX = buf.readVarInt();
        this.chunkZ = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.chunkX);
        buf.writeVarInt(this.chunkZ);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }
}

