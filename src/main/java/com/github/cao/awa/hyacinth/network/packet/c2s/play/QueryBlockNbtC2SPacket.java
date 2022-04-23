package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class QueryBlockNbtC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int transactionId;
    private final BlockPos pos;

    public QueryBlockNbtC2SPacket(int transactionId, BlockPos pos) {
        this.transactionId = transactionId;
        this.pos = pos;
    }

    public QueryBlockNbtC2SPacket(PacketByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onQueryBlockNbt(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

