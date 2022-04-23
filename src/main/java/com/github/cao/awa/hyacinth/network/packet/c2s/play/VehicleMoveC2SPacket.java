package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.server.entity.Entity;

//public class VehicleMoveC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final double x;
//    private final double y;
//    private final double z;
//    private final float yaw;
//    private final float pitch;
//
//    public VehicleMoveC2SPacket(Entity entity) {
//        this.x = entity.getX();
//        this.y = entity.getY();
//        this.z = entity.getZ();
//        this.yaw = entity.getYaw();
//        this.pitch = entity.getPitch();
//    }
//
//    public VehicleMoveC2SPacket(PacketByteBuf buf) {
//        this.x = buf.readDouble();
//        this.y = buf.readDouble();
//        this.z = buf.readDouble();
//        this.yaw = buf.readFloat();
//        this.pitch = buf.readFloat();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeDouble(this.x);
//        buf.writeDouble(this.y);
//        buf.writeDouble(this.z);
//        buf.writeFloat(this.yaw);
//        buf.writeFloat(this.pitch);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onVehicleMove(this);
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
//    public float getYaw() {
//        return this.yaw;
//    }
//
//    public float getPitch() {
//        return this.pitch;
//    }
//}
//
