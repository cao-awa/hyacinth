package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.util.BitSet;

//public class LightUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int chunkX;
//    private final int chunkZ;
//    private final LightData data;
//
//    public LightUpdateS2CPacket(ChunkPos chunkPos, LightingProvider lightProvider, @Nullable BitSet skyBits, @Nullable BitSet blockBits, boolean nonEdge) {
//        this.chunkX = chunkPos.x;
//        this.chunkZ = chunkPos.z;
//        this.data = new LightData(chunkPos, lightProvider, skyBits, blockBits, nonEdge);
//    }
//
//    public LightUpdateS2CPacket(PacketByteBuf buf) {
//        this.chunkX = buf.readVarInt();
//        this.chunkZ = buf.readVarInt();
//        this.data = new LightData(buf, this.chunkX, this.chunkZ);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.chunkX);
//        buf.writeVarInt(this.chunkZ);
//        this.data.write(buf);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getChunkX() {
//        return this.chunkX;
//    }
//
//    public int getChunkZ() {
//        return this.chunkZ;
//    }
//
//    public LightData getData() {
//        return this.data;
//    }
//}

