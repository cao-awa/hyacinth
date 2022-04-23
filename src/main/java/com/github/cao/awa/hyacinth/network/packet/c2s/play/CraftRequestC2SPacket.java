package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import net.minecraft.util.identifier.Identifier;

//public class CraftRequestC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final int syncId;
//    private final Identifier recipe;
//    private final boolean craftAll;
//
//    public CraftRequestC2SPacket(int syncId, Recipe<?> recipe, boolean craftAll) {
//        this.syncId = syncId;
//        this.recipe = recipe.getId();
//        this.craftAll = craftAll;
//    }
//
//    public CraftRequestC2SPacket(PacketByteBuf buf) {
//        this.syncId = buf.readByte();
//        this.recipe = buf.readIdentifier();
//        this.craftAll = buf.readBoolean();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeByte(this.syncId);
//        buf.writeIdentifier(this.recipe);
//        buf.writeBoolean(this.craftAll);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onCraftRequest(this);
//    }
//
//    public int getSyncId() {
//        return this.syncId;
//    }
//
//    public Identifier getRecipe() {
//        return this.recipe;
//    }
//
//    public boolean shouldCraftAll() {
//        return this.craftAll;
//    }
//}

