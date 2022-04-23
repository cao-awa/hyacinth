//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import com.github.cao.awa.hyacinth.server.entity.Entity;
//import com.github.cao.awa.hyacinth.server.world.World;
//import org.jetbrains.annotations.Nullable;
//
//public class SetCameraEntityS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int entityId;
//
//    public SetCameraEntityS2CPacket(Entity entity) {
//        this.entityId = entity.getId();
//    }
//
//    public SetCameraEntityS2CPacket(PacketByteBuf buf) {
//        this.entityId = buf.readVarInt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.entityId);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    @Nullable
//    public Entity getEntity(World world) {
//        return world.getEntityById(this.entityId);
//    }
//}
//
