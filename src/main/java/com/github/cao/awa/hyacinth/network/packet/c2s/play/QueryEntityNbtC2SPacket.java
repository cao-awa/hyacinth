package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class QueryEntityNbtC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int transactionId;
    private final int entityId;

    public QueryEntityNbtC2SPacket(int transactionId, int entityId) {
        this.transactionId = transactionId;
        this.entityId = entityId;
    }

    public QueryEntityNbtC2SPacket(PacketByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.entityId = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeVarInt(this.entityId);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onQueryEntityNbt(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public int getEntityId() {
        return this.entityId;
    }
}

