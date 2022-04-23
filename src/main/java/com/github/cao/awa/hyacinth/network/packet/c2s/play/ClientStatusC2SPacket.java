package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class ClientStatusC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final Mode mode;
//
//    public ClientStatusC2SPacket(Mode mode) {
//        this.mode = mode;
//    }
//
//    public ClientStatusC2SPacket(PacketByteBuf buf) {
//        this.mode = buf.readEnumConstant(Mode.class);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeEnumConstant(this.mode);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onClientStatus(this);
//    }
//
//    public Mode getMode() {
//        return this.mode;
//    }
//
//    public static enum Mode {
//        PERFORM_RESPAWN,
//        REQUEST_STATS;
//
//    }
//}
//
