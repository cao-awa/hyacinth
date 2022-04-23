package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.server.entity.Entity;
import com.github.cao.awa.hyacinth.server.world.World;

//public class EntitySetHeadYawS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int entity;
//    private final byte headYaw;
//
//    public EntitySetHeadYawS2CPacket(Entity entity, byte headYaw) {
//        this.entity = entity.getId();
//        this.headYaw = headYaw;
//    }
//
//    public EntitySetHeadYawS2CPacket(PacketByteBuf buf) {
//        this.entity = buf.readVarInt();
//        this.headYaw = buf.readByte();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.entity);
//        buf.writeByte(this.headYaw);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public Entity getEntity(World world) {
//        return world.getEntityById(this.entity);
//    }
//
//    public byte getHeadYaw() {
//        return this.headYaw;
//    }
//}
//
