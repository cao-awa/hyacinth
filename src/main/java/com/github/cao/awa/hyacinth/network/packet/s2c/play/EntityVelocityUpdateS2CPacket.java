package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.vec.Vec3d;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

/**
 * Sent when a server modifies an entity's velocity.
 * 
 * <p>If the entity is a player, {@link
 * ExplosionS2CPacket} can be used as
 * a replacement.
 */
//public class EntityVelocityUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final int velocityX;
//    private final int velocityY;
//    private final int velocityZ;
//
//    public EntityVelocityUpdateS2CPacket(Entity entity) {
//        this(entity.getId(), entity.getVelocity());
//    }
//
//    public EntityVelocityUpdateS2CPacket(int id, Vec3d velocity) {
//        this.id = id;
//        double d = 3.9;
//        double e = MathHelper.clamp(velocity.x, -3.9, 3.9);
//        double f = MathHelper.clamp(velocity.y, -3.9, 3.9);
//        double g = MathHelper.clamp(velocity.z, -3.9, 3.9);
//        this.velocityX = (int)(e * 8000.0);
//        this.velocityY = (int)(f * 8000.0);
//        this.velocityZ = (int)(g * 8000.0);
//    }
//
//    public EntityVelocityUpdateS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.velocityX = buf.readShort();
//        this.velocityY = buf.readShort();
//        this.velocityZ = buf.readShort();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeShort(this.velocityX);
//        buf.writeShort(this.velocityY);
//        buf.writeShort(this.velocityZ);
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
//    public int getVelocityX() {
//        return this.velocityX;
//    }
//
//    public int getVelocityY() {
//        return this.velocityY;
//    }
//
//    public int getVelocityZ() {
//        return this.velocityZ;
//    }
//}

