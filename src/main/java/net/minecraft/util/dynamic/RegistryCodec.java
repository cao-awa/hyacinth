package net.minecraft.util.dynamic;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import net.minecraft.util.registry.*;

/**
 * A codec for {@link SimpleRegistry}.
 * 
 * <p>Compared to regular codec, this codec performs additional work when
 * decoding, loading its elements from the given resource manager's JSON
 * files.
 * 
 * @param <E> the registry's element type
 * @see RegistryElementCodec
 * @see RegistryOps
 */
public final class RegistryCodec<E>
implements Codec<SimpleRegistry<E>> {
    private final Codec<SimpleRegistry<E>> delegate;
    private final RegistryKey<? extends Registry<E>> registryRef;
    private final Codec<E> elementCodec;

    public static <E> RegistryCodec<E> of(RegistryKey<? extends Registry<E>> registryRef, Lifecycle lifecycle, Codec<E> codec) {
        return new RegistryCodec<>(registryRef, lifecycle, codec);
    }

    private RegistryCodec(RegistryKey<? extends Registry<E>> registryRef, Lifecycle lifecycle, Codec<E> codec) {
        this.delegate = SimpleRegistry.createCodec(registryRef, lifecycle, codec);
        this.registryRef = registryRef;
        this.elementCodec = codec;
    }

    @Override
    public <T> DataResult<T> encode(SimpleRegistry<E> simpleRegistry, DynamicOps<T> dynamicOps, T object) {
        return this.delegate.encode(simpleRegistry, dynamicOps, object);
    }

    @Override
    public <T> DataResult<Pair<SimpleRegistry<E>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<Pair<SimpleRegistry<E>, T>> dataResult = this.delegate.decode(ops, input);
        if (ops instanceof RegistryOps) {
            return dataResult.flatMap((pair) -> ((RegistryOps<?>)ops).loadToRegistry(pair.getFirst(), this.registryRef, this.elementCodec).map((simpleRegistry) -> Pair.of(simpleRegistry, pair.getSecond())));
        }
        return dataResult;
    }

    public String toString() {
        return "RegistryDataPackCodec[" + this.delegate + " " + this.registryRef + " " + this.elementCodec + "]";
    }
}

