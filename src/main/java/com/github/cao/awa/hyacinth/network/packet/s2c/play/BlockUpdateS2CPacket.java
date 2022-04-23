package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import javax.swing.text.html.BlockView;

//public class BlockUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final BlockPos pos;
//    private final BlockState state;
//
//    public BlockUpdateS2CPacket(BlockPos pos, BlockState state) {
//        this.pos = pos;
//        this.state = state;
//    }
//
//    public BlockUpdateS2CPacket(BlockView world, BlockPos pos) {
//        this(pos, world.getBlockState(pos));
//    }
//
//    public BlockUpdateS2CPacket(PacketByteBuf buf) {
//        this.pos = buf.readBlockPos();
//        this.state = Block.STATE_IDS.get(buf.readVarInt());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeBlockPos(this.pos);
//        buf.writeVarInt(Block.getRawIdFromState(this.state));
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public BlockState getState() {
//        return this.state;
//    }
//
//    public BlockPos getPos() {
//        return this.pos;
//    }
//}
//
