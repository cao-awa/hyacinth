package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class CooldownUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Item item;
//    private final int cooldown;
//
//    public CooldownUpdateS2CPacket(Item item, int cooldown) {
//        this.item = item;
//        this.cooldown = cooldown;
//    }
//
//    public CooldownUpdateS2CPacket(PacketByteBuf buf) {
//        this.item = Item.byRawId(buf.readVarInt());
//        this.cooldown = buf.readVarInt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(Item.getRawId(this.item));
//        buf.writeVarInt(this.cooldown);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public Item getItem() {
//        return this.item;
//    }
//
//    public int getCooldown() {
//        return this.cooldown;
//    }
//}

