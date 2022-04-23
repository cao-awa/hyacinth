package com.github.cao.awa.hyacinth.server.player.manager;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.handler.play.ServerPlayNetworkHandler;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.s2c.play.CustomPayloadS2CPacket;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.dimension.DimensionType;
import com.github.cao.awa.hyacinth.server.entity.player.PlayerEntity;
import com.github.cao.awa.hyacinth.server.entity.player.ServerPlayerEntity;
import com.github.cao.awa.hyacinth.server.mode.GameMode;
import com.github.cao.awa.hyacinth.server.permission.ops.OperatorList;
import com.github.cao.awa.hyacinth.server.permission.whitelist.Whitelist;
import com.github.cao.awa.hyacinth.server.player.ban.BannedIpEntry;
import com.github.cao.awa.hyacinth.server.player.ban.BannedIpList;
import com.github.cao.awa.hyacinth.server.player.ban.BannedPlayerEntry;
import com.github.cao.awa.hyacinth.server.player.ban.BannedPlayerList;
import com.github.cao.awa.hyacinth.server.world.ServerWorld;
import com.github.cao.awa.hyacinth.server.world.World;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.identifier.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    public static final File BANNED_PLAYERS_FILE = new File("banned-players.json");
    public static final File BANNED_IPS_FILE = new File("banned-ips.json");
    public static final File OPERATORS_FILE = new File("ops.json");
    public static final File WHITELIST_FILE = new File("whitelist.json");
    private static final Logger LOGGER = LogManager.getLogger("Manager:Player");
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private final BannedPlayerList bannedProfiles = new BannedPlayerList(BANNED_PLAYERS_FILE);
    private final BannedIpList bannedIps = new BannedIpList(BANNED_IPS_FILE);
    private final MinecraftServer server;
    private final List<ServerPlayerEntity> players = Lists.newArrayList();
    private final Map<UUID, ServerPlayerEntity> playerMap = Maps.newHashMap();
    private final OperatorList ops = new OperatorList(OPERATORS_FILE);
    private final Whitelist whitelist = new Whitelist(WHITELIST_FILE);
    private boolean whitelistEnabled;
    protected final int maxPlayers;
    private ServerPlayNetworkHandler serverPlayNetworkHandler;

    public PlayerManager(MinecraftServer server, int maxPlayers) {
        this.server = server;
        this.maxPlayers = maxPlayers;
    }

    @Nullable
    public Text checkCanJoin(SocketAddress address, GameProfile profile) {
        if (this.bannedProfiles.contains(profile)) {
            BannedPlayerEntry bannedPlayerEntry = this.bannedProfiles.get(profile);
            TranslatableText mutableText = new TranslatableText("multiplayer.disconnect.banned.reason", bannedPlayerEntry.getReason());
            if (bannedPlayerEntry.getExpiryDate() != null) {
                mutableText.append(new TranslatableText("multiplayer.disconnect.banned.expiration", DATE_FORMATTER.format(bannedPlayerEntry.getExpiryDate())));
            }
            return mutableText;
        }
        if (!this.isWhitelisted(profile)) {
            return new TranslatableText("multiplayer.disconnect.not_whitelisted");
        }
        if (this.bannedIps.isBanned(address)) {
            BannedIpEntry bannedPlayerEntry = this.bannedIps.get(address.toString());
            TranslatableText mutableText = new TranslatableText("multiplayer.disconnect.banned_ip.reason", bannedPlayerEntry.getReason());
            if (bannedPlayerEntry.getExpiryDate() != null) {
                mutableText.append(new TranslatableText("multiplayer.disconnect.banned_ip.expiration", DATE_FORMATTER.format(bannedPlayerEntry.getExpiryDate())));
            }
            return mutableText;
        }
        if (this.players.size() >= this.maxPlayers && !this.canBypassPlayerLimit(profile)) {
            return new TranslatableText("multiplayer.disconnect.server_full");
        }
        return null;
    }

    @Nullable
    public ServerPlayerEntity getPlayer(UUID uuid) {
        return this.playerMap.get(uuid);
    }

    public ServerPlayerEntity createPlayer(GameProfile profile) {
        UUID uUID = PlayerEntity.getUuidFromProfile(profile);
        ArrayList<ServerPlayerEntity> list = Lists.newArrayList();
        for(ServerPlayerEntity serverPlayerEntity : this.players) {
            if(! serverPlayerEntity.getUuid().equals(uUID))
                continue;
            list.add(serverPlayerEntity);
        }
        ServerPlayerEntity i = this.playerMap.get(profile.getId());
        if (i != null && !list.contains(i)) {
            list.add(i);
        }
        for (ServerPlayerEntity serverPlayerEntity2 : list) {
            serverPlayerEntity2.networkHandler.disconnect(new TranslatableText("multiplayer.disconnect.duplicate_login"));
        }
        return new ServerPlayerEntity(this.server, this.server.getOverworld(), profile);
    }

    public boolean canBypassPlayerLimit(GameProfile profile) {
        return false;
    }

    public boolean isWhitelisted(GameProfile profile) {
        return !this.whitelistEnabled || this.ops.contains(profile) || this.whitelist.contains(profile);
    }

    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player) {
//        NbtCompound nbtCompound = this.loadPlayerData(player);
//        Identifier registryKey = nbtCompound != null ? DimensionType.worldFromDimensionNbt(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.get("Dimension"))).resultOrPartial(LOGGER::error).orElse(World.OVERWORLD) : World.OVERWORLD;
//        ServerWorld serverWorld2;
//        ServerWorld serverWorld = this.server.getWorld(registryKey);
//        if (serverWorld == null) {
//            LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", registryKey);
//            serverWorld2 = this.server.getOverworld();
//        } else {
//            serverWorld2 = serverWorld;
//        }
//        WorldProperties worldProperties = serverWorld2.getLevelProperties();
//        serverPlayNetworkHandler.sendPacket(new GameJoinS2CPacket(player.getId(), false, GameMode.CREATIVE, GameMode.CREATIVE, this.server.getWorldRegistryKeys(), this.registryManager, world2.getDimension(), world2.getRegistryKey(), BiomeAccess.hashSeed(world2.getSeed()), this.getMaxPlayerCount(), this.viewDistance, this.simulationDistance, bl2, !bl, world2.isDebugWorld(), world2.isFlat()));
//        serverPlayNetworkHandler.sendPacket(new GameJoinS2CPacket(player.getId(), worldProperties.isHardcore(), player.interactionManager.getGameMode(), player.interactionManager.getPreviousGameMode(), this.server.getWorldRegistryKeys(), this.registryManager, world2.getDimension(), world2.getRegistryKey(), BiomeAccess.hashSeed(world2.getSeed()), this.getMaxPlayerCount(), this.viewDistance, this.simulationDistance, bl2, !bl, world2.isDebugWorld(), world2.isFlat()));
//        serverPlayNetworkHandler.sendPacket(new CustomPayloadS2CPacket(CustomPayloadS2CPacket.BRAND, new PacketByteBuf(Unpooled.buffer()).writeString(this.getServer().getServerModName())));
//        serverPlayNetworkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
//        serverPlayNetworkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));
//        serverPlayNetworkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(player.getInventory().selectedSlot));
//        serverPlayNetworkHandler.sendPacket(new SynchronizeRecipesS2CPacket(this.server.getRecipeManager().values()));
//        serverPlayNetworkHandler.sendPacket(new SynchronizeTagsS2CPacket(this.server.getTagManager().toPacket(this.registryManager)));
        System.out.println("connecting");
    }
}
