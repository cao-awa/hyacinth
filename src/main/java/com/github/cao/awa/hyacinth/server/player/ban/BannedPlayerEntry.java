package com.github.cao.awa.hyacinth.server.player.ban;

import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BannedPlayerEntry
        extends BanEntry<GameProfile> {
    public BannedPlayerEntry(GameProfile profile) {
        this(profile, null, null, null, null);
    }

    public BannedPlayerEntry(GameProfile profile, @Nullable Date created, @Nullable String source, @Nullable Date expiry, @Nullable String reason) {
        super(profile, created, source, expiry, reason);
    }

    public BannedPlayerEntry(JsonObject json) {
        super(BannedPlayerEntry.profileFromJson(json), json);
    }

    @Override
    public void write(JsonObject json) {
        if (this.getKey() == null) {
            return;
        }
        json.addProperty("uuid", this.getKey().getId() == null ? "" : this.getKey().getId().toString());
        json.addProperty("name", this.getKey().getName());
        super.write(json);
    }

    @Override
    public Text toText() {
        GameProfile gameProfile = this.getKey();
        return new LiteralText(gameProfile.getName() != null ? gameProfile.getName() : Objects.toString(gameProfile.getId(), "(Unknown)"));
    }

    private static GameProfile profileFromJson(JsonObject json) {
        UUID uUID;
        if (!json.has("uuid") || !json.has("name")) {
            return null;
        }
        String string = json.get("uuid").getAsString();
        try {
            uUID = UUID.fromString(string);
        }
        catch (Throwable throwable) {
            return null;
        }
        return new GameProfile(uUID, json.get("name").getAsString());
    }
}

