package net.minecraft;

import com.github.cao.awa.hyacinth.network.text.Text;
import org.jetbrains.annotations.Nullable;

public interface Nameable {
    Text getName();

    default boolean hasCustomName() {
        return this.getCustomName() != null;
    }

    default Text getDisplayName() {
        return this.getName();
    }

    @Nullable
    default Text getCustomName() {
        return null;
    }
}

