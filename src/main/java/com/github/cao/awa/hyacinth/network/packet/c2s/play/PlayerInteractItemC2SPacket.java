package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class PlayerInteractItemC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final Hand hand;
//
//    public PlayerInteractItemC2SPacket(Hand hand) {
//        this.hand = hand;
//    }
//
//    public PlayerInteractItemC2SPacket(PacketByteBuf buf) {
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
//        serverPlayPacketListener.onPlayerInteractItem(this);
//    }
//
//    public Hand getHand() {
//        return this.hand;
//    }
//}
//
