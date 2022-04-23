package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.rmi.registry.Registry;

//public class BlockEventS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final BlockPos pos;
//    private final int type;
//    private final int data;
//    private final Block block;
//
//    public BlockEventS2CPacket(BlockPos pos, Block block, int type, int data) {
//        this.pos = pos;
//        this.block = block;
//        this.type = type;
//        this.data = data;
//    }
//
//    public BlockEventS2CPacket(PacketByteBuf buf) {
//        this.pos = buf.readBlockPos();
//        this.type = buf.readUnsignedByte();
//        this.data = buf.readUnsignedByte();
//        this.block = Registry.BLOCK.get(buf.readVarInt());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeBlockPos(this.pos);
//        buf.writeByte(this.type);
//        buf.writeByte(this.data);
//        buf.writeVarInt(Registry.BLOCK.getRawId(this.block));
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
//    public int getType() {
//        return this.type;
//    }
//
//    public int getData() {
//        return this.data;
//    }
//
//    public Block getBlock() {
//        return this.block;
//    }
//}
//
