package net.minecraft.nbt;

import net.minecraft.util.dynamic.DynamicSerializableUuid;

import java.util.UUID;

public class NbtHelper {
    /**
     * Deserializes an NBT element into a {@link UUID}.
     * The NBT element's data must have the same structure as the output of {@link #fromUuid}.
     *
     * @throws IllegalArgumentException if {@code element} is not a valid representation of a UUID
     * @since 20w10a
     */
    public static UUID toUuid(NbtElement element) {
        if (element.getNbtType() != NbtIntArray.TYPE) {
            throw new IllegalArgumentException("Expected UUID-Tag to be of type " + NbtIntArray.TYPE.getCrashReportName() + ", but found " + element.getNbtType().getCrashReportName() + ".");
        }
        int[] is = ((NbtIntArray)element).getIntArray();
        if (is.length != 4) {
            throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + is.length + ".");
        }
        return DynamicSerializableUuid.toUuid(is);
    }

    /**
     * Serializes a {@link UUID} into its equivalent NBT representation.
     *
     * @since 20w10a
     */
    public static NbtIntArray fromUuid(UUID uuid) {
        return new NbtIntArray(DynamicSerializableUuid.toIntArray(uuid));
    }
}
