package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class UpdatePlayerAbilitiesC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private static final int FLYING_MASK = 2;
//    private final boolean flying;
//
//    public UpdatePlayerAbilitiesC2SPacket(PlayerAbilities abilities) {
//        this.flying = abilities.flying;
//    }
//
//    public UpdatePlayerAbilitiesC2SPacket(PacketByteBuf buf) {
//        byte b = buf.readByte();
//        this.flying = (b & 2) != 0;
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        int b = 0;
//        if (this.flying) {
//            b = (byte)(b | 2);
//        }
//        buf.writeByte(b);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onUpdatePlayerAbilities(this);
//    }
//
//    public boolean isFlying() {
//        return this.flying;
//    }
//}
//
