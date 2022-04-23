package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import net.minecraft.util.identifier.Identifier;

//public class CraftFailedResponseS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int syncId;
//    private final Identifier recipeId;
//
//    public CraftFailedResponseS2CPacket(int syncId, Recipe<?> recipe) {
//        this.syncId = syncId;
//        this.recipeId = recipe.getId();
//    }
//
//    public CraftFailedResponseS2CPacket(PacketByteBuf buf) {
//        this.syncId = buf.readByte();
//        this.recipeId = buf.readIdentifier();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeByte(this.syncId);
//        buf.writeIdentifier(this.recipeId);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public Identifier getRecipeId() {
//        return this.recipeId;
//    }
//
//    public int getSyncId() {
//        return this.syncId;
//    }
//}
//
