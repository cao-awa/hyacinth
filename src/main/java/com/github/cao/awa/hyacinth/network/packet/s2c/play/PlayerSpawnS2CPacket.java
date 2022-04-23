package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.server.entity.player.PlayerEntity;

import java.util.UUID;

//public class PlayerSpawnS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final UUID uuid;
//    private final double x;
//    private final double y;
//    private final double z;
//    private final byte yaw;
//    private final byte pitch;
//
//    public PlayerSpawnS2CPacket(PlayerEntity player) {
//        this.id = player.getId();
//        this.uuid = player.getGameProfile().getId();
//        this.x = player.getX();
//        this.y = player.getY();
//        this.z = player.getZ();
//        this.yaw = (byte)(player.getYaw() * 256.0f / 360.0f);
//        this.pitch = (byte)(player.getPitch() * 256.0f / 360.0f);
//    }
//
//    public PlayerSpawnS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.uuid = buf.readUuid();
//        this.x = buf.readDouble();
//        this.y = buf.readDouble();
//        this.z = buf.readDouble();
//        this.yaw = buf.readByte();
//        this.pitch = buf.readByte();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeUuid(this.uuid);
//        buf.writeDouble(this.x);
//        buf.writeDouble(this.y);
//        buf.writeDouble(this.z);
//        buf.writeByte(this.yaw);
//        buf.writeByte(this.pitch);
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
//    public UUID getPlayerUuid() {
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
//    public byte getYaw() {
//        return this.yaw;
//    }
//
//    public byte getPitch() {
//        return this.pitch;
//    }
//}

