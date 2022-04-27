package com.github.cao.awa.hyacinth.network.handler.play;

import com.github.cao.awa.hyacinth.math.vec.*;
import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.c2s.play.*;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.play.*;
import com.github.cao.awa.hyacinth.network.state.*;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.translate.*;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.entity.player.ServerPlayerEntity;
import com.github.cao.awa.hyacinth.server.world.listener.EntityTrackingListener;
import com.github.zhuaidadaya.rikaishinikui.handler.times.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerPlayNetworkHandler implements EntityTrackingListener, ServerPlayPacketListener {
    private static final Logger LOGGER = LogManager.getLogger("Handler:Play");
    private ClientConnection connection;
    private MinecraftServer server;
    private ServerPlayerEntity player;
    private boolean waitingForKeepAlive;
    private long keepAliveId;
    private long lastKeepAliveTime;
    private Vec3d requestedTeleportPos;
    private int requestedTeleportId;
    private int teleportRequestTick;
    private double lastTickX;
    private double lastTickY;
    private double lastTickZ;
    private double updatedX;
    private double updatedY;
    private double updatedZ;
    private int ticks;

    public ServerPlayNetworkHandler(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player) {
        this.server = server;
        this.connection = connection;
        connection.setPacketListener(this);
        this.player = player;
        player.networkHandler = this;
        player.getTextStream().onConnect();
    }

    public void tick() {
//        this.syncWithPlayerPosition();
        this.player.prevX = this.player.getX();
        this.player.prevY = this.player.getY();
        this.player.prevZ = this.player.getZ();
//        this.player.playerTick();
//        this.player.updatePositionAndAngles(this.lastTickX, this.lastTickY, this.lastTickZ, this.player.getYaw(), this.player.getPitch());
        ++ this.ticks;
//        this.lastTickMovePacketsCount = this.movePacketsCount;
//        if(this.floating && ! this.player.isSleeping()) {
//            if(++ this.floatingTicks > 80) {
//                LOGGER.warn("{} was kicked for floating too long!", this.player.getName().getString());
//                this.disconnect(new TranslatableText("multiplayer.disconnect.flying"));
//                return;
//            }
//        } else {
//            this.floating = false;
//            this.floatingTicks = 0;
//        }
//        this.topmostRiddenEntity = this.player.getRootVehicle();
//        if(this.topmostRiddenEntity == this.player || this.topmostRiddenEntity.getPrimaryPassenger() != this.player) {
//            this.topmostRiddenEntity = null;
//            this.vehicleFloating = false;
//            this.vehicleFloatingTicks = 0;
//        } else {
//            this.lastTickRiddenX = this.topmostRiddenEntity.getX();
//            this.lastTickRiddenY = this.topmostRiddenEntity.getY();
//            this.lastTickRiddenZ = this.topmostRiddenEntity.getZ();
//            this.updatedRiddenX = this.topmostRiddenEntity.getX();
//            this.updatedRiddenY = this.topmostRiddenEntity.getY();
//            this.updatedRiddenZ = this.topmostRiddenEntity.getZ();
//            if(this.vehicleFloating && this.player.getRootVehicle().getPrimaryPassenger() == this.player) {
//                if(++ this.vehicleFloatingTicks > 80) {
//                    LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getName().getString());
//                    this.disconnect(new TranslatableText("multiplayer.disconnect.flying"));
//                    return;
//                }
//            } else {
//                this.vehicleFloating = false;
//                this.vehicleFloatingTicks = 0;
//            }
//        }
        this.server.getProfiler().push("keepAlive");
        long l = TimeUtil.measuringTimeMillions();
        if(l - this.lastKeepAliveTime >= 15000L) {
            if(this.waitingForKeepAlive) {
                this.disconnect(new TranslatableText("disconnect.timeout"));
            } else {
                this.waitingForKeepAlive = true;
                this.lastKeepAliveTime = l;
                this.keepAliveId = l;
                this.sendPacket(new KeepAliveS2CPacket(this.keepAliveId));
            }
        }
        this.server.getProfiler().pop();
//        if(this.messageCooldown > 0) {
//            -- this.messageCooldown;
//        }
//        if(this.creativeItemDropThreshold > 0) {
//            -- this.creativeItemDropThreshold;
//        }
        if(this.player.getLastActionTime() > 0L && this.server.getPlayerIdleTimeout() > 0 && TimeUtil.measuringTimeMillions() - this.player.getLastActionTime() > (this.server.getPlayerIdleTimeout() * 1000L * 60L)) {
            this.disconnect(new TranslatableText("multiplayer.disconnect.idling"));
        }
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
            throwable.printStackTrace();
//            CrashReport crashReport = CrashReport.create(throwable, "Sending packet");
//            CrashReportSection crashReportSection = crashReport.addElement("Packet being sent");
//            crashReportSection.add("Packet class", () -> packet.getClass().getCanonicalName());
//            throw new CrashException(crashReport);
        }
    }

    @Override
    public void onDisconnected(Text reason) {
        LOGGER.info("{} lost connection: {}", this.getConnectionInfo(), reason.getString());
    }

    public String getConnectionInfo() {
        if (this.player.getGameProfile() != null) {
            return this.player.getGameProfile() + " (" + this.connection.getAddress() + ")";
        }
        return String.valueOf(this.connection.getAddress());
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
    public void onKeepAlive(KeepAliveC2SPacket packet) {
        if(this.waitingForKeepAlive && packet.getId() == this.keepAliveId) {
            int i = (int) (TimeUtil.measuringTimeMillions() - this.lastKeepAliveTime);
            this.player.pingMilliseconds = (this.player.pingMilliseconds * 3 + i) / 4;
            this.waitingForKeepAlive = false;
        } else {
            this.disconnect(new TranslatableText("disconnect.timeout"));
        }
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

    public void requestTeleportAndDismount(double x, double y, double z, float yaw, float pitch) {
        this.requestTeleport(x, y, z, yaw, pitch, Collections.emptySet(), true);
    }

    public void requestTeleport(double x, double y, double z, float yaw, float pitch) {
        this.requestTeleport(x, y, z, yaw, pitch, Collections.emptySet(), false);
    }

    public void requestTeleport(double x, double y, double z, float yaw, float pitch, Set<PlayerPositionLookS2CPacket.Flag> flags) {
        this.requestTeleport(x, y, z, yaw, pitch, flags, false);
    }

    public void requestTeleport(double x, double y, double z, float yaw, float pitch, Set<PlayerPositionLookS2CPacket.Flag> flags, boolean shouldDismount) {
        double d = flags.contains(PlayerPositionLookS2CPacket.Flag.X) ? this.player.getX() : 0.0;
        double e = flags.contains(PlayerPositionLookS2CPacket.Flag.Y) ? this.player.getY() : 0.0;
        double f = flags.contains(PlayerPositionLookS2CPacket.Flag.Z) ? this.player.getZ() : 0.0;
        float g = flags.contains(PlayerPositionLookS2CPacket.Flag.Y_ROT) ? this.player.getYaw() : 0.0f;
        float h = flags.contains(PlayerPositionLookS2CPacket.Flag.X_ROT) ? this.player.getPitch() : 0.0f;
        this.requestedTeleportPos = new Vec3d(x, y, z);
        if(++ this.requestedTeleportId == Integer.MAX_VALUE) {
            this.requestedTeleportId = 0;
        }
        this.teleportRequestTick = this.ticks;
//        this.player.updatePositionAndAngles(x, y, z, yaw, pitch);
        this.player.networkHandler.sendPacket(new PlayerPositionLookS2CPacket(x - d, y - e, z - f, yaw - g, pitch - h, flags, this.requestedTeleportId, shouldDismount));
    }
}
