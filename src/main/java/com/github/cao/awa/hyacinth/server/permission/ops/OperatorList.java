package com.github.cao.awa.hyacinth.server.permission.ops;

import com.github.cao.awa.hyacinth.server.config.ServerConfigEntry;
import com.github.cao.awa.hyacinth.server.config.ServerConfigList;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import java.io.File;
import java.util.Objects;

public class OperatorList
        extends ServerConfigList<GameProfile, OperatorEntry> {
    public OperatorList(File file) {
        super(file);
    }

    @Override
    public ServerConfigEntry<GameProfile> fromJson(JsonObject json) {
        return new OperatorEntry(json);
    }

    @Override
    public String[] getNames() {
        return this.values().stream().map(ServerConfigEntry::getKey).filter(Objects::nonNull).map(GameProfile::getName).toArray(String[]::new);
    }

    public boolean canBypassPlayerLimit(GameProfile profile) {
        OperatorEntry operatorEntry = this.get(profile);
        if (operatorEntry != null) {
            return operatorEntry.canBypassPlayerLimit();
        }
        return false;
    }

    @Override
    public String toString(GameProfile gameProfile) {
        return gameProfile.getId().toString();
    }
}

