package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class NbtQueryResponseS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int transactionId;
//    @Nullable
//    private final NbtCompound nbt;
//
//    public NbtQueryResponseS2CPacket(int transactionId, @Nullable NbtCompound nbt) {
//        this.transactionId = transactionId;
//        this.nbt = nbt;
//    }
//
//    public NbtQueryResponseS2CPacket(PacketByteBuf buf) {
//        this.transactionId = buf.readVarInt();
//        this.nbt = buf.readNbt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.transactionId);
//        buf.writeNbt(this.nbt);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getTransactionId() {
//        return this.transactionId;
//    }
//
//    @Nullable
//    public NbtCompound getNbt() {
//        return this.nbt;
//    }
//
//    @Override
//    public boolean isWritingErrorSkippable() {
//        return true;
//    }
//}

