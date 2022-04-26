package net.minecraft.util.dynamic;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class Codecs {
    public static <E> Codec<E> method_39512(final Codec<E> codec, final Codec<E> codec2) {
        return new Codec<>() {
            @Override
            public <T> DataResult<T> encode(E object, DynamicOps<T> dynamicOps, T object2) {
                if (dynamicOps.compressMaps()) {
                    return codec2.encode(object, dynamicOps, object2);
                }
                return codec.encode(object, dynamicOps, object2);
            }

            @Override
            public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> dynamicOps, T object) {
                if (dynamicOps.compressMaps()) {
                    return codec2.decode(dynamicOps, object);
                }
                return codec.decode(dynamicOps, object);
            }

            public String toString() {
                return codec + " orCompressed " + codec2;
            }
        };
    }

    public static <E> Codec<E> method_39508(Function<E, String> function, Function<String, E> function2) {
        return Codec.STRING.flatXmap(string -> Optional.ofNullable(function2.apply(string)).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown element name:" + string)), object -> Optional.ofNullable(function.apply(object)).map(DataResult::success).orElseGet(() -> DataResult.error("Element with unknown name: " + object)));
    }

    public static <E> Codec<E> method_39511(ToIntFunction<E> toIntFunction, IntFunction<E> intFunction, int i) {
        return Codec.INT.flatXmap(integer -> Optional.ofNullable(intFunction.apply(integer)).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown element id: " + integer)), object -> {
            int j = toIntFunction.applyAsInt(object);
            return j == i ? DataResult.error("Element with unknown id: " + object) : DataResult.success(j);
        });
    }

    public static <E> Codec<E> method_39504(Codec<E> codec, final Function<E, Lifecycle> function, final Function<E, Lifecycle> function2) {
        return codec.mapResult(new Codec.ResultFunction<E>(){
            @Override
            public <T> DataResult<Pair<E, T>> apply(DynamicOps<T> dynamicOps, T object, DataResult<Pair<E, T>> dataResult) {
                return dataResult.result().map(pair -> dataResult.setLifecycle(function.apply(pair.getFirst()))).orElse(dataResult);
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, E object, DataResult<T> dataResult) {
                return dataResult.setLifecycle(function2.apply(object));
            }

            public String toString() {
                return "WithLifecycle[" + function + " " + function2 + "]";
            }
        });
    }
}
