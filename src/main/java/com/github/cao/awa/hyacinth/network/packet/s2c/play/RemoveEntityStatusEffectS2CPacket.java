//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import com.github.cao.awa.hyacinth.server.world.World;
//import org.jetbrains.annotations.Nullable;
//
//public class RemoveEntityStatusEffectS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int entityId;
//    private final StatusEffect effectType;
//
//    public RemoveEntityStatusEffectS2CPacket(int entityId, StatusEffect effectType) {
//        this.entityId = entityId;
//        this.effectType = effectType;
//    }
//
//    public RemoveEntityStatusEffectS2CPacket(PacketByteBuf buf) {
//        this.entityId = buf.readVarInt();
//        this.effectType = StatusEffect.byRawId(buf.readUnsignedByte());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.entityId);
//        buf.writeByte(StatusEffect.getRawId(this.effectType));
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    @Nullable
//    public Entity getEntity(World world) {
//        return world.getEntityById(this.entityId);
//    }
//
//    @Nullable
//    public StatusEffect getEffectType() {
//        return this.effectType;
//    }
//}
//
