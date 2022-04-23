package com.github.cao.awa.hyacinth.server.player.ban;

import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class BannedIpEntry
        extends BanEntry<String> {
    public BannedIpEntry(String ip) {
        this(ip, null, null, null, null);
    }

    public BannedIpEntry(String ip, @Nullable Date created, @Nullable String source, @Nullable Date expiry, @Nullable String reason) {
        super(ip, created, source, expiry, reason);
    }

    @Override
    public Text toText() {
        return new LiteralText(String.valueOf(this.getKey()));
    }

    public BannedIpEntry(JsonObject json) {
        super(BannedIpEntry.getIp(json), json);
    }

    private static String getIp(JsonObject json) {
        return json.has("ip") ? json.get("ip").getAsString() : null;
    }

    @Override
    public void write(JsonObject json) {
        if (this.getKey() == null) {
            return;
        }
        json.addProperty("ip", this.getKey());
        super.write(json);
    }
}

