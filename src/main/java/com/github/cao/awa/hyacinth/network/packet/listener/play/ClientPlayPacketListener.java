package com.github.cao.awa.hyacinth.network.packet.listener.play;

import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;

/**
 * A client side packet listener where play stage packets from the server are processed.
 */
public interface ClientPlayPacketListener
        extends PacketListener {
    /**
     * Handles the spawning of non-living entities.
     */
    void onDisconnect(DisconnectS2CPacket var1);

    // TODO: 2022/4/22
//
//    public void onEntitySpawn(EntitySpawnS2CPacket var1);
//
//    public void onExperienceOrbSpawn(ExperienceOrbSpawnS2CPacket var1);
//
//    public void onVibration(VibrationS2CPacket var1);
//
//    public void onMobSpawn(MobSpawnS2CPacket var1);
//
//    public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket var1);
//
//    public void onPaintingSpawn(PaintingSpawnS2CPacket var1);
//
//    public void onPlayerSpawn(PlayerSpawnS2CPacket var1);
//
//    public void onEntityAnimation(EntityAnimationS2CPacket var1);
//
//    public void onStatistics(StatisticsS2CPacket var1);
//
//    public void onUnlockRecipes(UnlockRecipesS2CPacket var1);
//
//    public void onBlockBreakingProgress(BlockBreakingProgressS2CPacket var1);
//
//    public void onSignEditorOpen(SignEditorOpenS2CPacket var1);
//
//    public void onBlockEntityUpdate(BlockEntityUpdateS2CPacket var1);
//
//    public void onBlockEvent(BlockEventS2CPacket var1);
//
//    public void onBlockUpdate(BlockUpdateS2CPacket var1);
//
//    public void onGameMessage(GameMessageS2CPacket var1);
//
//    public void onChunkDeltaUpdate(ChunkDeltaUpdateS2CPacket var1);
//
//    public void onMapUpdate(MapUpdateS2CPacket var1);
//
//    public void onCloseScreen(CloseScreenS2CPacket var1);
//
//    public void onInventory(InventoryS2CPacket var1);
//
//    public void onOpenHorseScreen(OpenHorseScreenS2CPacket var1);
//
//    public void onScreenHandlerPropertyUpdate(ScreenHandlerPropertyUpdateS2CPacket var1);
//
//    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket var1);
//
//    public void onCustomPayload(CustomPayloadS2CPacket var1);
//
//    public void onEntityStatus(EntityStatusS2CPacket var1);
//
//    public void onEntityAttach(EntityAttachS2CPacket var1);
//
//    public void onEntityPassengersSet(EntityPassengersSetS2CPacket var1);
//
//    public void onExplosion(ExplosionS2CPacket var1);
//
//    public void onGameStateChange(GameStateChangeS2CPacket var1);
//
//    public void onKeepAlive(KeepAliveS2CPacket var1);
//
//    public void onChunkData(ChunkDataS2CPacket var1);
//
//    public void onUnloadChunk(UnloadChunkS2CPacket var1);
//
//    public void onWorldEvent(WorldEventS2CPacket var1);
//
//    public void onGameJoin(GameJoinS2CPacket var1);
//
//    public void onEntity(EntityS2CPacket var1);
//
//    public void onPlayerPositionLook(PlayerPositionLookS2CPacket var1);
//
//    public void onParticle(ParticleS2CPacket var1);
//
//    public void onPing(PlayPingS2CPacket var1);
//
//    public void onPlayerAbilities(PlayerAbilitiesS2CPacket var1);
//
//    public void onPlayerList(PlayerListS2CPacket var1);
//
//    public void onEntitiesDestroy(EntitiesDestroyS2CPacket var1);
//
//    public void onRemoveEntityStatusEffect(RemoveEntityStatusEffectS2CPacket var1);
//
//    public void onPlayerRespawn(PlayerRespawnS2CPacket var1);
//
//    public void onEntitySetHeadYaw(EntitySetHeadYawS2CPacket var1);
//
//    public void onUpdateSelectedSlot(UpdateSelectedSlotS2CPacket var1);
//
//    public void onScoreboardDisplay(ScoreboardDisplayS2CPacket var1);
//
//    public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket var1);
//
//    public void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket var1);
//
//    public void onEntityEquipmentUpdate(EntityEquipmentUpdateS2CPacket var1);
//
//    public void onExperienceBarUpdate(ExperienceBarUpdateS2CPacket var1);
//
//    public void onHealthUpdate(HealthUpdateS2CPacket var1);
//
//    public void onTeam(TeamS2CPacket var1);
//
//    public void onScoreboardPlayerUpdate(ScoreboardPlayerUpdateS2CPacket var1);
//
//    public void onPlayerSpawnPosition(PlayerSpawnPositionS2CPacket var1);
//
//    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket var1);
//
//    public void onPlaySound(PlaySoundS2CPacket var1);
//
//    public void onPlaySoundFromEntity(PlaySoundFromEntityS2CPacket var1);
//
//    public void onPlaySoundId(PlaySoundIdS2CPacket var1);
//
//    public void onItemPickupAnimation(ItemPickupAnimationS2CPacket var1);
//
//    public void onEntityPosition(EntityPositionS2CPacket var1);
//
//    public void onEntityAttributes(EntityAttributesS2CPacket var1);
//
//    public void onEntityStatusEffect(EntityStatusEffectS2CPacket var1);
//
//    public void onSynchronizeTags(SynchronizeTagsS2CPacket var1);
//
//    public void onEndCombat(EndCombatS2CPacket var1);
//
//    public void onEnterCombat(EnterCombatS2CPacket var1);
//
//    public void onDeathMessage(DeathMessageS2CPacket var1);
//
//    public void onDifficulty(DifficultyS2CPacket var1);
//
//    public void onSetCameraEntity(SetCameraEntityS2CPacket var1);
//
//    public void onWorldBorderInitialize(WorldBorderInitializeS2CPacket var1);
//
//    public void onWorldBorderInterpolateSize(WorldBorderInterpolateSizeS2CPacket var1);
//
//    public void onWorldBorderSizeChanged(WorldBorderSizeChangedS2CPacket var1);
//
//    public void onWorldBorderWarningTimeChanged(WorldBorderWarningTimeChangedS2CPacket var1);
//
//    public void onWorldBorderWarningBlocksChanged(WorldBorderWarningBlocksChangedS2CPacket var1);
//
//    public void onWorldBorderCenterChanged(WorldBorderCenterChangedS2CPacket var1);
//
//    public void onPlayerListHeader(PlayerListHeaderS2CPacket var1);
//
//    public void onResourcePackSend(ResourcePackSendS2CPacket var1);
//
//    public void onBossBar(BossBarS2CPacket var1);
//
//    public void onCooldownUpdate(CooldownUpdateS2CPacket var1);
//
//    public void onVehicleMove(VehicleMoveS2CPacket var1);
//
//    public void onAdvancements(AdvancementUpdateS2CPacket var1);
//
//    public void onSelectAdvancementTab(SelectAdvancementTabS2CPacket var1);
//
//    public void onCraftFailedResponse(CraftFailedResponseS2CPacket var1);
//
//    public void onCommandTree(CommandTreeS2CPacket var1);
//
//    public void onStopSound(StopSoundS2CPacket var1);
//
//    public void onCommandSuggestions(CommandSuggestionsS2CPacket var1);
//
//    public void onSynchronizeRecipes(SynchronizeRecipesS2CPacket var1);
//
//    public void onLookAt(LookAtS2CPacket var1);
//
//    public void onNbtQueryResponse(NbtQueryResponseS2CPacket var1);
//
//    public void onLightUpdate(LightUpdateS2CPacket var1);
//
//    public void onOpenWrittenBook(OpenWrittenBookS2CPacket var1);
//
//    public void onOpenScreen(OpenScreenS2CPacket var1);
//
//    public void onSetTradeOffers(SetTradeOffersS2CPacket var1);
//
//    public void onChunkLoadDistance(ChunkLoadDistanceS2CPacket var1);
//
//    public void onSimulationDistance(SimulationDistanceS2CPacket var1);
//
//    public void onChunkRenderDistanceCenter(ChunkRenderDistanceCenterS2CPacket var1);
//
//    public void onPlayerActionResponse(PlayerActionResponseS2CPacket var1);
//
//    public void onOverlayMessage(OverlayMessageS2CPacket var1);
//
//    public void onSubtitle(SubtitleS2CPacket var1);
//
//    public void onTitle(TitleS2CPacket var1);
//
//    public void onTitleFade(TitleFadeS2CPacket var1);
//
//    public void onTitleClear(ClearTitleS2CPacket var1);
}

