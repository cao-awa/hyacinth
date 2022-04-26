package net.minecraft.util.registry;

import com.mojang.serialization.*;
import net.minecraft.util.identifier.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DefaultedRegistry<T>
extends SimpleRegistry<T> {
    private final Identifier defaultId;
    private T defaultValue;

    public DefaultedRegistry(String defaultId, RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        super(key, lifecycle);
        this.defaultId = new Identifier(defaultId);
    }

    @Override
    public <V extends T> V set(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle) {
        if (this.defaultId.equals(key.getValue())) {
            this.defaultValue = entry;
        }
        return super.set(rawId, key, entry, lifecycle);
    }

    @Override
    public int getRawId(@Nullable T entry) {
        int i = super.getRawId(entry);
        return i == -1 ? super.getRawId(this.defaultValue) : i;
    }

    @Override
    @NotNull
    public Identifier getId(T entry) {
        Identifier identifier = super.getId(entry);
        return identifier == null ? this.defaultId : identifier;
    }

    @Override
    @NotNull
    public T get(@Nullable Identifier id) {
        T object = super.get(id);
        return object == null ? this.defaultValue : object;
    }

    @Override
    public Optional<T> getOrEmpty(@Nullable Identifier id) {
        return Optional.ofNullable(super.get(id));
    }

    @Override
    @NotNull
    public T get(int index) {
        T object = super.get(index);
        return object == null ? this.defaultValue : object;
    }

    @Override
    @NotNull
    public T getRandom(Random random) {
        T object = super.getRandom(random);
        return object == null ? this.defaultValue : object;
    }

    public Identifier getDefaultId() {
        return this.defaultId;
    }
}

