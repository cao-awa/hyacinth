/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.registry;

import com.mojang.serialization.Lifecycle;
import java.util.OptionalInt;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public abstract class MutableRegistry<T>
extends Registry<T> {
    public MutableRegistry(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    public abstract <V extends T> V set(int var1, RegistryKey<T> var2, V var3, Lifecycle var4);

    public abstract <V extends T> V add(RegistryKey<T> var1, V var2, Lifecycle var3);

    /**
     * If the given key is already present in the registry, replaces the entry associated with the given
     * key with the new entry. This method asserts that the raw ID is equal to the value already in
     * the registry. The raw ID not being present may lead to buggy behavior.
     * 
     * <p>If the given key is not already present in the registry, adds the entry to the registry. If
     * {@code rawId} is present, then this method gives the entry this raw ID. Otherwise, uses the
     * next available ID.
     */
    public abstract <V extends T> V replace(OptionalInt var1, RegistryKey<T> var2, V var3, Lifecycle var4);

    public abstract boolean isEmpty();
}

