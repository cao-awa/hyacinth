//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class VibrationS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Vibration vibration;
//
//    public VibrationS2CPacket(Vibration vibration) {
//        this.vibration = vibration;
//    }
//
//    public VibrationS2CPacket(PacketByteBuf buf) {
//        this.vibration = Vibration.readFromBuf(buf);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        Vibration.writeToBuf(buf, this.vibration);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public Vibration getVibration() {
//        return this.vibration;
//    }
//}
//
