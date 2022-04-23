/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.collection;

import org.jetbrains.annotations.Nullable;

public interface IndexedIterable<T>
extends Iterable<T> {
    int ABSENT_RAW_ID = -1;

    int getRawId(T var1);

    @Nullable T get(int var1);

    int size();
}

