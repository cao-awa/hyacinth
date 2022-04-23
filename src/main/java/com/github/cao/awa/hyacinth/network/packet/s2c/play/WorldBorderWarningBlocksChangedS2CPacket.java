//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class WorldBorderWarningBlocksChangedS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int warningBlocks;
//
//    public WorldBorderWarningBlocksChangedS2CPacket(WorldBorder worldBorder) {
//        this.warningBlocks = worldBorder.getWarningBlocks();
//    }
//
//    public WorldBorderWarningBlocksChangedS2CPacket(PacketByteBuf buf) {
//        this.warningBlocks = buf.readVarInt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.warningBlocks);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public int getWarningBlocks() {
//        return this.warningBlocks;
//    }
//}
//
