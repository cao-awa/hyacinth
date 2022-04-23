package com.github.cao.awa.hyacinth.server.config;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public abstract class ServerConfigEntry<T> {
    @Nullable
    private final T key;

    public ServerConfigEntry(@Nullable T key) {
        this.key = key;
    }

    @Nullable
    public T getKey() {
        return this.key;
    }

    public boolean isInvalid() {
        return false;
    }

    public abstract void write(JsonObject var1);
}

