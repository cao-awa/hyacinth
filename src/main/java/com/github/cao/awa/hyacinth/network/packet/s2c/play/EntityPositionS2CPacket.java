package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class EntityPositionS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final double x;
//    private final double y;
//    private final double z;
//    private final byte yaw;
//    private final byte pitch;
//    private final boolean onGround;
//
//    public EntityPositionS2CPacket(Entity entity) {
//        this.id = entity.getId();
//        this.x = entity.getX();
//        this.y = entity.getY();
//        this.z = entity.getZ();
//        this.yaw = (byte)(entity.getYaw() * 256.0f / 360.0f);
//        this.pitch = (byte)(entity.getPitch() * 256.0f / 360.0f);
//        this.onGround = entity.isOnGround();
//    }
//
//    public EntityPositionS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.x = buf.readDouble();
//        this.y = buf.readDouble();
//        this.z = buf.readDouble();
//        this.yaw = buf.readByte();
//        this.pitch = buf.readByte();
//        this.onGround = buf.readBoolean();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeDouble(this.x);
//        buf.writeDouble(this.y);
//        buf.writeDouble(this.z);
//        buf.writeByte(this.yaw);
//        buf.writeByte(this.pitch);
//        buf.writeBoolean(this.onGround);
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
//    public byte getYaw() {
//        return this.yaw;
//    }
//
//    public byte getPitch() {
//        return this.pitch;
//    }
//
//    public boolean isOnGround() {
//        return this.onGround;
//    }
//}

