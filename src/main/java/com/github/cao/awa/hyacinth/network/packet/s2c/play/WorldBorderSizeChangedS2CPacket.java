//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class WorldBorderSizeChangedS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final double sizeLerpTarget;
//
//    public WorldBorderSizeChangedS2CPacket(WorldBorder worldBorder) {
//        this.sizeLerpTarget = worldBorder.getSizeLerpTarget();
//    }
//
//    public WorldBorderSizeChangedS2CPacket(PacketByteBuf buf) {
//        this.sizeLerpTarget = buf.readDouble();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeDouble(this.sizeLerpTarget);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public double getSizeLerpTarget() {
//        return this.sizeLerpTarget;
//    }
//}
//
