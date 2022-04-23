package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

/**
 * Represents the contents of a block or entity inventory being synchronized
 * from the server to the client.
 */
//public class InventoryS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    /**
//     * The {@link net.minecraft.screen.ScreenHandler#syncId} of a screen handler.
//     */
//    private final int syncId;
//    private final int revision;
//    private final List<ItemStack> contents;
//    private final ItemStack cursorStack;
//
//    public InventoryS2CPacket(int syncId, int revision, DefaultedList<ItemStack> contents, ItemStack cursorStack) {
//        this.syncId = syncId;
//        this.revision = revision;
//        this.contents = DefaultedList.ofSize(contents.size(), ItemStack.EMPTY);
//        for (int i = 0; i < contents.size(); ++i) {
//            this.contents.set(i, contents.get(i).copy());
//        }
//        this.cursorStack = cursorStack.copy();
//    }
//
//    public InventoryS2CPacket(PacketByteBuf buf) {
//        this.syncId = buf.readUnsignedByte();
//        this.revision = buf.readVarInt();
//        this.contents = buf.readCollection(DefaultedList::ofSize, PacketByteBuf::readItemStack);
//        this.cursorStack = buf.readItemStack();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeByte(this.syncId);
//        buf.writeVarInt(this.revision);
//        buf.writeCollection(this.contents, PacketByteBuf::writeItemStack);
//        buf.writeItemStack(this.cursorStack);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getSyncId() {
//        return this.syncId;
//    }
//
//    public List<ItemStack> getContents() {
//        return this.contents;
//    }
//
//    public ItemStack getCursorStack() {
//        return this.cursorStack;
//    }
//
//    public int getRevision() {
//        return this.revision;
//    }
//}

