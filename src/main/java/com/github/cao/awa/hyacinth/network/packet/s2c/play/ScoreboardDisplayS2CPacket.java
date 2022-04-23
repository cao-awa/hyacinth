//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Objects;
//
//public class ScoreboardDisplayS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int slot;
//    private final String name;
//
//    public ScoreboardDisplayS2CPacket(int slot, @Nullable ScoreboardObjective objective) {
//        this.slot = slot;
//        this.name = objective == null ? "" : objective.getName();
//    }
//
//    public ScoreboardDisplayS2CPacket(PacketByteBuf buf) {
//        this.slot = buf.readByte();
//        this.name = buf.readString();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeByte(this.slot);
//        buf.writeString(this.name);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public int getSlot() {
//        return this.slot;
//    }
//
//    @Nullable
//    public String getName() {
//        return Objects.equals(this.name, "") ? null : this.name;
//    }
//}
//
