package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class DifficultyS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Difficulty difficulty;
//    private final boolean difficultyLocked;
//
//    public DifficultyS2CPacket(Difficulty difficulty, boolean difficultyLocked) {
//        this.difficulty = difficulty;
//        this.difficultyLocked = difficultyLocked;
//    }
//
//    public DifficultyS2CPacket(PacketByteBuf buf) {
//        this.difficulty = Difficulty.byOrdinal(buf.readUnsignedByte());
//        this.difficultyLocked = buf.readBoolean();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeByte(this.difficulty.getId());
//        buf.writeBoolean(this.difficultyLocked);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public boolean isDifficultyLocked() {
//        return this.difficultyLocked;
//    }
//
//    public Difficulty getDifficulty() {
//        return this.difficulty;
//    }
//}
//
