package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.util.BitSet;

//public class ChunkDataS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int chunkX;
//    private final int chunkZ;
//    private final ChunkData chunkData;
//    private final LightData lightData;
//
//    public ChunkDataS2CPacket(WorldChunk chunk, LightingProvider lightProvider, @Nullable BitSet skyBits, @Nullable BitSet blockBits, boolean nonEdge) {
//        ChunkPos chunkPos = chunk.getPos();
//        this.chunkX = chunkPos.x;
//        this.chunkZ = chunkPos.z;
//        this.chunkData = new ChunkData(chunk);
//        this.lightData = new LightData(chunkPos, lightProvider, skyBits, blockBits, nonEdge);
//    }
//
//    public ChunkDataS2CPacket(PacketByteBuf buf) {
//        this.chunkX = buf.readInt();
//        this.chunkZ = buf.readInt();
//        this.chunkData = new ChunkData(buf, this.chunkX, this.chunkZ);
//        this.lightData = new LightData(buf, this.chunkX, this.chunkZ);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeInt(this.chunkX);
//        buf.writeInt(this.chunkZ);
//        this.chunkData.write(buf);
//        this.lightData.write(buf);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getX() {
//        return this.chunkX;
//    }
//
//    public int getZ() {
//        return this.chunkZ;
//    }
//
//    public ChunkData getChunkData() {
//        return this.chunkData;
//    }
//
//    public LightData getLightData() {
//        return this.lightData;
//    }
//}
//
