package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import net.minecraft.util.dynamic.Codecs;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface StringIdentifiable {
    String asString();

    /**
     * Creates a codec that serializes an enum implementing this interface either
     * using its ordinals (when compressed) or using its {@link #asString()} method
     * and a given decode function.
     */
    static <E extends Enum<E>> Codec<E> createCodec(Supplier<E[]> enumValues, Function<String, E> fromString) {
        Enum[] enums = enumValues.get();
        return Codecs.method_39512(
                (Codec) Codecs.method_39508(object -> ((StringIdentifiable)object).asString(), fromString),
                (Codec) Codecs.method_39511(object -> object.ordinal(), ordinal -> ordinal > -1 && ordinal < enums.length ? enums[ordinal] : null, -1));
    }

    static Keyable toKeyable(final StringIdentifiable[] values) {
        return new Keyable(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Arrays.stream(values).map(StringIdentifiable::asString).map(dynamicOps::createString);
            }
        };
    }
}

