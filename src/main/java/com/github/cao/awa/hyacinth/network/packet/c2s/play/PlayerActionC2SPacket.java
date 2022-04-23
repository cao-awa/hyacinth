package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.direction.Direction;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class PlayerActionC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final BlockPos pos;
//    private final Direction direction;
//    private final Action action;
//
//    public PlayerActionC2SPacket(Action action, BlockPos pos, Direction direction) {
//        this.action = action;
//        this.pos = pos.toImmutable();
//        this.direction = direction;
//    }
//
//    public PlayerActionC2SPacket(PacketByteBuf buf) {
//        this.action = buf.readEnumConstant(Action.class);
//        this.pos = buf.readBlockPos();
//        this.direction = Direction.byId(buf.readUnsignedByte());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeEnumConstant(this.action);
//        buf.writeBlockPos(this.pos);
//        buf.writeByte(this.direction.getId());
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onPlayerAction(this);
//    }
//
//    public BlockPos getPos() {
//        return this.pos;
//    }
//
//    public Direction getDirection() {
//        return this.direction;
//    }
//
//    public Action getAction() {
//        return this.action;
//    }
//
//    public static enum Action {
//        START_DESTROY_BLOCK,
//        ABORT_DESTROY_BLOCK,
//        STOP_DESTROY_BLOCK,
//        DROP_ALL_ITEMS,
//        DROP_ITEM,
//        RELEASE_USE_ITEM,
//        SWAP_ITEM_WITH_OFFHAND;
//
//    }
//}
//
