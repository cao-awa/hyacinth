package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.vec.Vec3d;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class PlaySoundIdS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    public static final float COORDINATE_SCALE = 8.0f;
//    private final Identifier id;
//    private final SoundCategory category;
//    private final int fixedX;
//    private final int fixedY;
//    private final int fixedZ;
//    private final float volume;
//    private final float pitch;
//
//    public PlaySoundIdS2CPacket(Identifier sound, SoundCategory category, Vec3d pos, float volume, float pitch) {
//        this.id = sound;
//        this.category = category;
//        this.fixedX = (int)(pos.x * 8.0);
//        this.fixedY = (int)(pos.y * 8.0);
//        this.fixedZ = (int)(pos.z * 8.0);
//        this.volume = volume;
//        this.pitch = pitch;
//    }
//
//    public PlaySoundIdS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readIdentifier();
//        this.category = buf.readEnumConstant(SoundCategory.class);
//        this.fixedX = buf.readInt();
//        this.fixedY = buf.readInt();
//        this.fixedZ = buf.readInt();
//        this.volume = buf.readFloat();
//        this.pitch = buf.readFloat();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeIdentifier(this.id);
//        buf.writeEnumConstant(this.category);
//        buf.writeInt(this.fixedX);
//        buf.writeInt(this.fixedY);
//        buf.writeInt(this.fixedZ);
//        buf.writeFloat(this.volume);
//        buf.writeFloat(this.pitch);
//    }
//
//    public Identifier getSoundId() {
//        return this.id;
//    }
//
//    public SoundCategory getCategory() {
//        return this.category;
//    }
//
//    public double getX() {
//        return (float)this.fixedX / 8.0f;
//    }
//
//    public double getY() {
//        return (float)this.fixedY / 8.0f;
//    }
//
//    public double getZ() {
//        return (float)this.fixedZ / 8.0f;
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
//
