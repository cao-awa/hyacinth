package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class ExperienceBarUpdateS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final float barProgress;
    private final int experienceLevel;
    private final int experience;

    public ExperienceBarUpdateS2CPacket(float barProgress, int experienceLevel, int experience) {
        this.barProgress = barProgress;
        this.experienceLevel = experienceLevel;
        this.experience = experience;
    }

    public ExperienceBarUpdateS2CPacket(PacketByteBuf buf) {
        this.barProgress = buf.readFloat();
        this.experience = buf.readVarInt();
        this.experienceLevel = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.barProgress);
        buf.writeVarInt(this.experience);
        buf.writeVarInt(this.experienceLevel);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public float getBarProgress() {
        return this.barProgress;
    }

    public int getExperienceLevel() {
        return this.experienceLevel;
    }

    public int getExperience() {
        return this.experience;
    }
}

