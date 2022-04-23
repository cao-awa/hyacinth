package com.github.cao.awa.hyacinth.server.permission.whitelist;

import com.github.cao.awa.hyacinth.server.config.ServerConfigEntry;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class WhitelistEntry extends ServerConfigEntry<GameProfile> {
    public WhitelistEntry(GameProfile profile) {
        super(profile);
    }

    public WhitelistEntry(JsonObject json) {
        super(WhitelistEntry.profileFromJson(json));
    }

    @Override
    public void write(JsonObject json) {
        if (this.getKey() == null) {
            return;
        }
        json.addProperty("uuid", this.getKey().getId() == null ? "" : this.getKey().getId().toString());
        json.addProperty("name", this.getKey().getName());
    }

    private static GameProfile profileFromJson(JsonObject json) {
        UUID uUID;
        if (!json.has("uuid") || !json.has("name")) {
            return null;
        }
        String string = json.get("uuid").getAsString();
        try {
            uUID = UUID.fromString(string);
        } catch (Throwable throwable) {
            return null;
        }
        return new GameProfile(uUID, json.get("name").getAsString());
    }
}

