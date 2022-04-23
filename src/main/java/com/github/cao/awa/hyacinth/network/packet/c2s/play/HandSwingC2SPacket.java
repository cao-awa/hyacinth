package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class HandSwingC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final Hand hand;
//
//    public HandSwingC2SPacket(Hand hand) {
//        this.hand = hand;
//    }
//
//    public HandSwingC2SPacket(PacketByteBuf buf) {
//        this.hand = buf.readEnumConstant(Hand.class);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeEnumConstant(this.hand);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onHandSwing(this);
//    }
//
//    public Hand getHand() {
//        return this.hand;
//    }
//}
//
