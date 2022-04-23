package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class PlayerSpawnPositionS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final BlockPos pos;
    private final float angle;

    public PlayerSpawnPositionS2CPacket(BlockPos pos, float angle) {
        this.pos = pos;
        this.angle = angle;
    }

    public PlayerSpawnPositionS2CPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.angle = buf.readFloat();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeFloat(this.angle);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public float getAngle() {
        return this.angle;
    }
}

