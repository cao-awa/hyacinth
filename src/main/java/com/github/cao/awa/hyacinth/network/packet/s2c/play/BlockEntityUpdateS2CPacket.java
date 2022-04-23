package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.rmi.registry.Registry;

//public class BlockEntityUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final BlockPos pos;
//    private final BlockEntityType<?> blockEntityType;
//    @Nullable
//    private final NbtCompound nbt;
//
//    public static BlockEntityUpdateS2CPacket create(BlockEntity blockEntity, Function<BlockEntity, NbtCompound> nbtGetter) {
//        return new BlockEntityUpdateS2CPacket(blockEntity.getPos(), blockEntity.getType(), nbtGetter.apply(blockEntity));
//    }
//
//    public static BlockEntityUpdateS2CPacket create(BlockEntity blockEntity) {
//        return BlockEntityUpdateS2CPacket.create(blockEntity, BlockEntity::toInitialChunkDataNbt);
//    }
//
//    private BlockEntityUpdateS2CPacket(BlockPos pos, BlockEntityType<?> blockEntityType, NbtCompound nbt) {
//        this.pos = pos;
//        this.blockEntityType = blockEntityType;
//        this.nbt = nbt.isEmpty() ? null : nbt;
//    }
//
//    public BlockEntityUpdateS2CPacket(PacketByteBuf buf) {
//        this.pos = buf.readBlockPos();
//        this.blockEntityType = (BlockEntityType) Registry.BLOCK_ENTITY_TYPE.get(buf.readVarInt());
//        this.nbt = buf.readNbt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeBlockPos(this.pos);
//        buf.writeVarInt(Registry.BLOCK_ENTITY_TYPE.getRawId(this.blockEntityType));
//        buf.writeNbt(this.nbt);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public BlockPos getPos() {
//        return this.pos;
//    }
//
//    public BlockEntityType<?> getBlockEntityType() {
//        return this.blockEntityType;
//    }
//
//    @Nullable
//    public NbtCompound getNbt() {
//        return this.nbt;
//    }
//}
//
