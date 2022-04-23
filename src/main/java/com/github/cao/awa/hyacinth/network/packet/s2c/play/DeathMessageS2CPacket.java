package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class DeathMessageS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int entityId;
//    private final int killerId;
//    private final Text message;
//
//    public DeathMessageS2CPacket(DamageTracker damageTracker, Text message) {
//        this(damageTracker.getEntity().getId(), damageTracker.getBiggestAttackerId(), message);
//    }
//
//    public DeathMessageS2CPacket(int entityId, int killerId, Text message) {
//        this.entityId = entityId;
//        this.killerId = killerId;
//        this.message = message;
//    }
//
//    public DeathMessageS2CPacket(PacketByteBuf buf) {
//        this.entityId = buf.readVarInt();
//        this.killerId = buf.readInt();
//        this.message = buf.readText();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.entityId);
//        buf.writeInt(this.killerId);
//        buf.writeText(this.message);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    @Override
//    public boolean isWritingErrorSkippable() {
//        return true;
//    }
//
//    public int getKillerId() {
//        return this.killerId;
//    }
//
//    public int getEntityId() {
//        return this.entityId;
//    }
//
//    public Text getMessage() {
//        return this.message;
//    }
//}

