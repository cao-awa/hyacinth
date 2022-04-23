package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.server.entity.Entity;

//public class EntityAnimationS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    public static final int SWING_MAIN_HAND = 0;
//    public static final int DAMAGE = 1;
//    public static final int WAKE_UP = 2;
//    public static final int SWING_OFF_HAND = 3;
//    public static final int CRIT = 4;
//    public static final int ENCHANTED_HIT = 5;
//    private final int id;
//    private final int animationId;
//
//    public EntityAnimationS2CPacket(Entity entity, int animationId) {
//        this.id = entity.getId();
//        this.animationId = animationId;
//    }
//
//    public EntityAnimationS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.animationId = buf.readUnsignedByte();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeByte(this.animationId);
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
//    public int getAnimationId() {
//        return this.animationId;
//    }
//}

