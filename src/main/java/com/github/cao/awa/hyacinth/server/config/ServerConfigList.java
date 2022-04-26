package com.github.cao.awa.hyacinth.server.config;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.*;
import net.minecraft.util.json.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public abstract class ServerConfigList<K, V extends ServerConfigEntry<K>> {
    protected static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File file;
    private final Map<String, V> map = Maps.newHashMap();

    public ServerConfigList(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void add(V entry) {
        this.map.put(this.toString(entry.getKey()), entry);
        try {
            this.save();
        }
        catch (IOException iOException) {
            LOGGER.warn("Could not save the list after adding a user.", iOException);
        }
    }

    @Nullable
    public V get(K key) {
        this.removeInvalidEntries();
        return this.map.get(this.toString(key));
    }

    public void remove(K key) {
        this.map.remove(this.toString(key));
        try {
            this.save();
        }
        catch (IOException iOException) {
            LOGGER.warn("Could not save the list after removing a user.", iOException);
        }
    }

    public void remove(ServerConfigEntry<K> entry) {
        this.remove(entry.getKey());
    }

    public String[] getNames() {
        return this.map.keySet().toArray(new String[0]);
    }

    public boolean isEmpty() {
        return this.map.size() < 1;
    }

    public String toString(K profile) {
        return profile.toString();
    }

    public boolean contains(K object) {
        return this.map.containsKey(this.toString(object));
    }

    public void removeInvalidEntries() {
        ArrayList<Object> list = Lists.newArrayList();
        for (Object serverConfigEntry : this.map.values()) {
            if (!((ServerConfigEntry)serverConfigEntry).isInvalid()) continue;
            list.add(((ServerConfigEntry)serverConfigEntry).getKey());
        }
        for (Object serverConfigEntry : list) {
            this.map.remove(this.toString((K) serverConfigEntry));
        }
    }

    public abstract ServerConfigEntry<K> fromJson(JsonObject var1);

    public Collection<V> values() {
        return this.map.values();
    }

    public void save() throws IOException {
        JsonArray jsonArray = new JsonArray();
        this.map.values().stream().map(entry -> EntrustParser.operation(new JsonObject(), entry::write)).forEach(jsonArray::add);
        try (BufferedWriter bufferedWriter = Files.newWriter(this.file, StandardCharsets.UTF_8)){
            GSON.toJson(jsonArray, bufferedWriter);
        }
    }

    public void load() throws IOException {
        if (!this.file.exists()) {
            return;
        }
        try (BufferedReader bufferedReader = Files.newReader(this.file, StandardCharsets.UTF_8)){
            JsonArray jsonArray = GSON.fromJson(bufferedReader, JsonArray.class);
            this.map.clear();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = JsonHelper.asObject(jsonElement, "entry");
                ServerConfigEntry<K> serverConfigEntry = this.fromJson(jsonObject);
                if (serverConfigEntry.getKey() == null) continue;
                this.map.put(this.toString(serverConfigEntry.getKey()), (V) serverConfigEntry);
            }
        }
    }
}
