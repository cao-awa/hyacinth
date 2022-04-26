package net.minecraft.util.registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.identifier.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stores a few hardcoded registries with builtin values for datapack-loadable registries,
 * from which a registry tracker can create a new dynamic registry.
 * 
 * <p>Note that these registries do not contain the actual entries that the server has,
 * for that you will need to access it from {@link net.minecraft.util.registry.DynamicRegistryManager}.
 * 
 * @see com.github.cao.awa.hyacinth.server.MinecraftServer#getRegistryManager()
 * @see net.minecraft.client.network.ClientPlayNetworkHandler#getRegistryManager()
 * @see net.minecraft.util.registry.DynamicRegistryManager#get(RegistryKey)
 */
public class BuiltinRegistries {
    protected static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Identifier, Supplier<?>> DEFAULT_VALUE_SUPPLIERS = Maps.newLinkedHashMap();
    private static final MutableRegistry<MutableRegistry<?>> ROOT = new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier("root")), Lifecycle.experimental());
    public static final Registry<? extends Registry<?>> REGISTRIES = ROOT;
//    public static final Registry<ConfiguredCarver<?>> CONFIGURED_CARVER = BuiltinRegistries.addRegistry(Registry.CONFIGURED_CARVER_KEY, () -> ConfiguredCarvers.CAVE);
//    public static final Registry<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE = BuiltinRegistries.addRegistry(Registry.CONFIGURED_FEATURE_KEY, ConfiguredFeatures::getDefaultConfiguredFeature);
//    public static final Registry<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE_FEATURE = BuiltinRegistries.addRegistry(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, ConfiguredStructureFeatures::getDefault);
//    public static final Registry<PlacedFeature> PLACED_FEATURE = BuiltinRegistries.addRegistry(Registry.PLACED_FEATURE_KEY, PlacedFeatures::getDefaultPlacedFeature);
//    public static final Registry<StructureProcessorList> STRUCTURE_PROCESSOR_LIST = BuiltinRegistries.addRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY, () -> StructureProcessorLists.ZOMBIE_PLAINS);
//    public static final Registry<StructurePool> STRUCTURE_POOL = BuiltinRegistries.addRegistry(Registry.STRUCTURE_POOL_KEY, StructurePools::initDefaultPools);
//    public static final Registry<Biome> BIOME = BuiltinRegistries.addRegistry(Registry.BIOME_KEY, () -> BuiltinBiomes.PLAINS);
//    public static final Registry<ChunkGeneratorSettings> CHUNK_GENERATOR_SETTINGS = BuiltinRegistries.addRegistry(Registry.CHUNK_GENERATOR_SETTINGS_KEY, ChunkGeneratorSettings::getInstance);
//    public static final Registry<DoublePerlinNoiseSampler.NoiseParameters> NOISE_PARAMETERS = BuiltinRegistries.addRegistry(Registry.NOISE_WORLDGEN, BuiltinNoiseParameters::init);

    private static <T> Registry<T> addRegistry(RegistryKey<? extends Registry<T>> registryRef, Supplier<T> defaultValueSupplier) {
        return BuiltinRegistries.addRegistry(registryRef, Lifecycle.stable(), defaultValueSupplier);
    }

    private static <T> Registry<T> addRegistry(RegistryKey<? extends Registry<T>> registryRef, Lifecycle lifecycle, Supplier<T> defaultValueSupplier) {
        return BuiltinRegistries.addRegistry(registryRef, new SimpleRegistry(registryRef, lifecycle), defaultValueSupplier, lifecycle);
    }

    private static <T, R extends MutableRegistry<T>> R addRegistry(RegistryKey<? extends Registry<T>> registryRef, R registry, Supplier<T> defaultValueSupplier, Lifecycle lifecycle) {
        Identifier identifier = registryRef.getValue();
        DEFAULT_VALUE_SUPPLIERS.put(identifier, defaultValueSupplier);
        return (R) ROOT.add((RegistryKey) registryRef, registry, lifecycle);
    }

    public static <T> T add(Registry<? super T> registry, String id, T object) {
        return BuiltinRegistries.add(registry, new Identifier(id), object);
    }

    public static <V, T extends V> T add(Registry<V> registry, Identifier id, T object) {
        return BuiltinRegistries.add(registry, RegistryKey.of(registry.getKey(), id), object);
    }

    public static <V, T extends V> T add(Registry<V> registry, RegistryKey<V> key, T object) {
        return (T) ((MutableRegistry)registry).add(key, object, Lifecycle.stable());
    }

    public static <V, T extends V> T set(Registry<V> registry, RegistryKey<V> key, T object) {
        return (T) ((MutableRegistry)registry).add(key, object, Lifecycle.stable());
    }

    public static void init() {
    }

    static {
        DEFAULT_VALUE_SUPPLIERS.forEach((id, supplier) -> {
            if (supplier.get() == null) {
                LOGGER.error("Unable to bootstrap registry '{}'", id);
            }
        });
        Registry.validate(ROOT);
    }
}

