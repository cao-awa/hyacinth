package com.github.cao.awa.hyacinth.version;

/**
 * The game version interface used by Minecraft, replacing the javabridge
 * one's occurences in Minecraft code.
 */
public interface GameVersion
        extends com.mojang.bridge.game.GameVersion {
    @Override
    @Deprecated
    default int getWorldVersion() {
        return this.getSaveVersion().getId();
    }

    @Deprecated
    default String getSeriesId() {
        return this.getSaveVersion().getSeries();
    }

    /**
     * {@return the save version information for this game version}
     */
    SaveVersion getSaveVersion();
}


