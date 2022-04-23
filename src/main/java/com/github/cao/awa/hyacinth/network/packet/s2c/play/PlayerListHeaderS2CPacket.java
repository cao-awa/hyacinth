package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class PlayerListHeaderS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Text header;
//    private final Text footer;
//
//    public PlayerListHeaderS2CPacket(Text header, Text footer) {
//        this.header = header;
//        this.footer = footer;
//    }
//
//    public PlayerListHeaderS2CPacket(PacketByteBuf buf) {
//        this.header = buf.readText();
//        this.footer = buf.readText();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeText(this.header);
//        buf.writeText(this.footer);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public Text getHeader() {
//        return this.header;
//    }
//
//    public Text getFooter() {
//        return this.footer;
//    }
//}
//
