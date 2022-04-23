package com.github.cao.awa.hyacinth.network.packet.listener.play;

import com.github.cao.awa.hyacinth.network.packet.c2s.play.*;
import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;

/**
 * A server side packet listener where play stage packets from a client are processed.
 */
public interface ServerPlayPacketListener extends PacketListener {
//    void onHandSwing(HandSwingC2SPacket var1);

    void onChatMessage(ChatMessageC2SPacket var1);

//    void onClientStatus(ClientStatusC2SPacket var1);

//    void onClientSettings(ClientSettingsC2SPacket var1);

    void onButtonClick(ButtonClickC2SPacket var1);

//    void onClickSlot(ClickSlotC2SPacket var1);

//    void onCraftRequest(CraftRequestC2SPacket var1);

    void onCloseHandledScreen(CloseHandledScreenC2SPacket var1);

    void onCustomPayload(CustomPayloadC2SPacket var1);

//    void onPlayerInteractEntity(PlayerInteractEntityC2SPacket var1);

    void onKeepAlive(KeepAliveC2SPacket var1);

    void onPlayerMove(PlayerMoveC2SPacket var1);

    void onPong(PlayPongC2SPacket var1);

//    void onUpdatePlayerAbilities(UpdatePlayerAbilitiesC2SPacket var1);

//    void onPlayerAction(PlayerActionC2SPacket var1);

//    void onClientCommand(ClientCommandC2SPacket var1);

    void onPlayerInput(PlayerInputC2SPacket var1);

    void onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket var1);

//    void onCreativeInventoryAction(CreativeInventoryActionC2SPacket var1);

    void onUpdateSign(UpdateSignC2SPacket var1);

//    void onPlayerInteractBlock(PlayerInteractBlockC2SPacket var1);

//    void onPlayerInteractItem(PlayerInteractItemC2SPacket var1);

//    void onSpectatorTeleport(SpectatorTeleportC2SPacket var1);

//    void onResourcePackStatus(ResourcePackStatusC2SPacket var1);

    void onBoatPaddleState(BoatPaddleStateC2SPacket var1);

//    void onVehicleMove(VehicleMoveC2SPacket var1);

    void onTeleportConfirm(TeleportConfirmC2SPacket var1);

//    void onRecipeBookData(RecipeBookDataC2SPacket var1);

//    void onRecipeCategoryOptions(RecipeCategoryOptionsC2SPacket var1);

//    void onAdvancementTab(AdvancementTabC2SPacket var1);

    void onRequestCommandCompletions(RequestCommandCompletionsC2SPacket var1);

//    void onUpdateCommandBlock(UpdateCommandBlockC2SPacket var1);

//    void onUpdateCommandBlockMinecart(UpdateCommandBlockMinecartC2SPacket var1);

    void onPickFromInventory(PickFromInventoryC2SPacket var1);

    void onRenameItem(RenameItemC2SPacket var1);

    void onUpdateBeacon(UpdateBeaconC2SPacket var1);

//    void onUpdateStructureBlock(UpdateStructureBlockC2SPacket var1);

    void onSelectMerchantTrade(SelectMerchantTradeC2SPacket var1);

//    void onBookUpdate(BookUpdateC2SPacket var1);

    void onQueryEntityNbt(QueryEntityNbtC2SPacket var1);

    void onQueryBlockNbt(QueryBlockNbtC2SPacket var1);

//    void onUpdateJigsaw(UpdateJigsawC2SPacket var1);

    void onJigsawGenerating(JigsawGeneratingC2SPacket var1);

//    void onUpdateDifficulty(UpdateDifficultyC2SPacket var1);

    void onUpdateDifficultyLock(UpdateDifficultyLockC2SPacket var1);
}

