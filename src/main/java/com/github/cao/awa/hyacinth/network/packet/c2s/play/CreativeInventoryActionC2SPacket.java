package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class CreativeInventoryActionC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final int slot;
//    private final ItemStack stack;
//
//    public CreativeInventoryActionC2SPacket(int slot, ItemStack stack) {
//        this.slot = slot;
//        this.stack = stack.copy();
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onCreativeInventoryAction(this);
//    }
//
//    public CreativeInventoryActionC2SPacket(PacketByteBuf buf) {
//        this.slot = buf.readShort();
//        this.stack = buf.readItemStack();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeShort(this.slot);
//        buf.writeItemStack(this.stack);
//    }
//
//    public int getSlot() {
//        return this.slot;
//    }
//
//    public ItemStack getItemStack() {
//        return this.stack;
//    }
//}
//