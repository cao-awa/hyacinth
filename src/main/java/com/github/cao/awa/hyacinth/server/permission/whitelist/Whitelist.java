package com.github.cao.awa.hyacinth.server.permission.whitelist;

import com.github.cao.awa.hyacinth.server.config.ServerConfigEntry;
import com.github.cao.awa.hyacinth.server.config.ServerConfigList;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import java.io.File;
import java.util.Objects;

public class Whitelist
        extends ServerConfigList<GameProfile, WhitelistEntry> {
    public Whitelist(File file) {
        super(file);
    }

    @Override
    public ServerConfigEntry<GameProfile> fromJson(JsonObject json) {
        return new WhitelistEntry(json);
    }

    public boolean isAllowed(GameProfile profile) {
        return this.contains(profile);
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

