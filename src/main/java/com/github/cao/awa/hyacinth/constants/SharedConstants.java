package com.github.cao.awa.hyacinth.constants;

import com.github.cao.awa.hyacinth.version.GameVersion;
import com.github.cao.awa.hyacinth.version.MinecraftVersion;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.util.ResourceLeakDetector;
import org.jetbrains.annotations.Nullable;

public class SharedConstants {
    @Deprecated
    public static final boolean IS_DEVELOPMENT_VERSION = false;
    @Deprecated
    public static final int WORLD_VERSION = 2860;
    @Deprecated
    public static final String CURRENT_SERIES = "main";
    @Deprecated
    public static final String VERSION_NAME = "1.18";
    @Deprecated
    public static final String RELEASE_TARGET = "1.18";
    @Deprecated
    public static final int RELEASE_TARGET_PROTOCOL_VERSION = 757;
    @Deprecated
    public static final int field_29736 = 60;
    public static final int SNBT_TOO_OLD_THRESHOLD = 2830;
    @Deprecated
    public static final int RESOURCE_PACK_VERSION = 8;
    @Deprecated
    public static final int DATA_PACK_VERSION = 8;
    public static final String DATA_VERSION_KEY = "DataVersion";
    public static boolean DEBUG_BIOME_SOURCE = false;
    public static boolean DEBUG_NOISE = false;
    public static final int DEFAULT_PORT = 25565;
    public static final ResourceLeakDetector.Level RESOURCE_LEAK_DETECTOR_DISABLED = ResourceLeakDetector.Level.DISABLED;
    public static final String OVERWORLD_ID = "minecraft:WORLD_OVERWORLD";

    /**
     * Specifies whether Minecraft should use choice type registrations from the game's schema when entity types or block entity types are created.
     */
    public static boolean useChoiceTypeRegistrations = true;
    public static boolean isDevelopment;
    public static final int CHUNK_WIDTH = 16;
    public static final int DEFAULT_WORLD_HEIGHT = 256;
    public static final int COMMAND_MAX_LENGTH = 32500;
    public static final char[] INVALID_CHARS_LEVEL_NAME;
    public static final int TICKS_PER_SECOND = 20;
    public static final int TICKS_PER_MINUTE = 1200;
    public static final int TICKS_PER_IN_GAME_DAY = 24000;
    @Nullable
    private static GameVersion gameVersion;

    /**
     * {@return true if the character is not {@linkplain
     * com.github.cao.awa.hyacinth.network.text.style.Formatting#FORMATTING_CODE_PREFIX the formatting code
     * prefix} (&bsol;u00a7), C0 control code (&bsol;u0000 to &bsol;u001f) or
     * delete (&bsol;u007f)}
     *
     * @apiNote This method is used to determine if the server should
     * accept a chat message sent from client.
     *
     * @see net.minecraft.server.network.ServerPlayNetworkHandler#onChatMessage
     */
    public static boolean isValidChar(char chr) {
        return chr != '\u00a7' && chr >= ' ' && chr != '\u007f';
    }

    public static String stripInvalidChars(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (!SharedConstants.isValidChar(c)) continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static void setGameVersion(GameVersion gameVersion) {
        if (SharedConstants.gameVersion == null) {
            SharedConstants.gameVersion = gameVersion;
        } else if (gameVersion != SharedConstants.gameVersion) {
            throw new IllegalStateException("Cannot override the current game version!");
        }
    }

    public static void createGameVersion() {
        if (gameVersion == null) {
            gameVersion = MinecraftVersion.create();
        }
    }

    public static GameVersion getGameVersion() {
        if (gameVersion == null) {
            throw new IllegalStateException("Game version not set");
        }
        return gameVersion;
    }

    public static int getProtocolVersion() {
        return 756;
    }

    // TODO: 2022/4/22  
//    public static boolean method_37896(ChunkPos chunkPos) {
//        int i = chunkPos.getStartX();
//        int j = chunkPos.getStartZ();
//        if (DEBUG_BIOME_SOURCE) {
//            return i > 8192 || i < 0 || j > 1024 || j < 0;
//        }
//        return false;
//    }

    static {
        INVALID_CHARS_LEVEL_NAME = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
        ResourceLeakDetector.setLevel(RESOURCE_LEAK_DETECTOR_DISABLED);
        CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES = false;
//        CommandSyntaxException.BUILT_IN_EXCEPTIONS = new TranslatableBuiltInExceptions();

        // TODO: 2022/4/22  
                CommandSyntaxException.BUILT_IN_EXCEPTIONS = null;
    }
}

