package com.github.cao.awa.hyacinth.server.player.ban;

import com.github.cao.awa.hyacinth.server.config.ServerConfigEntry;
import com.github.cao.awa.hyacinth.server.config.ServerConfigList;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import java.io.File;
import java.util.Objects;

public class BannedPlayerList
        extends ServerConfigList<GameProfile, BannedPlayerEntry> {
    public BannedPlayerList(File file) {
        super(file);
    }

    @Override
    public ServerConfigEntry<GameProfile> fromJson(JsonObject json) {
        return new BannedPlayerEntry(json);
    }

    @Override
    public boolean contains(GameProfile profile) {
        return super.contains(profile);
    }

    @Override
    public String[] getNames() {
        return this.values().stream().map(ServerConfigEntry::getKey).filter(Objects::nonNull).map(GameProfile::getName).toArray(String[]::new);
    }

    @Override
    public String toString(GameProfile gameProfile) {
        return gameProfile.getId().toString();
    }
}

