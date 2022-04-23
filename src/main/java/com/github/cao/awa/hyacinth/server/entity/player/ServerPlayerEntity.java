package com.github.cao.awa.hyacinth.server.entity.player;

import com.github.cao.awa.hyacinth.math.Mathematics;
import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.handler.play.ServerPlayNetworkHandler;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.world.World;
import com.mojang.authlib.GameProfile;

import java.util.Random;
import java.util.UUID;

public class ServerPlayerEntity extends PlayerEntity {
    protected final Random random = new Random();
    private UUID uuid = Mathematics.randomUuid(this.random);
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntity(MinecraftServer server, World world, GameProfile profile) {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
