package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.vec.Vec3d;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Sent when an explosion occurs in the world.
 * 
 * <p>The client will update {@linkplain
 * net.minecraft.client.MinecraftClient#player the player}'s velocity as
 * well as performing an explosion.
 * 
 * @see EntityVelocityUpdateS2CPacket
 */
//public class ExplosionS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final double x;
//    private final double y;
//    private final double z;
//    private final float radius;
//    private final List<BlockPos> affectedBlocks;
//    private final float playerVelocityX;
//    private final float playerVelocityY;
//    private final float playerVelocityZ;
//
//    public ExplosionS2CPacket(double x, double y, double z, float radius, List<BlockPos> affectedBlocks, @Nullable Vec3d playerVelocity) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.radius = radius;
//        this.affectedBlocks = Lists.newArrayList(affectedBlocks);
//        if (playerVelocity != null) {
//            this.playerVelocityX = (float)playerVelocity.x;
//            this.playerVelocityY = (float)playerVelocity.y;
//            this.playerVelocityZ = (float)playerVelocity.z;
//        } else {
//            this.playerVelocityX = 0.0f;
//            this.playerVelocityY = 0.0f;
//            this.playerVelocityZ = 0.0f;
//        }
//    }
//
//    public ExplosionS2CPacket(PacketByteBuf buf) {
//        this.x = buf.readFloat();
//        this.y = buf.readFloat();
//        this.z = buf.readFloat();
//        this.radius = buf.readFloat();
//        int i = MathHelper.floor(this.x);
//        int j = MathHelper.floor(this.y);
//        int k = MathHelper.floor(this.z);
//        this.affectedBlocks = buf.readList(packetByteBuf -> {
//            int l = packetByteBuf.readByte() + i;
//            int m = packetByteBuf.readByte() + j;
//            int n = packetByteBuf.readByte() + k;
//            return new BlockPos(l, m, n);
//        });
//        this.playerVelocityX = buf.readFloat();
//        this.playerVelocityY = buf.readFloat();
//        this.playerVelocityZ = buf.readFloat();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf2) {
//        buf2.writeFloat((float)this.x);
//        buf2.writeFloat((float)this.y);
//        buf2.writeFloat((float)this.z);
//        buf2.writeFloat(this.radius);
//        int i = MathHelper.floor(this.x);
//        int j = MathHelper.floor(this.y);
//        int k = MathHelper.floor(this.z);
//        buf2.writeCollection(this.affectedBlocks, (buf, pos) -> {
//            int l = pos.getX() - i;
//            int m = pos.getY() - j;
//            int n = pos.getZ() - k;
//            buf.writeByte(l);
//            buf.writeByte(m);
//            buf.writeByte(n);
//        });
//        buf2.writeFloat(this.playerVelocityX);
//        buf2.writeFloat(this.playerVelocityY);
//        buf2.writeFloat(this.playerVelocityZ);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public float getPlayerVelocityX() {
//        return this.playerVelocityX;
//    }
//
//    public float getPlayerVelocityY() {
//        return this.playerVelocityY;
//    }
//
//    public float getPlayerVelocityZ() {
//        return this.playerVelocityZ;
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
//    public float getRadius() {
//        return this.radius;
//    }
//
//    public List<BlockPos> getAffectedBlocks() {
//        return this.affectedBlocks;
//    }
//}

