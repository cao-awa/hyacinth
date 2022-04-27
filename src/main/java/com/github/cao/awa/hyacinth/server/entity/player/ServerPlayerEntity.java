package com.github.cao.awa.hyacinth.server.entity.player;

import com.github.cao.awa.hyacinth.math.Mathematics;
import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.handler.play.ServerPlayNetworkHandler;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.filter.*;
import com.github.cao.awa.hyacinth.server.world.*;
import com.github.zhuaidadaya.rikaishinikui.handler.times.*;
import com.mojang.authlib.GameProfile;

import java.util.Random;
import java.util.UUID;

public class ServerPlayerEntity extends PlayerEntity {
    protected final Random random = new Random();
    private MinecraftServer server;
    private UUID uuid = Mathematics.randomUuid(this.random);
    public ServerPlayNetworkHandler networkHandler;
    public int pingMilliseconds;
    private long lastActionTime = TimeUtil.measuringTimeMillions();
//    public final ServerPlayerInteractionManager interactionManager;
    private final TextStream textStream;

    public TextStream getTextStream() {
        return textStream;
    }

    public ServerPlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile) {
        super(world, world.getSpawnPos(), world.getSpawnAngle(), profile);
        this.textStream = server.createFilterer(this);
//        this.interactionManager = server.getPlayerInteractionManager(this);
        this.server = server;
//        this.statHandler = server.getPlayerManager().createStatHandler(this);
//        this.advancementTracker = server.getPlayerManager().getAdvancementTracker(this);
//        this.stepHeight = 1.0f;
//        this.moveToSpawn(world);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getLastActionTime() {
        return this.lastActionTime;
    }

    public void updateLastActionTime() {
        this.lastActionTime = TimeUtil.measuringTimeMillions();
    }
}
