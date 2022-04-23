//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class WorldBorderInterpolateSizeS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final double size;
//    private final double sizeLerpTarget;
//    private final long sizeLerpTime;
//
//    public WorldBorderInterpolateSizeS2CPacket(WorldBorder worldBorder) {
//        this.size = worldBorder.getSize();
//        this.sizeLerpTarget = worldBorder.getSizeLerpTarget();
//        this.sizeLerpTime = worldBorder.getSizeLerpTime();
//    }
//
//    public WorldBorderInterpolateSizeS2CPacket(PacketByteBuf buf) {
//        this.size = buf.readDouble();
//        this.sizeLerpTarget = buf.readDouble();
//        this.sizeLerpTime = buf.readVarLong();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeDouble(this.size);
//        buf.writeDouble(this.sizeLerpTarget);
//        buf.writeVarLong(this.sizeLerpTime);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public double getSize() {
//        return this.size;
//    }
//
//    public double getSizeLerpTarget() {
//        return this.sizeLerpTarget;
//    }
//
//    public long getSizeLerpTime() {
//        return this.sizeLerpTime;
//    }
//}
//
