//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import com.github.cao.awa.hyacinth.server.dimension.DimensionType;
//import com.github.cao.awa.hyacinth.server.mode.GameMode;
//import com.github.cao.awa.hyacinth.server.world.World;
//import com.google.common.collect.Sets;
//import org.jetbrains.annotations.Nullable;
//
//import java.rmi.registry.Registry;
//import java.util.Set;
//
//public record GameJoinS2CPacket(int playerEntityId, boolean hardcore, GameMode gameMode, @Nullable GameMode previousGameMode, Set<World> dimensions, net.minecraft.util.registry.DynamicRegistryManager.Impl registryManager, DimensionType dimensionType, RegistryKey<World> dimensionId, long sha256Seed, int maxPlayers, int viewDistance, int simulationDistance, boolean reducedDebugInfo, boolean showDeathScreen, boolean debugWorld, boolean flatWorld) implements Packet<ClientPlayPacketListener>
//{
//    public GameJoinS2CPacket(PacketByteBuf buf) {
//        this(buf.readInt(), buf.readBoolean(), GameMode.byId(buf.readByte()), GameMode.getOrNull(buf.readByte()), buf.readCollection(Sets::newHashSetWithExpectedSize, b -> RegistryKey.of(Registry.WORLD_KEY, b.readIdentifier())), buf.decode(net.minecraft.util.registry.DynamicRegistryManager.Impl.CODEC), buf.decode(DimensionType.REGISTRY_CODEC).get(), RegistryKey.of(Registry.WORLD_KEY, buf.readIdentifier()), buf.readLong(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeInt(this.playerEntityId);
//        buf.writeBoolean(this.hardcore);
//        buf.writeByte(this.gameMode.getId());
//        buf.writeByte(GameMode.getId(this.previousGameMode));
//        buf.writeCollection(this.dimensions, (b, dimension) -> b.writeIdentifier(dimension.getIdentifier()));
//        buf.encode(net.minecraft.util.registry.DynamicRegistryManager.Impl.CODEC, this.registryManager);
//        buf.encode(DimensionType.REGISTRY_CODEC, () -> this.dimensionType);
//        buf.writeIdentifier(this.dimensionId.getValue());
//        buf.writeLong(this.sha256Seed);
//        buf.writeVarInt(this.maxPlayers);
//        buf.writeVarInt(this.viewDistance);
//        buf.writeVarInt(this.simulationDistance);
//        buf.writeBoolean(this.reducedDebugInfo);
//        buf.writeBoolean(this.showDeathScreen);
//        buf.writeBoolean(this.debugWorld);
//        buf.writeBoolean(this.flatWorld);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//}
//
