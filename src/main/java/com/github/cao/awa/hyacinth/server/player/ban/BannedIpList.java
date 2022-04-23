package com.github.cao.awa.hyacinth.server.player.ban;

import com.github.cao.awa.hyacinth.server.config.ServerConfigEntry;
import com.github.cao.awa.hyacinth.server.config.ServerConfigList;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.SocketAddress;

public class BannedIpList
        extends ServerConfigList<String, BannedIpEntry> {
    public BannedIpList(File file) {
        super(file);
    }

    @Override
    public ServerConfigEntry<String> fromJson(JsonObject json) {
        return new BannedIpEntry(json);
    }

    public boolean isBanned(SocketAddress ip) {
        String string = this.stringifyAddress(ip.toString());
        return this.contains(string);
    }

    public boolean isBanned(String ip) {
        return this.contains(ip);
    }

    @Override
    @Nullable
    public BannedIpEntry get(String address) {
        String string = this.stringifyAddress(address);
        return this.get(string);
    }

    private String stringifyAddress(String address) {
        String string = address;
        if (string.contains("/")) {
            string = string.substring(string.indexOf(47) + 1);
        }
        if (string.contains(":")) {
            string = string.substring(0, string.indexOf(58));
        }
        return string;
    }
}

