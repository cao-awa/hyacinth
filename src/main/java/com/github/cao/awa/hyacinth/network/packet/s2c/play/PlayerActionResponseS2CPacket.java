package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//public record PlayerActionResponseS2CPacket(BlockPos pos, BlockState state, PlayerActionC2SPacket.Action action, boolean approved) implements Packet<ClientPlayPacketListener>
//{
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    public PlayerActionResponseS2CPacket(BlockPos pos, BlockState state, PlayerActionC2SPacket.Action action, boolean approved, String reason) {
//        this(pos, state, action, approved);
//    }
//
//    public PlayerActionResponseS2CPacket(BlockPos pos, BlockState state, PlayerActionC2SPacket.Action action, boolean approved) {
//        this.pos = pos = pos.toImmutable();
//        this.state = state;
//        this.action = action;
//        this.approved = approved;
//    }
//
//    public PlayerActionResponseS2CPacket(PacketByteBuf buf) {
//        this(buf.readBlockPos(), Block.STATE_IDS.get(buf.readVarInt()), buf.readEnumConstant(PlayerActionC2SPacket.Action.class), buf.readBoolean());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeBlockPos(this.pos);
//        buf.writeVarInt(Block.getRawIdFromState(this.state));
//        buf.writeEnumConstant(this.action);
//        buf.writeBoolean(this.approved);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//}

