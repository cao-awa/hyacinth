package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class EntityAttachS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int attachedId;
//    private final int holdingId;
//
//    public EntityAttachS2CPacket(Entity attachedEntity, @Nullable Entity holdingEntity) {
//        this.attachedId = attachedEntity.getId();
//        this.holdingId = holdingEntity != null ? holdingEntity.getId() : 0;
//    }
//
//    public EntityAttachS2CPacket(PacketByteBuf buf) {
//        this.attachedId = buf.readInt();
//        this.holdingId = buf.readInt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeInt(this.attachedId);
//        buf.writeInt(this.holdingId);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getAttachedEntityId() {
//        return this.attachedId;
//    }
//
//    public int getHoldingEntityId() {
//        return this.holdingId;
//    }
//}
//
