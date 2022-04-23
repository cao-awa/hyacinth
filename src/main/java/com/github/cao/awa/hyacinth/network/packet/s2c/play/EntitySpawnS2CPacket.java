package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.vec.Vec3d;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.rmi.registry.Registry;
import java.util.UUID;

//public class EntitySpawnS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    public static final double VELOCITY_SCALE = 8000.0;
//    private final int id;
//    private final UUID uuid;
//    private final double x;
//    private final double y;
//    private final double z;
//    private final int velocityX;
//    private final int velocityY;
//    private final int velocityZ;
//    private final int pitch;
//    private final int yaw;
//    private final EntityType<?> entityTypeId;
//    private final int entityData;
//    /**
//     * The maximum absolute value allowed for each scalar value (velocity x, y, z)
//     * in the velocity vector sent by this packet.
//     */
//    public static final double MAX_ABSOLUTE_VELOCITY = 3.9;
//
//    public EntitySpawnS2CPacket(int id, UUID uuid, double x, double y, double z, float pitch, float yaw, EntityType<?> entityTypeId, int entityData, Vec3d velocity) {
//        this.id = id;
//        this.uuid = uuid;
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.pitch = MathHelper.floor(pitch * 256.0f / 360.0f);
//        this.yaw = MathHelper.floor(yaw * 256.0f / 360.0f);
//        this.entityTypeId = entityTypeId;
//        this.entityData = entityData;
//        this.velocityX = (int)(MathHelper.clamp(velocity.x, -3.9, 3.9) * 8000.0);
//        this.velocityY = (int)(MathHelper.clamp(velocity.y, -3.9, 3.9) * 8000.0);
//        this.velocityZ = (int)(MathHelper.clamp(velocity.z, -3.9, 3.9) * 8000.0);
//    }
//
//    public EntitySpawnS2CPacket(Entity entity) {
//        this(entity, 0);
//    }
//
//    public EntitySpawnS2CPacket(Entity entity, int entityData) {
//        this(entity.getId(), entity.getUuid(), entity.getX(), entity.getY(), entity.getZ(), entity.getPitch(), entity.getYaw(), entity.getType(), entityData, entity.getVelocity());
//    }
//
//    public EntitySpawnS2CPacket(Entity entity, EntityType<?> entityType, int data, BlockPos pos) {
//        this(entity.getId(), entity.getUuid(), pos.getX(), pos.getY(), pos.getZ(), entity.getPitch(), entity.getYaw(), entityType, data, entity.getVelocity());
//    }
//
//    public EntitySpawnS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.uuid = buf.readUuid();
//        this.entityTypeId = Registry.ENTITY_TYPE.get(buf.readVarInt());
//        this.x = buf.readDouble();
//        this.y = buf.readDouble();
//        this.z = buf.readDouble();
//        this.pitch = buf.readByte();
//        this.yaw = buf.readByte();
//        this.entityData = buf.readInt();
//        this.velocityX = buf.readShort();
//        this.velocityY = buf.readShort();
//        this.velocityZ = buf.readShort();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeUuid(this.uuid);
//        buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(this.entityTypeId));
//        buf.writeDouble(this.x);
//        buf.writeDouble(this.y);
//        buf.writeDouble(this.z);
//        buf.writeByte(this.pitch);
//        buf.writeByte(this.yaw);
//        buf.writeInt(this.entityData);
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
//    public UUID getUuid() {
//        return this.uuid;
//    }
//
//    public double getX() {
//        return this.x;
//    }
//
//    public double getY() {
//        return this.y;
//    }
//
//    public double getZ() {
//        return this.z;
//    }
//
//    public double getVelocityX() {
//        return (double)this.velocityX / 8000.0;
//    }
//
//    public double getVelocityY() {
//        return (double)this.velocityY / 8000.0;
//    }
//
//    public double getVelocityZ() {
//        return (double)this.velocityZ / 8000.0;
//    }
//
//    public int getPitch() {
//        return this.pitch;
//    }
//
//    public int getYaw() {
//        return this.yaw;
//    }
//
//    public EntityType<?> getEntityTypeId() {
//        return this.entityTypeId;
//    }
//
//    public int getEntityData() {
//        return this.entityData;
//    }
//}
//
