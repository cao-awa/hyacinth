package net.minecraft.util;

import org.jetbrains.annotations.*;

/**
 * Represents an object which can be cleared.
 */
public interface Clearable {
    void clear();

    static void clear(@Nullable Object o) {
        if (o instanceof Clearable) {
            ((Clearable)o).clear();
        }
    }
}


