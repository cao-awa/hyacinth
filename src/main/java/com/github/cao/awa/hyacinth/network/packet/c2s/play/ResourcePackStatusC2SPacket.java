package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class ResourcePackStatusC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final Status status;
//
//    public ResourcePackStatusC2SPacket(Status status) {
//        this.status = status;
//    }
//
//    public ResourcePackStatusC2SPacket(PacketByteBuf buf) {
//        this.status = buf.readEnumConstant(Status.class);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeEnumConstant(this.status);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onResourcePackStatus(this);
//    }
//
//    public Status getStatus() {
//        return this.status;
//    }
//
//    public static enum Status {
//        SUCCESSFULLY_LOADED,
//        DECLINED,
//        FAILED_DOWNLOAD,
//        ACCEPTED;
//
//    }
//}
//
