package com.github.cao.awa.hyacinth.network.handler.play;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.c2s.play.*;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.entity.player.ServerPlayerEntity;
import com.github.cao.awa.hyacinth.server.world.listener.EntityTrackingListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jetbrains.annotations.Nullable;

public class ServerPlayNetworkHandler implements EntityTrackingListener, ServerPlayPacketListener {
    private ClientConnection connection;
    private MinecraftServer server;

    public void tick() {
    }

    public void disconnect(Text reason) {
        this.connection.send(new DisconnectS2CPacket(reason), future -> this.connection.disconnect(reason));
        this.connection.disableAutoRead();
        this.server.submitAndJoin(this.connection::handleDisconnection);
    }

    @Override
    public ServerPlayerEntity getPlayer() {
        return null;
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        this.sendPacket(packet, null);
    }

    public void sendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> listener) {
        try {
            this.connection.send(packet, listener);
        } catch (Throwable throwable) {
//            CrashReport crashReport = CrashReport.create(throwable, "Sending packet");
//            CrashReportSection crashReportSection = crashReport.addElement("Packet being sent");
//            crashReportSection.add("Packet class", () -> packet.getClass().getCanonicalName());
//            throw new CrashException(crashReport);
        }
    }

    @Override
    public void onDisconnected(Text reason) {

    }

    @Override
    public ClientConnection getConnection() {
        return null;
    }

    @Override
    public void onChatMessage(ChatMessageC2SPacket var1) {

    }

    @Override
    public void onButtonClick(ButtonClickC2SPacket var1) {

    }

    @Override
    public void onCloseHandledScreen(CloseHandledScreenC2SPacket var1) {

    }

    @Override
    public void onCustomPayload(CustomPayloadC2SPacket var1) {

    }

    @Override
    public void onKeepAlive(KeepAliveC2SPacket var1) {

    }

    @Override
    public void onPlayerMove(PlayerMoveC2SPacket var1) {

    }

    @Override
    public void onPong(PlayPongC2SPacket var1) {

    }

    @Override
    public void onPlayerInput(PlayerInputC2SPacket var1) {

    }

    @Override
    public void onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket var1) {

    }

    @Override
    public void onUpdateSign(UpdateSignC2SPacket var1) {

    }

    @Override
    public void onBoatPaddleState(BoatPaddleStateC2SPacket var1) {

    }

    @Override
    public void onTeleportConfirm(TeleportConfirmC2SPacket var1) {

    }

    @Override
    public void onRequestCommandCompletions(RequestCommandCompletionsC2SPacket var1) {

    }

    @Override
    public void onPickFromInventory(PickFromInventoryC2SPacket var1) {

    }

    @Override
    public void onRenameItem(RenameItemC2SPacket var1) {

    }

    @Override
    public void onUpdateBeacon(UpdateBeaconC2SPacket var1) {

    }

    @Override
    public void onSelectMerchantTrade(SelectMerchantTradeC2SPacket var1) {

    }

    @Override
    public void onQueryEntityNbt(QueryEntityNbtC2SPacket var1) {

    }

    @Override
    public void onQueryBlockNbt(QueryBlockNbtC2SPacket var1) {

    }

    @Override
    public void onJigsawGenerating(JigsawGeneratingC2SPacket var1) {

    }

    @Override
    public void onUpdateDifficultyLock(UpdateDifficultyLockC2SPacket var1) {

    }
}
