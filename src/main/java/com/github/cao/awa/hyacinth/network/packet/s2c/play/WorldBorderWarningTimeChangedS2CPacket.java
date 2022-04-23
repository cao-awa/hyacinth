//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class WorldBorderWarningTimeChangedS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int warningTime;
//
//    public WorldBorderWarningTimeChangedS2CPacket(WorldBorder worldBorder) {
//        this.warningTime = worldBorder.getWarningTime();
//    }
//
//    public WorldBorderWarningTimeChangedS2CPacket(PacketByteBuf buf) {
//        this.warningTime = buf.readVarInt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.warningTime);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public int getWarningTime() {
//        return this.warningTime;
//    }
//}
//
