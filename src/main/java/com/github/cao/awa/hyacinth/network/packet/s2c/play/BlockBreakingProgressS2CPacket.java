package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class BlockBreakingProgressS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int entityId;
    private final BlockPos pos;
    private final int progress;

    public BlockBreakingProgressS2CPacket(int entityId, BlockPos pos, int progress) {
        this.entityId = entityId;
        this.pos = pos;
        this.progress = progress;
    }

    public BlockBreakingProgressS2CPacket(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.pos = buf.readBlockPos();
        this.progress = buf.readUnsignedByte();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeBlockPos(this.pos);
        buf.writeByte(this.progress);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public int getEntityId() {
        return this.entityId;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getProgress() {
        return this.progress;
    }
}

