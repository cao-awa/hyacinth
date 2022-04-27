/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.registry;

import com.github.cao.awa.hyacinth.server.dimension.*;
import com.github.cao.awa.hyacinth.string.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.util.dynamic.*;
import net.minecraft.util.identifier.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * A manager of dynamic registries. It allows users to access non-hardcoded
 * registries reliably.
 * 
 * <p>Each minecraft server has a dynamic registry manager for file-loaded
 * registries, while each client play network handler has a dynamic registry
 * manager for server-sent dynamic registries.
 * 
 * <p>The {@link DynamicRegistryManager.Impl}
 * class serves as an immutable implementation of any particular collection
 * or configuration of dynamic registries.
 */
public abstract class DynamicRegistryManager {
    static final Logger LOGGER = LogManager.getLogger();
    static final Map<RegistryKey<? extends Registry<?>>, Info<?>> INFOS = EntrustParser.operation(() -> {
        ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, Info<?>> builder = ImmutableMap.builder();
        DynamicRegistryManager.register(builder, Registry.DIMENSION_TYPE_KEY, DimensionType.CODEC, DimensionType.CODEC);
//        DynamicRegistryManager.register(builder, Registry.BIOME_KEY, Biome.CODEC, Biome.field_26633);
//        DynamicRegistryManager.register(builder, Registry.CONFIGURED_CARVER_KEY, ConfiguredCarver.CODEC);
//        DynamicRegistryManager.register(builder, Registry.CONFIGURED_FEATURE_KEY, ConfiguredFeature.CODEC);
//        DynamicRegistryManager.register(builder, Registry.PLACED_FEATURE_KEY, PlacedFeature.CODEC);
//        DynamicRegistryManager.register(builder, Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, ConfiguredStructureFeature.CODEC);
//        DynamicRegistryManager.register(builder, Registry.STRUCTURE_PROCESSOR_LIST_KEY, StructureProcessorType.field_25876);
//        DynamicRegistryManager.register(builder, Registry.STRUCTURE_POOL_KEY, StructurePool.CODEC);
//        DynamicRegistryManager.register(builder, Registry.CHUNK_GENERATOR_SETTINGS_KEY, ChunkGeneratorSettings.CODEC);
//        DynamicRegistryManager.register(builder, Registry.NOISE_WORLDGEN, DoublePerlinNoiseSampler.NoiseParameters.field_35424);
        return builder.build();
    });
    private static final Impl BUILTIN = EntrustParser.operation(() -> {
        Impl impl = new Impl();
        DimensionType.addRegistryDefaults(impl);
        INFOS.keySet().stream().filter(registryKey -> !registryKey.equals(Registry.DIMENSION_TYPE_KEY)).forEach(registryKey -> DynamicRegistryManager.copyFromBuiltin(impl, registryKey));
        return impl;
    });

    /**
     * Retrieves a registry optionally from this manager.
     */
    public abstract <E> Optional<MutableRegistry<E>> getOptionalMutable(RegistryKey<? extends Registry<? extends E>> var1);

    public <E> MutableRegistry<E> getMutable(RegistryKey<? extends Registry<? extends E>> key) {
        return this.getOptionalMutable(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key));
    }

    public <E> Optional<? extends Registry<E>> getOptional(RegistryKey<? extends Registry<? extends E>> key) {
        Optional<MutableRegistry<E>> optional = this.getOptionalMutable(key);
        if (optional.isPresent()) {
            return optional;
        }
        return (Optional<? extends Registry<E>>) Registry.REGISTRIES.getOrEmpty(key.getValue());
    }

    /**
     * Retrieves a registry from this manager, or throws an exception when the
     * registry does not exist.
     * 
     * @throws IllegalStateException if the registry does not exist
     */
    public <E> Registry<E> get(RegistryKey<? extends Registry<? extends E>> key) {
        return this.getOptional(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key));
    }

    private static <E> void register(ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, Info<?>> infosBuilder, RegistryKey<? extends Registry<E>> registryRef, Codec<E> entryCodec) {
        infosBuilder.put(registryRef, new Info<E>(registryRef, entryCodec, null));
    }

    private static <E> void register(ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, Info<?>> infosBuilder, RegistryKey<? extends Registry<E>> registryRef, Codec<E> entryCodec, Codec<E> networkEntryCodec) {
        infosBuilder.put(registryRef, new Info<E>(registryRef, entryCodec, networkEntryCodec));
    }

    public static Iterable<Info<?>> getInfos() {
        return INFOS.values();
    }

    /**
     * Creates a default dynamic registry manager.
     */
    public static Impl create() {
        Impl impl = new Impl();
        RegistryOps.EntryLoader.Impl impl2 = new RegistryOps.EntryLoader.Impl();
        for (Info<?> info : INFOS.values()) {
            DynamicRegistryManager.method_31141(impl, impl2, info);
        }
        RegistryOps.ofLoaded(JsonOps.INSTANCE, impl2, impl);
        return impl;
    }

    private static <E> void method_31141(Impl registryManager, RegistryOps.EntryLoader.Impl entryLoader, Info<E> info) {
        RegistryKey<Registry<E>> registryKey = (RegistryKey<Registry<E>>) info.registry();
//        boolean bl = !registryKey.equals(Registry.CHUNK_GENERATOR_SETTINGS_KEY) && !registryKey.equals(Registry.DIMENSION_TYPE_KEY);
        Registry<E> registry = BUILTIN.get(registryKey);
        MutableRegistry<E> mutableRegistry = registryManager.getMutable(registryKey);
        for (Map.Entry<RegistryKey<E>, E> entry : registry.getEntries()) {
            RegistryKey<E> registryKey2 = entry.getKey();
            E object = entry.getValue();
//            if (bl) {
//                entryLoader.add(BUILTIN, registryKey2, info.entryCodec(), registry.getRawId(object), object, registry.getEntryLifecycle(object));
//                continue;
//            }
            mutableRegistry.set(registry.getRawId(object), registryKey2, object, registry.getEntryLifecycle(object));
        }
    }

    /**
     * Add all entries of the registry referred by {@code registryRef} to the
     * corresponding registry within this manager.
     */
    private static <R extends Registry<?>> void copyFromBuiltin(Impl manager, RegistryKey<R> registryRef) {
        Registry<Registry<?>> registry = (Registry<Registry<?>>) BuiltinRegistries.REGISTRIES;
        Registry<?> registry2 = registry.getOrThrow((RegistryKey<Registry<?>>) registryRef);
        DynamicRegistryManager.addBuiltinEntries(manager, registry2);
    }

    /**
     * Add all entries of the {@code registry} to the corresponding registry
     * within this manager.
     */
    private static <E> void addBuiltinEntries(Impl manager, Registry<E> registry) {
        MutableRegistry<E> mutableRegistry = manager.getMutable(registry.getKey());
        for (Map.Entry<RegistryKey<E>, E> entry : registry.getEntries()) {
            E object = entry.getValue();
            mutableRegistry.set(registry.getRawId(object), entry.getKey(), object, registry.getEntryLifecycle(object));
        }
    }

    /**
     * Loads a dynamic registry manager from the resource manager's data files.
     */
    public static void load(DynamicRegistryManager manager, RegistryOps<?> ops) {
        for (Info<?> info : INFOS.values()) {
            DynamicRegistryManager.load(ops, manager, info);
        }
    }

    /**
     * Loads elements from the {@code ops} into the registry specified by {@code
     * info} within the {@code manager}. Note that the resource manager instance
     * is kept within the {@code ops}.
     */
    private static <E> void load(RegistryOps<?> ops, DynamicRegistryManager manager, Info<E> info) {
        RegistryKey<? extends Registry<E>> registryKey = info.registry();
        SimpleRegistry<E> simpleRegistry = (SimpleRegistry<E>)manager.getMutable(registryKey);
        DataResult<SimpleRegistry<E>> dataResult = ops.loadToRegistry(simpleRegistry, info.registry(), info.entryCodec());
        dataResult.error().ifPresent(partialResult -> {
            throw new JsonParseException("Error loading registry data: " + partialResult.message());
        });
    }

    public record Info<E>(RegistryKey<? extends Registry<E>> registry, Codec<E> entryCodec, @Nullable Codec<E> networkEntryCodec) {
        public boolean isSynced() {
            return this.networkEntryCodec != null;
        }
    }

    public static final class Impl
    extends DynamicRegistryManager {
        public static final Codec<Impl> CODEC = Impl.setupCodec();
        private final Map<? extends RegistryKey<? extends Registry<?>>, ? extends SimpleRegistry<?>> registries;

        private static <E> Codec<Impl> setupCodec() {
            Codec<RegistryKey> codec = Identifier.CODEC.xmap(RegistryKey::ofRegistry, RegistryKey::getValue);
            Codec<SimpleRegistry> codec2 = codec.partialDispatch("type", (SimpleRegistry simpleRegistry) -> DataResult.success(simpleRegistry.getKey()), registryKey -> Impl.getDataResultForCodec(registryKey).map(codec1 -> SimpleRegistry.createRegistryManagerCodec(registryKey, Lifecycle.experimental(), ((Codec<E>)codec1))));
            UnboundedMapCodec<RegistryKey, SimpleRegistry> unboundedMapCodec = Codec.unboundedMap(codec, codec2);
            return Impl.fromRegistryCodecs(unboundedMapCodec);
        }

        private static <K extends RegistryKey<? extends Registry<?>>, V extends SimpleRegistry<?>> Codec<Impl> fromRegistryCodecs(UnboundedMapCodec<K, V> unboundedMapCodec) {
            return unboundedMapCodec.xmap(Impl::new, impl -> impl.registries.entrySet().stream().filter(entry -> INFOS.get(entry.getKey()).isSynced()).collect(ImmutableMap.toImmutableMap(entry1 -> (K)entry1.getKey(), entry2 -> (V)entry2.getValue())));
        }

        private static <E> DataResult<? extends Codec<E>> getDataResultForCodec(RegistryKey<? extends Registry<E>> registryRef) {
            return (DataResult) Optional.ofNullable(INFOS.get(registryRef)).map(info -> info.networkEntryCodec()).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown or not serializable registry: " + registryRef));
        }

        public Impl() {
            this(INFOS.keySet().stream().collect(Collectors.toMap(Function.identity(), Impl::createRegistry)));
        }

//        public static DynamicRegistryManager method_39199(Dynamic<?> dynamic) {
//            return new Impl(INFOS.keySet().stream().collect(Collectors.toMap(Function.identity(), registryKey -> Impl.method_39201(registryKey, dynamic))));
//        }

//        private static <E> SimpleRegistry<?> method_39201(RegistryKey<? extends Registry<?>> registryKey, Dynamic<?> dynamic) {
//            return (SimpleRegistry)RegistryLookupCodec.of(registryKey).codec().parse(dynamic).resultOrPartial(StringUtil.addPrefix(registryKey + " registry: ", LOGGER::error)).orElseThrow(() -> new IllegalStateException("Failed to get " + registryKey + " registry"));
//        }

        private Impl(Map<? extends RegistryKey<? extends Registry<?>>, ? extends SimpleRegistry<?>> registries) {
            this.registries = registries;
        }

        private static <E> SimpleRegistry<?> createRegistry(RegistryKey<? extends Registry<?>> registryRef) {
            return new SimpleRegistry(registryRef, Lifecycle.stable());
        }

        @Override
        public <E> Optional<MutableRegistry<E>> getOptionalMutable(RegistryKey<? extends Registry<? extends E>> key) {
            return (Optional) Optional.ofNullable(this.registries.get(key));
        }
    }
}

