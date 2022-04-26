package com.github.cao.awa.hyacinth.server.entity.player;

import com.github.cao.awa.hyacinth.math.block.*;
import com.github.cao.awa.hyacinth.server.entity.*;
import com.github.cao.awa.hyacinth.server.world.*;
import com.mojang.authlib.GameProfile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class PlayerEntity extends LivingEntity {
    public static final String OFFLINE_PLAYER_UUID_PREFIX = "OfflinePlayer:";

    public PlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(EntityType.PLAYER, world);
        this.setUuid(PlayerEntity.getUuidFromProfile(profile));
//        this.gameProfile = profile;
//        this.playerScreenHandler = new PlayerScreenHandler(this.inventory, !world.isClient, this);
//        this.currentScreenHandler = this.playerScreenHandler;
//        this.refreshPositionAndAngles((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5, yaw, 0.0f);
//        this.field_6215 = 180.0f;
    }

    public static UUID getOfflinePlayerUuid(String nickname) {
        return UUID.nameUUIDFromBytes((OFFLINE_PLAYER_UUID_PREFIX + nickname).getBytes(StandardCharsets.UTF_8));
    }

    public static UUID getUuidFromProfile(GameProfile profile) {
        UUID uuid = profile.getId();
        if (uuid == null) {
            uuid = PlayerEntity.getOfflinePlayerUuid(profile.getName());
        }
        return uuid;
    }

    public boolean isPlayer() {
        return true;
    }
}
