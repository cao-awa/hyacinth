package com.github.cao.awa.hyacinth.server.entity.player;

import com.github.cao.awa.hyacinth.server.entity.LivingEntity;
import com.mojang.authlib.GameProfile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class PlayerEntity extends LivingEntity {
    public static final String OFFLINE_PLAYER_UUID_PREFIX = "OfflinePlayer:";

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

}
