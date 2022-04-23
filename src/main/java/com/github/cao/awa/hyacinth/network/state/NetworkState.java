package com.github.cao.awa.hyacinth.network.state;

import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.query.QueryPongS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.c2s.handshake.HandshakeC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginHelloC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginKeyC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.query.QueryPingC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.query.QueryRequestC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.listener.ClientLoginPacketListener;
import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;
import com.github.cao.awa.hyacinth.network.packet.listener.ServerHandshakePacketListener;
import com.github.cao.awa.hyacinth.network.packet.listener.ServerLoginPacketListener;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ClientQueryPacketListener;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ServerQueryPacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.query.QueryResponseS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.login.*;
import com.github.zhuaidadaya.rikaishinikui.handler.entrust.EntrustParser;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public enum NetworkState {
    HANDSHAKING(-1, NetworkState.createPacketHandlerInitializer().setup(
            NetworkSide.SERVERBOUND,
            new PacketHandler<ServerHandshakePacketListener>()
.register(HandshakeC2SPacket.class, HandshakeC2SPacket::new)
    )
    ),
    PLAY(0, NetworkState.createPacketHandlerInitializer().setup(NetworkSide.CLIENTBOUND,
            new PacketHandler<ClientPlayPacketListener>()
//                    .register(EntitySpawnS2CPacket.class, EntitySpawnS2CPacket::new)
//                    .register(ExperienceOrbSpawnS2CPacket.class, ExperienceOrbSpawnS2CPacket::new)
//                    .register(MobSpawnS2CPacket.class, MobSpawnS2CPacket::new)
//                    .register(PaintingSpawnS2CPacket.class, PaintingSpawnS2CPacket::new)
//.register(PlayerSpawnS2CPacket.class, PlayerSpawnS2CPacket::new)
//.register(VibrationS2CPacket.class, VibrationS2CPacket::new)
//.register(EntityAnimationS2CPacket.class, EntityAnimationS2CPacket::new)
//.register(StatisticsS2CPacket.class, StatisticsS2CPacket::new)
//.register(PlayerActionResponseS2CPacket.class, PlayerActionResponseS2CPacket::new)
//.register(BlockBreakingProgressS2CPacket.class, BlockBreakingProgressS2CPacket::new)
//.register(BlockEntityUpdateS2CPacket.class, BlockEntityUpdateS2CPacket::new)
//.register(BlockEventS2CPacket.class, BlockEventS2CPacket::new)
//.register(BlockUpdateS2CPacket.class, BlockUpdateS2CPacket::new)
//.register(BossBarS2CPacket.class, BossBarS2CPacket::new)
//.register(DifficultyS2CPacket.class, DifficultyS2CPacket::new)
//.register(GameMessageS2CPacket.class, GameMessageS2CPacket::new)
//.register(ClearTitleS2CPacket.class, ClearTitleS2CPacket::new)
//.register(CommandSuggestionsS2CPacket.class, CommandSuggestionsS2CPacket::new)
//.register(CommandTreeS2CPacket.class, CommandTreeS2CPacket::new)
//.register(CloseScreenS2CPacket.class, CloseScreenS2CPacket::new)
//.register(InventoryS2CPacket.class, InventoryS2CPacket::new)
//.register(ScreenHandlerPropertyUpdateS2CPacket.class, ScreenHandlerPropertyUpdateS2CPacket::new)
//.register(ScreenHandlerSlotUpdateS2CPacket.class, ScreenHandlerSlotUpdateS2CPacket::new)
//.register(CooldownUpdateS2CPacket.class, CooldownUpdateS2CPacket::new)
//.register(CustomPayloadS2CPacket.class, CustomPayloadS2CPacket::new)
//.register(PlaySoundIdS2CPacket.class, PlaySoundIdS2CPacket::new)
.register(DisconnectS2CPacket.class, DisconnectS2CPacket::new)
//.register(EntityStatusS2CPacket.class, EntityStatusS2CPacket::new)
//.register(ExplosionS2CPacket.class, ExplosionS2CPacket::new)
//.register(UnloadChunkS2CPacket.class, UnloadChunkS2CPacket::new)
//.register(GameStateChangeS2CPacket.class, GameStateChangeS2CPacket::new)
//.register(OpenHorseScreenS2CPacket.class, OpenHorseScreenS2CPacket::new)
//.register(WorldBorderInitializeS2CPacket.class, WorldBorderInitializeS2CPacket::new)
//.register(KeepAliveS2CPacket.class, KeepAliveS2CPacket::new)
//.register(ChunkDataS2CPacket.class, ChunkDataS2CPacket::new)
//.register(WorldEventS2CPacket.class, WorldEventS2CPacket::new)
//.register(ParticleS2CPacket.class, ParticleS2CPacket::new)
//.register(LightUpdateS2CPacket.class, LightUpdateS2CPacket::new)
//.register(GameJoinS2CPacket.class, GameJoinS2CPacket::new)
//.register(MapUpdateS2CPacket.class, MapUpdateS2CPacket::new)
//.register(SetTradeOffersS2CPacket.class, SetTradeOffersS2CPacket::new)
//.register(EntityS2CPacket.MoveRelative.class, EntityS2CPacket.MoveRelative::read)
//.register(EntityS2CPacket.RotateAndMoveRelative.class, EntityS2CPacket.RotateAndMoveRelative::read)
//.register(EntityS2CPacket.Rotate.class, EntityS2CPacket.Rotate::read)
//.register(VehicleMoveS2CPacket.class, VehicleMoveS2CPacket::new)
//.register(OpenWrittenBookS2CPacket.class, OpenWrittenBookS2CPacket::new)
//.register(OpenScreenS2CPacket.class, OpenScreenS2CPacket::new)
//.register(SignEditorOpenS2CPacket.class, SignEditorOpenS2CPacket::new)
//.register(PlayPingS2CPacket.class, PlayPingS2CPacket::new)
//.register(CraftFailedResponseS2CPacket.class, CraftFailedResponseS2CPacket::new)
//.register(PlayerAbilitiesS2CPacket.class, PlayerAbilitiesS2CPacket::new)
//.register(EndCombatS2CPacket.class, EndCombatS2CPacket::new)
//.register(EnterCombatS2CPacket.class, EnterCombatS2CPacket::new)
//.register(DeathMessageS2CPacket.class, DeathMessageS2CPacket::new)
//.register(PlayerListS2CPacket.class, PlayerListS2CPacket::new)
//.register(LookAtS2CPacket.class, LookAtS2CPacket::new)
//.register(PlayerPositionLookS2CPacket.class, PlayerPositionLookS2CPacket::new)
//.register(UnlockRecipesS2CPacket.class, UnlockRecipesS2CPacket::new)
//.register(EntitiesDestroyS2CPacket.class, EntitiesDestroyS2CPacket::new)
//.register(RemoveEntityStatusEffectS2CPacket.class, RemoveEntityStatusEffectS2CPacket::new)
//.register(ResourcePackSendS2CPacket.class, ResourcePackSendS2CPacket::new)
//.register(PlayerRespawnS2CPacket.class, PlayerRespawnS2CPacket::new)
//.register(EntitySetHeadYawS2CPacket.class, EntitySetHeadYawS2CPacket::new)
//.register(ChunkDeltaUpdateS2CPacket.class, ChunkDeltaUpdateS2CPacket::new)
//.register(SelectAdvancementTabS2CPacket.class, SelectAdvancementTabS2CPacket::new)
//.register(OverlayMessageS2CPacket.class, OverlayMessageS2CPacket::new)
//.register(WorldBorderCenterChangedS2CPacket.class, WorldBorderCenterChangedS2CPacket::new)
//.register(WorldBorderInterpolateSizeS2CPacket.class, WorldBorderInterpolateSizeS2CPacket::new)
//.register(WorldBorderSizeChangedS2CPacket.class, WorldBorderSizeChangedS2CPacket::new)
//.register(WorldBorderWarningTimeChangedS2CPacket.class, WorldBorderWarningTimeChangedS2CPacket::new)
//.register(WorldBorderWarningBlocksChangedS2CPacket.class, WorldBorderWarningBlocksChangedS2CPacket::new)
//.register(SetCameraEntityS2CPacket.class, SetCameraEntityS2CPacket::new)
//.register(UpdateSelectedSlotS2CPacket.class, UpdateSelectedSlotS2CPacket::new)
//.register(ChunkRenderDistanceCenterS2CPacket.class, ChunkRenderDistanceCenterS2CPacket::new)
//.register(ChunkLoadDistanceS2CPacket.class, ChunkLoadDistanceS2CPacket::new)
//.register(PlayerSpawnPositionS2CPacket.class, PlayerSpawnPositionS2CPacket::new)
//.register(ScoreboardDisplayS2CPacket.class, ScoreboardDisplayS2CPacket::new)
//.register(EntityTrackerUpdateS2CPacket.class, EntityTrackerUpdateS2CPacket::new)
//.register(EntityAttachS2CPacket.class, EntityAttachS2CPacket::new)
//.register(EntityVelocityUpdateS2CPacket.class, EntityVelocityUpdateS2CPacket::new)
//.register(EntityEquipmentUpdateS2CPacket.class, EntityEquipmentUpdateS2CPacket::new)
//.register(ExperienceBarUpdateS2CPacket.class, ExperienceBarUpdateS2CPacket::new)
//.register(HealthUpdateS2CPacket.class, HealthUpdateS2CPacket::new)
//.register(ScoreboardObjectiveUpdateS2CPacket.class, ScoreboardObjectiveUpdateS2CPacket::new)
//.register(EntityPassengersSetS2CPacket.class, EntityPassengersSetS2CPacket::new)
//.register(TeamS2CPacket.class, TeamS2CPacket::new)
//.register(ScoreboardPlayerUpdateS2CPacket.class, ScoreboardPlayerUpdateS2CPacket::new)
//.register(SimulationDistanceS2CPacket.class, SimulationDistanceS2CPacket::new)
//.register(SubtitleS2CPacket.class, SubtitleS2CPacket::new)
//.register(WorldTimeUpdateS2CPacket.class, WorldTimeUpdateS2CPacket::new)
//.register(TitleS2CPacket.class, TitleS2CPacket::new)
//.register(TitleFadeS2CPacket.class, TitleFadeS2CPacket::new)
//.register(PlaySoundFromEntityS2CPacket.class, PlaySoundFromEntityS2CPacket::new)
//.register(PlaySoundS2CPacket.class, PlaySoundS2CPacket::new)
//.register(StopSoundS2CPacket.class, StopSoundS2CPacket::new)
//.register(PlayerListHeaderS2CPacket.class, PlayerListHeaderS2CPacket::new)
//.register(NbtQueryResponseS2CPacket.class, NbtQueryResponseS2CPacket::new)
//.register(ItemPickupAnimationS2CPacket.class, ItemPickupAnimationS2CPacket::new)
//.register(EntityPositionS2CPacket.class, EntityPositionS2CPacket::new)
//.register(AdvancementUpdateS2CPacket.class, AdvancementUpdateS2CPacket::new)
//.register(EntityAttributesS2CPacket.class, EntityAttributesS2CPacket::new)
//.register(EntityStatusEffectS2CPacket.class, EntityStatusEffectS2CPacket::new)
//.register(SynchronizeRecipesS2CPacket.class, SynchronizeRecipesS2CPacket::new)
//.register(SynchronizeTagsS2CPacket.class, SynchronizeTagsS2CPacket::new))
    ).setup(NetworkSide.SERVERBOUND, new PacketHandler<ServerPlayPacketListener>()))
//.register(TeleportConfirmC2SPacket.class, TeleportConfirmC2SPacket::new)
//.register(QueryBlockNbtC2SPacket.class, QueryBlockNbtC2SPacket::new)
//.register(UpdateDifficultyC2SPacket.class, UpdateDifficultyC2SPacket::new)
//.register(ChatMessageC2SPacket.class, ChatMessageC2SPacket::new)
//.register(ClientStatusC2SPacket.class, ClientStatusC2SPacket::new)
//.register(ClientSettingsC2SPacket.class, ClientSettingsC2SPacket::new)
//.register(RequestCommandCompletionsC2SPacket.class, RequestCommandCompletionsC2SPacket::new)
//.register(ButtonClickC2SPacket.class, ButtonClickC2SPacket::new)
//.register(ClickSlotC2SPacket.class, ClickSlotC2SPacket::new)
//.register(CloseHandledScreenC2SPacket.class, CloseHandledScreenC2SPacket::new)
//.register(CustomPayloadC2SPacket.class, CustomPayloadC2SPacket::new)
//.register(BookUpdateC2SPacket.class, BookUpdateC2SPacket::new)
//.register(QueryEntityNbtC2SPacket.class, QueryEntityNbtC2SPacket::new)
//.register(PlayerInteractEntityC2SPacket.class, PlayerInteractEntityC2SPacket::new)
//.register(JigsawGeneratingC2SPacket.class, JigsawGeneratingC2SPacket::new)
//.register(KeepAliveC2SPacket.class, KeepAliveC2SPacket::new)
//.register(UpdateDifficultyLockC2SPacket.class, UpdateDifficultyLockC2SPacket::new)
//.register(PlayerMoveC2SPacket.PositionAndOnGround.class, PlayerMoveC2SPacket.PositionAndOnGround::read)
//.register(PlayerMoveC2SPacket.Full.class, PlayerMoveC2SPacket.Full::read)
//.register(PlayerMoveC2SPacket.LookAndOnGround.class, PlayerMoveC2SPacket.LookAndOnGround::read)
//.register(PlayerMoveC2SPacket.OnGroundOnly.class, PlayerMoveC2SPacket.OnGroundOnly::read)
//.register(VehicleMoveC2SPacket.class, VehicleMoveC2SPacket::new)
//.register(BoatPaddleStateC2SPacket.class, BoatPaddleStateC2SPacket::new)
//.register(PickFromInventoryC2SPacket.class, PickFromInventoryC2SPacket::new)
//.register(CraftRequestC2SPacket.class, CraftRequestC2SPacket::new)
//.register(UpdatePlayerAbilitiesC2SPacket.class, UpdatePlayerAbilitiesC2SPacket::new)
//.register(PlayerActionC2SPacket.class, PlayerActionC2SPacket::new)
//.register(ClientCommandC2SPacket.class, ClientCommandC2SPacket::new)
//.register(PlayerInputC2SPacket.class, PlayerInputC2SPacket::new)
//.register(PlayPongC2SPacket.class, PlayPongC2SPacket::new)
//.register(RecipeCategoryOptionsC2SPacket.class, RecipeCategoryOptionsC2SPacket::new)
//.register(RecipeBookDataC2SPacket.class, RecipeBookDataC2SPacket::new)
//.register(RenameItemC2SPacket.class, RenameItemC2SPacket::new)
//.register(ResourcePackStatusC2SPacket.class, ResourcePackStatusC2SPacket::new)
//.register(AdvancementTabC2SPacket.class, AdvancementTabC2SPacket::new)
//.register(SelectMerchantTradeC2SPacket.class, SelectMerchantTradeC2SPacket::new)
//.register(UpdateBeaconC2SPacket.class, UpdateBeaconC2SPacket::new)
//.register(UpdateSelectedSlotC2SPacket.class, UpdateSelectedSlotC2SPacket::new)
//.register(UpdateCommandBlockC2SPacket.class, UpdateCommandBlockC2SPacket::new)
//.register(UpdateCommandBlockMinecartC2SPacket.class, UpdateCommandBlockMinecartC2SPacket::new)
//.register(CreativeInventoryActionC2SPacket.class, CreativeInventoryActionC2SPacket::new)
//.register(UpdateJigsawC2SPacket.class, UpdateJigsawC2SPacket::new)
//.register(UpdateStructureBlockC2SPacket.class, UpdateStructureBlockC2SPacket::new)
//.register(UpdateSignC2SPacket.class, UpdateSignC2SPacket::new)
//.register(HandSwingC2SPacket.class, HandSwingC2SPacket::new)
//.register(SpectatorTeleportC2SPacket.class, SpectatorTeleportC2SPacket::new)
//.register(PlayerInteractBlockC2SPacket.class, PlayerInteractBlockC2SPacket::new)
//.register(PlayerInteractItemC2SPacket.class, PlayerInteractItemC2SPacket::new)))
    ,
    STATUS(1,
            NetworkState.createPacketHandlerInitializer().setup(
                    NetworkSide.CLIENTBOUND, new PacketHandler<ClientQueryPacketListener>()
                            .register(QueryResponseS2CPacket.class, QueryResponseS2CPacket::new)
                            .register(QueryPongS2CPacket.class, QueryPongS2CPacket::new)).setup(
                    NetworkSide.SERVERBOUND, new PacketHandler<ServerQueryPacketListener>()
                            .register(QueryRequestC2SPacket.class, QueryRequestC2SPacket::new)
                            .register(QueryPingC2SPacket.class, QueryPingC2SPacket::new))),
    LOGIN(2,
            NetworkState.createPacketHandlerInitializer().setup(
                    NetworkSide.CLIENTBOUND,
                    new PacketHandler<ClientLoginPacketListener>()
                            .register(LoginDisconnectS2CPacket.class, LoginDisconnectS2CPacket::new)
                            .register(LoginHelloS2CPacket.class, LoginHelloS2CPacket::new)
                            .register(LoginSuccessS2CPacket.class, LoginSuccessS2CPacket::new)
                            .register(LoginCompressionS2CPacket.class, LoginCompressionS2CPacket::new)
                            .register(LoginQueryRequestS2CPacket.class, LoginQueryRequestS2CPacket::new)).setup(
                    NetworkSide.SERVERBOUND, new PacketHandler<ServerLoginPacketListener>()
                            .register(LoginHelloC2SPacket.class, LoginHelloC2SPacket::new)
                            .register(LoginKeyC2SPacket.class, LoginKeyC2SPacket::new)
                            .register(LoginQueryResponseC2SPacket.class, LoginQueryResponseC2SPacket::new)));

    private final int stateId;
    private final Map<NetworkSide, ? extends PacketHandler<?>> packetHandlers;
    private static final NetworkState[] STATES = new NetworkState[4];
    private static final Map<Class<? extends Packet<?>>, NetworkState> HANDLER_STATE_MAP = EntrustParser.operation(Maps.newHashMap(), map -> {
        for (NetworkState networkState : NetworkState.values()) {
            int i = networkState.getId();
            if (i < -1 || i > 2) {
                throw new Error("Invalid protocol ID " + i);
            }
            NetworkState.STATES[i - -1] = networkState;
            networkState.packetHandlers.forEach((side, handler) -> handler.getPacketTypes().forEach(packetClass -> {
                if (map.containsKey(packetClass) && map.get(packetClass) != networkState) {
                    throw new IllegalStateException("Packet " + packetClass + " is already assigned to protocol " + map.get(packetClass) + " - can't reassign to " + networkState);
                }
                map.put(packetClass, networkState);
            }));
        }
    });

    NetworkState(int id, PacketHandlerInitializer initializer) {
        this.stateId = id;
        this.packetHandlers = initializer.packetHandlers;
    }

    private static PacketHandlerInitializer createPacketHandlerInitializer() {
        return new PacketHandlerInitializer();
    }

    @Nullable
    public Packet<?> getPacketHandler(NetworkSide side, int packetId, PacketByteBuf buf) {
        return this.packetHandlers.get(side).createPacket(packetId, buf);
    }

    @Nullable
    public Integer getPacketId(NetworkSide side, Packet<?> packet) {
        return this.packetHandlers.get(side).getId(packet.getClass());
    }

    public static NetworkState getPacketHandlerState(Packet<?> handler) {
        return HANDLER_STATE_MAP.get(handler.getClass());
    }

    public static NetworkState byId(int id) {
        if (id < -1 || id > 2) {
            return null;
        }
        return STATES[id - -1];
    }

    public int getId() {
        return this.stateId;
    }

    static class PacketHandlerInitializer {
        final Map<NetworkSide, PacketHandler<?>> packetHandlers = Maps.newEnumMap(NetworkSide.class);

        PacketHandlerInitializer() {
        }

        public <T extends PacketListener> PacketHandlerInitializer setup(NetworkSide side, PacketHandler<T> handler) {
            this.packetHandlers.put(side, handler);
            return this;
        }
    }

    static class PacketHandler<T extends PacketListener> {
        final Object2IntMap<Class<? extends Packet<T>>> packetIds = EntrustParser.operation(new Object2IntOpenHashMap<>(), map -> map.defaultReturnValue(-1));
        private final List<Function<PacketByteBuf, ? extends Packet<T>>> packetFactories = Lists.newArrayList();

        PacketHandler() {
        }

        public <P extends Packet<T>> PacketHandler<T> register(Class<P> type, Function<PacketByteBuf, P> packetFactory) {
            int i = this.packetFactories.size();
            int j = this.packetIds.put(type, i);
            if (j != -1) {
                String string = "Packet " + type + " is already registered to ID " + j;
                LogManager.getLogger().fatal(string);
                throw new IllegalArgumentException(string);
            }
            this.packetFactories.add(packetFactory);
            return this;
        }

        @Nullable
        public Integer getId(Class<?> packet) {
            int i = this.packetIds.getInt(packet);
            return i == -1 ? null : i;
        }

        @Nullable
        public Packet<?> createPacket(int id, PacketByteBuf buf) {
            Function<PacketByteBuf, Packet<T>> function = (Function<PacketByteBuf, Packet<T>>) this.packetFactories.get(id);
            return function == null ? null : function.apply(buf);
        }

        public Iterable<Class<? extends Packet<?>>> getPacketTypes() {
            return Iterables.unmodifiableIterable(this.packetIds.keySet());
        }
    }
}
