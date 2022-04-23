package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import org.apache.commons.lang3.Validate;

import java.rmi.registry.Registry;

//public class PlaySoundFromEntityS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final SoundEvent sound;
//    private final SoundCategory category;
//    private final int entityId;
//    private final float volume;
//    private final float pitch;
//
//    public PlaySoundFromEntityS2CPacket(SoundEvent sound, SoundCategory category, Entity entity, float volume, float pitch) {
//        Validate.notNull(sound, "sound", new Object[0]);
//        this.sound = sound;
//        this.category = category;
//        this.entityId = entity.getId();
//        this.volume = volume;
//        this.pitch = pitch;
//    }
//
//    public PlaySoundFromEntityS2CPacket(PacketByteBuf buf) {
//        this.sound = (SoundEvent) Registry.SOUND_EVENT.get(buf.readVarInt());
//        this.category = buf.readEnumConstant(SoundCategory.class);
//        this.entityId = buf.readVarInt();
//        this.volume = buf.readFloat();
//        this.pitch = buf.readFloat();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(Registry.SOUND_EVENT.getRawId(this.sound));
//        buf.writeEnumConstant(this.category);
//        buf.writeVarInt(this.entityId);
//        buf.writeFloat(this.volume);
//        buf.writeFloat(this.pitch);
//    }
//
//    public SoundEvent getSound() {
//        return this.sound;
//    }
//
//    public SoundCategory getCategory() {
//        return this.category;
//    }
//
//    public int getEntityId() {
//        return this.entityId;
//    }
//
//    public float getVolume() {
//        return this.volume;
//    }
//
//    public float getPitch() {
//        return this.pitch;
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//}

