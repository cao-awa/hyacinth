package com.github.cao.awa.hyacinth.version;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.bridge.game.PackType;
import net.minecraft.util.json.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class MinecraftVersion
        implements GameVersion {
    private static final Logger LOGGER = LogManager.getLogger("Version:Server");
    public static final GameVersion CURRENT = new MinecraftVersion();
    private final String id;
    private final String name;
    private final boolean stable;
    private final SaveVersion saveVersion;
    private final int protocolVersion;
    private final int resourcePackVersion;
    private final int dataPackVersion;
    private final Date buildTime;
    private final String releaseTarget;

    private MinecraftVersion() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.name = "1.18";
        this.stable = true;
        this.saveVersion = new SaveVersion(2860, "main");
        this.protocolVersion = SharedConstants.getProtocolVersion();
        this.resourcePackVersion = 8;
        this.dataPackVersion = 8;
        this.buildTime = new Date();
        this.releaseTarget = "1.18";
    }

    private MinecraftVersion(JsonObject json) {
        this.id = JsonHelper.getString(json, "id");
        this.name = JsonHelper.getString(json, "name");
        this.releaseTarget = JsonHelper.getString(json, "release_target");
        this.stable = JsonHelper.getBoolean(json, "stable");
        this.saveVersion = new SaveVersion(JsonHelper.getInt(json, "world_version"), JsonHelper.getString(json, "series_id", SaveVersion.MAIN_SERIES));
        this.protocolVersion = JsonHelper.getInt(json, "protocol_version");
        JsonObject jsonObject = JsonHelper.getObject(json, "pack_version");
        this.resourcePackVersion = JsonHelper.getInt(jsonObject, "resource");
        this.dataPackVersion = JsonHelper.getInt(jsonObject, "data");
        this.buildTime = Date.from(ZonedDateTime.parse(JsonHelper.getString(json, "build_time")).toInstant());
    }

    public static GameVersion create() {
        try (InputStream inputStream = MinecraftVersion.class.getResourceAsStream("/version.json");){
            MinecraftVersion minecraftVersion;
            if (inputStream == null) {
                LOGGER.warn("Missing version information!");
                GameVersion gameVersion = CURRENT;
                return gameVersion;
            }
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);){
                minecraftVersion = new MinecraftVersion(JsonHelper.deserialize(inputStreamReader));
            }
            return minecraftVersion;
        }
        catch (JsonParseException | IOException inputStream2) {
            throw new IllegalStateException("Game version information is corrupt", inputStream2);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getReleaseTarget() {
        return this.releaseTarget;
    }

    @Override
    public SaveVersion getSaveVersion() {
        return this.saveVersion;
    }

    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public int getPackVersion(PackType packType) {
        return packType == PackType.DATA ? this.dataPackVersion : this.resourcePackVersion;
    }

    @Override
    public Date getBuildTime() {
        return this.buildTime;
    }

    @Override
    public boolean isStable() {
        return this.stable;
    }
}

