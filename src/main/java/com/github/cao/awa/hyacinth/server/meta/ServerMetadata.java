package com.github.cao.awa.hyacinth.server.meta;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.zhuaidadaya.rikaishinikui.handler.entrust.EntrustExecution;
import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.json.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Represents metadata sent to the client. This describes the server's message of the day, online players and the protocol version.
 */
public class ServerMetadata {
    public static final int FAVICON_WIDTH = 64;
    public static final int FAVICON_HEIGHT = 64;
    @Nullable
    private Text description = new LiteralText("A Minecraft Server");
    @Nullable
    private Players players = new Players(20, 0);
    @Nullable
    private Version version = new Version(SharedConstants.getGameVersion().getName(), SharedConstants.getGameVersion().getProtocolVersion());
    @Nullable
    private String favicon = "";

    @Nullable
    public Text getDescription() {
        return this.description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    @Nullable
    public String getFavicon() {
        return this.favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public JsonObject toJSONObject() {
        JsonObject json = new JsonObject();

        EntrustExecution.notNull(version, v -> json.add("version", v.toJSONObject()));
        EntrustExecution.notNull(players, p -> json.add("players", p.toJSONObject()));
        EntrustExecution.notNull(favicon, icon -> json.addProperty("favicon", icon));
        EntrustExecution.notNull(description, des -> json.add("description", des.toJSONObject()));

        return json;
    }

    @Nullable
    public Players getPlayers() {
        return this.players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    @Nullable
    public Version getVersion() {
        return this.version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public static class Players {
        private final int max;
        private final int online;
        @Nullable
        private GameProfile[] sample;

        public Players(int max, int online) {
            this.max = max;
            this.online = online;
        }

        public int getPlayerLimit() {
            return this.max;
        }

        public int getOnlinePlayerCount() {
            return this.online;
        }

        @Nullable
        public GameProfile[] getSample() {
            return this.sample;
        }

        public void setSample(GameProfile[] sample) {
            this.sample = sample;
        }

        public JsonObject toJSONObject() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("max", getPlayerLimit());
            jsonObject.addProperty("online", getOnlinePlayerCount());
            GameProfile[] gameProfiles = getSample();
            if (gameProfiles != null && gameProfiles.length > 0) {
                JsonArray jsonArray = new JsonArray();
                for (GameProfile gameProfile : gameProfiles) {
                    JsonObject jsonObject2 = new JsonObject();
                    UUID uUID = gameProfile.getId();
                    jsonObject2.addProperty("id", uUID == null ? "" : uUID.toString());
                    jsonObject2.addProperty("name", gameProfile.getName());
                    jsonArray.add(jsonObject2);
                }
                jsonObject.add("sample", jsonArray);
            }
            return jsonObject;
        }
    }

    public static class Version {
        private final String gameVersion;
        private final int protocolVersion;

        public Version(String gameVersion, int protocolVersion) {
            this.gameVersion = gameVersion;
            this.protocolVersion = protocolVersion;
        }

        public String getGameVersion() {
            return this.gameVersion;
        }

        public int getProtocolVersion() {
            return this.protocolVersion;
        }

        public JsonObject toJSONObject() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", getGameVersion());
            jsonObject.addProperty("protocol", getProtocolVersion());
            return jsonObject;
        }
    }

    public static class Deserializer implements JsonDeserializer<ServerMetadata>, JsonSerializer<ServerMetadata> {
        @Override
        public ServerMetadata deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = JsonHelper.asObject(jsonElement, "status");
            ServerMetadata serverMetadata = new ServerMetadata();
            if (jsonObject.has("description")) {
                serverMetadata.setDescription(jsonDeserializationContext.deserialize(jsonObject.get("description"), Text.class));
            }
            if (jsonObject.has("players")) {
                serverMetadata.setPlayers(jsonDeserializationContext.deserialize(jsonObject.get("players"), Players.class));
            }
            if (jsonObject.has("version")) {
                serverMetadata.setVersion(jsonDeserializationContext.deserialize(jsonObject.get("version"), Version.class));
            }
            if (jsonObject.has("favicon")) {
                serverMetadata.setFavicon(JsonHelper.getString(jsonObject, "favicon"));
            }
            return serverMetadata;
        }

        @Override
        public JsonElement serialize(ServerMetadata serverMetadata, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            if (serverMetadata.getDescription() != null) {
                jsonObject.add("description", jsonSerializationContext.serialize(serverMetadata.getDescription()));
            }
            if (serverMetadata.getPlayers() != null) {
                jsonObject.add("players", jsonSerializationContext.serialize(serverMetadata.getPlayers()));
            }
            if (serverMetadata.getVersion() != null) {
                jsonObject.add("version", jsonSerializationContext.serialize(serverMetadata.getVersion()));
            }
            if (serverMetadata.getFavicon() != null) {
                jsonObject.addProperty("favicon", serverMetadata.getFavicon());
            }
            return jsonObject;
        }
    }
}