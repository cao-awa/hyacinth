package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class ExperienceOrbSpawnS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final double x;
//    private final double y;
//    private final double z;
//    private final int experience;
//
//    public ExperienceOrbSpawnS2CPacket(ExperienceOrbEntity experienceOrbEntity) {
//        this.id = experienceOrbEntity.getId();
//        this.x = experienceOrbEntity.getX();
//        this.y = experienceOrbEntity.getY();
//        this.z = experienceOrbEntity.getZ();
//        this.experience = experienceOrbEntity.getExperienceAmount();
//    }
//
//    public ExperienceOrbSpawnS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.x = buf.readDouble();
//        this.y = buf.readDouble();
//        this.z = buf.readDouble();
//        this.experience = buf.readShort();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeDouble(this.x);
//        buf.writeDouble(this.y);
//        buf.writeDouble(this.z);
//        buf.writeShort(this.experience);
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
//    public int getExperience() {
//        return this.experience;
//    }
//}

