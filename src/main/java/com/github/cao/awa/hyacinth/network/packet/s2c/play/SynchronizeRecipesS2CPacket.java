//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import com.google.common.collect.Lists;
//import net.minecraft.util.identifier.Identifier;
//
//import java.rmi.registry.Registry;
//
//public class SynchronizeRecipesS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final List<Recipe<?>> recipes;
//
//    public SynchronizeRecipesS2CPacket(Collection<Recipe<?>> recipes) {
//        this.recipes = Lists.newArrayList(recipes);
//    }
//
//    public SynchronizeRecipesS2CPacket(PacketByteBuf buf) {
//        this.recipes = buf.readList(SynchronizeRecipesS2CPacket::readRecipe);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeCollection(this.recipes, SynchronizeRecipesS2CPacket::writeRecipe);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public List<Recipe<?>> getRecipes() {
//        return this.recipes;
//    }
//
//    public static Recipe<?> readRecipe(PacketByteBuf buf) {
//        Identifier identifier = buf.readIdentifier();
//        Identifier identifier2 = buf.readIdentifier();
//        return Registry.RECIPE_SERIALIZER.getOrEmpty(identifier).orElseThrow(() -> new IllegalArgumentException("Unknown recipe serializer " + identifier)).read(identifier2, buf);
//    }
//
//    public static <T extends Recipe<?>> void writeRecipe(PacketByteBuf buf, T recipe) {
//        buf.writeIdentifier(Registry.RECIPE_SERIALIZER.getId(recipe.getSerializer()));
//        buf.writeIdentifier(recipe.getId());
//        recipe.getSerializer().write(buf, recipe);
//    }
//}
//
