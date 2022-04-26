/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.resource.metadata;

import com.google.gson.JsonObject;

public interface ResourceMetadataReader<T> {
    public String getKey();

    public T fromJson(JsonObject var1);
}

