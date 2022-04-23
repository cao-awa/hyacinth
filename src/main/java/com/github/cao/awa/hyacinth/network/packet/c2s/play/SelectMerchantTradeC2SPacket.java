package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class SelectMerchantTradeC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final int tradeId;

    public SelectMerchantTradeC2SPacket(int tradeId) {
        this.tradeId = tradeId;
    }

    public SelectMerchantTradeC2SPacket(PacketByteBuf buf) {
        this.tradeId = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.tradeId);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onSelectMerchantTrade(this);
    }

    public int getTradeId() {
        return this.tradeId;
    }
}

