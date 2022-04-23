package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import net.minecraft.util.identifier.Identifier;

//public class RecipeBookDataC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final Identifier recipeId;
//
//    public RecipeBookDataC2SPacket(Recipe<?> recipe) {
//        this.recipeId = recipe.getId();
//    }
//
//    public RecipeBookDataC2SPacket(PacketByteBuf buf) {
//        this.recipeId = buf.readIdentifier();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeIdentifier(this.recipeId);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onRecipeBookData(this);
//    }
//
//    public Identifier getRecipeId() {
//        return this.recipeId;
//    }
//}
//
