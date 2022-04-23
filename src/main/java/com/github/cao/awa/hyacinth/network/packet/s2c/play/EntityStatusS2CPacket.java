package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.server.world.World;

//public class EntityStatusS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final byte status;
//
//    public EntityStatusS2CPacket(Entity entity, byte status) {
//        this.id = entity.getId();
//        this.status = status;
//    }
//
//    public EntityStatusS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readInt();
//        this.status = buf.readByte();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeInt(this.id);
//        buf.writeByte(this.status);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    @Nullable
//    public Entity getEntity(World world) {
//        return world.getEntityById(this.id);
//    }
//
//    public byte getStatus() {
//        return this.status;
//    }
//}
//
