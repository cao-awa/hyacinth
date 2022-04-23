package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.direction.Direction;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.rmi.registry.Registry;
import java.util.UUID;

//public class PaintingSpawnS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final UUID uuid;
//    private final BlockPos pos;
//    private final Direction facing;
//    private final int motiveId;
//
//    public PaintingSpawnS2CPacket(PaintingEntity entity) {
//        this.id = entity.getId();
//        this.uuid = entity.getUuid();
//        this.pos = entity.getDecorationBlockPos();
//        this.facing = entity.getHorizontalFacing();
//        this.motiveId = Registry.PAINTING_MOTIVE.getRawId(entity.motive);
//    }
//
//    public PaintingSpawnS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.uuid = buf.readUuid();
//        this.motiveId = buf.readVarInt();
//        this.pos = buf.readBlockPos();
//        this.facing = Direction.fromHorizontal(buf.readUnsignedByte());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeUuid(this.uuid);
//        buf.writeVarInt(this.motiveId);
//        buf.writeBlockPos(this.pos);
//        buf.writeByte(this.facing.getHorizontal());
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getId() {
//        return this.id;
//    }
//
//    public UUID getPaintingUuid() {
//        return this.uuid;
//    }
//
//    public BlockPos getPos() {
//        return this.pos;
//    }
//
//    public Direction getFacing() {
//        return this.facing;
//    }
//
//    public PaintingMotive getMotive() {
//        return Registry.PAINTING_MOTIVE.get(this.motiveId);
//    }
//}
//
