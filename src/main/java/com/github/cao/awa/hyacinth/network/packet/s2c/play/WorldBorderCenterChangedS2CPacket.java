//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class WorldBorderCenterChangedS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final double centerX;
//    private final double centerZ;
//
//    public WorldBorderCenterChangedS2CPacket(WorldBorder worldBorder) {
//        this.centerX = worldBorder.getCenterX();
//        this.centerZ = worldBorder.getCenterZ();
//    }
//
//    public WorldBorderCenterChangedS2CPacket(PacketByteBuf buf) {
//        this.centerX = buf.readDouble();
//        this.centerZ = buf.readDouble();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeDouble(this.centerX);
//        buf.writeDouble(this.centerZ);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public double getCenterZ() {
//        return this.centerZ;
//    }
//
//    public double getCenterX() {
//        return this.centerX;
//    }
//}
//
