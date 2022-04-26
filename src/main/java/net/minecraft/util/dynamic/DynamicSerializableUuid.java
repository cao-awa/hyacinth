package net.minecraft.util.dynamic;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;

import java.util.Arrays;
import java.util.UUID;

public final class DynamicSerializableUuid {
    public static final Codec<UUID> CODEC = Codec.INT_STREAM.comapFlatMap(uuidStream -> EntrustParser.toArray(uuidStream, 4).map(DynamicSerializableUuid::toUuid), uuid -> Arrays.stream(DynamicSerializableUuid.toIntArray(uuid)));

    private DynamicSerializableUuid() {
    }

    public static UUID toUuid(int[] array) {
        return new UUID((long)array[0] << 32 | (long)array[1] & 0xFFFFFFFFL, (long)array[2] << 32 | (long)array[3] & 0xFFFFFFFFL);
    }

    public static int[] toIntArray(UUID uuid) {
        long l = uuid.getMostSignificantBits();
        long m = uuid.getLeastSignificantBits();
        return DynamicSerializableUuid.toIntArray(l, m);
    }

    private static int[] toIntArray(long uuidMost, long uuidLeast) {
        return new int[]{(int)(uuidMost >> 32), (int)uuidMost, (int)(uuidLeast >> 32), (int)uuidLeast};
    }

    public static UUID toUuid(Dynamic<?> dynamic) {
        int[] is = dynamic.asIntStream().toArray();
        if (is.length != 4) {
            throw new IllegalArgumentException("Could not read UUID. Expected int-array of length 4, got " + is.length + ".");
        }
        return DynamicSerializableUuid.toUuid(is);
    }
}

