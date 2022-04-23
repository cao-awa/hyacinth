package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class UpdateDifficultyC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final Difficulty difficulty;
//
//    public UpdateDifficultyC2SPacket(Difficulty difficulty) {
//        this.difficulty = difficulty;
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onUpdateDifficulty(this);
//    }
//
//    public UpdateDifficultyC2SPacket(PacketByteBuf buf) {
//        this.difficulty = Difficulty.byOrdinal(buf.readUnsignedByte());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeByte(this.difficulty.getId());
//    }
//
//    public Difficulty getDifficulty() {
//        return this.difficulty;
//    }
//}
//
