package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


// TODO: 2022/4/23
//public class ChunkData {
//    private static final int MAX_SECTIONS_DATA_SIZE = 0x200000;
//    private final NbtCompound heightmap;
//    private final byte[] sectionsData;
//    private final List<BlockEntityData> blockEntities;
//
//    public ChunkData(WorldChunk chunk) {
//        this.heightmap = new NbtCompound();
//        for (Map.Entry<Heightmap.Type, Heightmap> entry : chunk.getHeightmaps()) {
//            if (!entry.getKey().shouldSendToClient()) continue;
//            this.heightmap.put(entry.getKey().getName(), new NbtLongArray(entry.getValue().asLongArray()));
//        }
//        this.sectionsData = new byte[ChunkData.getSectionsPacketSize(chunk)];
//        ChunkData.writeSections(new PacketByteBuf(this.getWritableSectionsDataBuf()), chunk);
//        this.blockEntities = Lists.newArrayList();
//        for (Map.Entry<Object, Object> entry : chunk.getBlockEntities().entrySet()) {
//            this.blockEntities.add(BlockEntityData.of((BlockEntity)entry.getValue()));
//        }
//    }
//
//    public ChunkData(PacketByteBuf buf, int x, int z) {
//        this.heightmap = buf.readNbt();
//        if (this.heightmap == null) {
//            throw new RuntimeException("Can't read heightmap in packet for [" + x + ", " + z + "]");
//        }
//        int i = buf.readVarInt();
//        if (i > 0x200000) {
//            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
//        }
//        this.sectionsData = new byte[i];
//        buf.readBytes(this.sectionsData);
//        this.blockEntities = buf.readList(BlockEntityData::new);
//    }
//
//    public void write(PacketByteBuf buf2) {
//        buf2.writeNbt(this.heightmap);
//        buf2.writeVarInt(this.sectionsData.length);
//        buf2.writeBytes(this.sectionsData);
//        buf2.writeCollection(this.blockEntities, (buf, entry) -> entry.write((PacketByteBuf)buf));
//    }
//
//    private static int getSectionsPacketSize(WorldChunk chunk) {
//        int i = 0;
//        for (ChunkSection chunkSection : chunk.getSectionArray()) {
//            i += chunkSection.getPacketSize();
//        }
//        return i;
//    }
//
//    private ByteBuf getWritableSectionsDataBuf() {
//        ByteBuf byteBuf = Unpooled.wrappedBuffer(this.sectionsData);
//        byteBuf.writerIndex(0);
//        return byteBuf;
//    }
//
//    public static void writeSections(PacketByteBuf buf, WorldChunk chunk) {
//        for (ChunkSection chunkSection : chunk.getSectionArray()) {
//            chunkSection.toPacket(buf);
//        }
//    }
//
//    public Consumer<BlockEntityVisitor> getBlockEntities(int x, int z) {
//        return visitor -> this.iterateBlockEntities((BlockEntityVisitor)visitor, x, z);
//    }
//
//    private void iterateBlockEntities(BlockEntityVisitor consumer, int x, int z) {
//        int i = 16 * x;
//        int j = 16 * z;
//        BlockPos.Mutable mutable = new BlockPos.Mutable();
//        for (BlockEntityData blockEntityData : this.blockEntities) {
//            int k = i + ChunkSectionPos.getLocalCoord(blockEntityData.localXz >> 4);
//            int l = j + ChunkSectionPos.getLocalCoord(blockEntityData.localXz);
//            mutable.set(k, blockEntityData.y, l);
//            consumer.accept(mutable, blockEntityData.type, blockEntityData.nbt);
//        }
//    }
//
//    public PacketByteBuf getSectionsDataBuf() {
//        return new PacketByteBuf(Unpooled.wrappedBuffer(this.sectionsData));
//    }
//
//    public NbtCompound getHeightmap() {
//        return this.heightmap;
//    }
//
//    static class BlockEntityData {
//        final int localXz;
//        final int y;
//        final BlockEntityType<?> type;
//        @Nullable
//        final NbtCompound nbt;
//
//        private BlockEntityData(int localXz, int y, BlockEntityType<?> type, @Nullable NbtCompound nbt) {
//            this.localXz = localXz;
//            this.y = y;
//            this.type = type;
//            this.nbt = nbt;
//        }
//
//        private BlockEntityData(PacketByteBuf buf) {
//            this.localXz = buf.readByte();
//            this.y = buf.readShort();
//            int i = buf.readVarInt();
//            this.type = (BlockEntityType) Registry.BLOCK_ENTITY_TYPE.get(i);
//            this.nbt = buf.readNbt();
//        }
//
//        void write(PacketByteBuf buf) {
//            buf.writeByte(this.localXz);
//            buf.writeShort(this.y);
//            buf.writeVarInt(Registry.BLOCK_ENTITY_TYPE.getRawId(this.type));
//            buf.writeNbt(this.nbt);
//        }
//
//        static BlockEntityData of(BlockEntity blockEntity) {
//            NbtCompound nbtCompound = blockEntity.toInitialChunkDataNbt();
//            BlockPos blockPos = blockEntity.getPos();
//            int i = ChunkSectionPos.getLocalCoord(blockPos.getX()) << 4 | ChunkSectionPos.getLocalCoord(blockPos.getZ());
//            return new BlockEntityData(i, blockPos.getY(), blockEntity.getType(), nbtCompound.isEmpty() ? null : nbtCompound);
//        }
//    }
//
//    @FunctionalInterface
//    public static interface BlockEntityVisitor {
//        public void accept(BlockPos var1, BlockEntityType<?> var2, @Nullable NbtCompound var3);
//    }
//}
//
