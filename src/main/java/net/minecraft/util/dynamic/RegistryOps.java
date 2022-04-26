package net.minecraft.util.dynamic;

import com.github.cao.awa.hyacinth.identity.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.resource.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.registry.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Optional;
import java.util.stream.*;

public class RegistryOps<T> extends ForwardingDynamicOps<T> {
    static final Logger LOGGER = LogManager.getLogger();
    private static final String JSON_FILE_EXTENSION = ".json";
    private final RegistryOps.EntryLoader entryLoader;
    private final DynamicRegistryManager registryManager;
    private final Map<RegistryKey<? extends Registry<?>>, ValueHolder<?>> valueHolders;
    private final RegistryOps<?> entryOps;

    public static <T> RegistryOps<T> ofLoaded(DynamicOps<T> dynamicOps, net.minecraft.resource.ResourceManager resourceManager, DynamicRegistryManager registryManager) {
        return ofLoaded(dynamicOps, RegistryOps.EntryLoader.resourceBacked(resourceManager), registryManager);
    }

    public static <T> RegistryOps<T> ofLoaded(DynamicOps<T> dynamicOps, RegistryOps.EntryLoader entryLoader, DynamicRegistryManager registryManager) {
        RegistryOps<T> registryOps = new RegistryOps<>(dynamicOps, entryLoader, registryManager, Maps.newIdentityHashMap());
        DynamicRegistryManager.load(registryManager, registryOps);
        return registryOps;
    }

    public static <T> RegistryOps<T> of(DynamicOps<T> delegate, net.minecraft.resource.ResourceManager resourceManager, DynamicRegistryManager registryManager) {
        return of(delegate, RegistryOps.EntryLoader.resourceBacked(resourceManager), registryManager);
    }

    public static <T> RegistryOps<T> of(DynamicOps<T> delegate, RegistryOps.EntryLoader entryLoader, DynamicRegistryManager registryManager) {
        return new RegistryOps<>(delegate, entryLoader, registryManager, Maps.newIdentityHashMap());
    }

    private RegistryOps(DynamicOps<T> delegate, RegistryOps.EntryLoader entryLoader, DynamicRegistryManager registryManager, IdentityHashMap<RegistryKey<? extends Registry<?>>, RegistryOps.ValueHolder<?>> valueHolders) {
        super(delegate);
        this.entryLoader = entryLoader;
        this.registryManager = registryManager;
        this.valueHolders = valueHolders;
        this.entryOps = delegate == JsonOps.INSTANCE ? this : new RegistryOps<>(JsonOps.INSTANCE, entryLoader, registryManager, valueHolders);
    }

    /**
     * Encode an id for a registry element than a full object if possible.
     *
     * <p>This method is called by casting an arbitrary dynamic ops to a registry
     * reading ops.
     *
     * @see RegistryReadingOps#encodeOrId(Object, Object, RegistryKey, Codec)
     */
    protected <E> DataResult<Pair<Supplier<E>, T>> decodeOrId(T object, RegistryKey<? extends Registry<E>> key, Codec<E> codec, boolean allowInlineDefinitions) {
        Optional<MutableRegistry<E>> optional = this.registryManager.getOptionalMutable(key);
        if (optional.isEmpty()) {
            return DataResult.error("Unknown registry: " + key);
        } else {
            MutableRegistry<E> mutableRegistry = optional.get();
            DataResult<Pair<Identifier, T>> dataResult = Identifier.CODEC.decode(this.delegate, object);
            if (dataResult.result().isEmpty()) {
                return !allowInlineDefinitions ? DataResult.error("Inline definitions not allowed here") : codec.decode(this, object).map((pairx) -> pairx.mapFirst((obj) -> () -> obj));
            } else {
                Pair<Identifier, T> pair = dataResult.result().get();
                Identifier identifier = pair.getFirst();
                return this.readSupplier(key, mutableRegistry, codec, identifier).map((supplier) -> Pair.of(supplier, pair.getSecond()));
            }
        }
    }

    /**
     * Loads elements into a registry just loaded from a decoder.
     */
    public <E> DataResult<SimpleRegistry<E>> loadToRegistry(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> key, Codec<E> codec) {
        Collection<Identifier> collection = this.entryLoader.getKnownEntryPaths(key);
        DataResult<SimpleRegistry<E>> dataResult = DataResult.success(registry, Lifecycle.stable());
        String string = key.getValue().getPath() + "/";
        Iterator var7 = collection.iterator();

        while(var7.hasNext()) {
            Identifier identifier = (Identifier)var7.next();
            String string2 = identifier.getPath();
            if (!string2.endsWith(".json")) {
                LOGGER.warn("Skipping resource {} since it is not a json file", identifier);
            } else if (!string2.startsWith(string)) {
                LOGGER.warn("Skipping resource {} since it does not have a registry name prefix", identifier);
            } else {
                String string3 = string2.substring(string.length(), string2.length() - ".json".length());
                Identifier identifier2 = new Identifier(identifier.getNamespace(), string3);
                dataResult = dataResult.flatMap((simpleRegistry) -> {
                    return this.readSupplier(key, simpleRegistry, codec, identifier2).map((supplier) -> {
                        return simpleRegistry;
                    });
                });
            }
        }

        return dataResult.setPartial(registry);
    }

    /**
     * Reads a supplier for a registry element.
     *
     * <p>This logic is used by both {@code decodeOrId} and {@code loadToRegistry}.
     */
    private <E> DataResult<Supplier<E>> readSupplier(RegistryKey<? extends Registry<E>> key, MutableRegistry<E> registry, Codec<E> codec, Identifier elementId) {
        final RegistryKey<E> registryKey = RegistryKey.of(key, elementId);
        RegistryOps.ValueHolder<E> valueHolder = this.getValueHolder(key);
        DataResult<Supplier<E>> dataResult = valueHolder.values.get(registryKey);
        if (dataResult != null) {
            return dataResult;
        } else {
            Supplier<E> supplier = Suppliers.memoize(() -> {
                E object = registry.get(registryKey);
                if (object == null) {
                    throw new RuntimeException("Error during recursive registry parsing, element resolved too early: " + registryKey);
                } else {
                    return object;
                }
            });
            valueHolder.values.put(registryKey, DataResult.success(supplier));
            Optional<DataResult<Pair<E, OptionalInt>>> optional = this.entryLoader.load((DynamicOps<JsonElement>) this.entryOps, key, registryKey, codec);
            DataResult dataResult2;
            if (optional.isEmpty()) {
                dataResult2 = DataResult.success(new Supplier<E>() {
                    public E get() {
                        return registry.get(registryKey);
                    }

                    public String toString() {
                        return registryKey.toString();
                    }
                }, Lifecycle.stable());
            } else {
                DataResult<Pair<E, OptionalInt>> dataResult3 = optional.get();
                Optional<Pair<E, OptionalInt>> optional2 = dataResult3.result();
                if (optional2.isPresent()) {
                    Pair<E, OptionalInt> pair = optional2.get();
                    registry.replace(pair.getSecond(), registryKey, pair.getFirst(), dataResult3.lifecycle());
                }

                dataResult2 = dataResult3.map((pairx) -> registry.get(registryKey));
            }

            valueHolder.values.put(registryKey, dataResult2);
            return dataResult2;
        }
    }

    private <E> RegistryOps.ValueHolder<E> getValueHolder(RegistryKey<? extends Registry<E>> registryRef) {
        return (RegistryOps.ValueHolder)this.valueHolders.computeIfAbsent(registryRef, (registryKey) -> new ValueHolder());
    }

    protected <E> DataResult<Registry<E>> getRegistry(RegistryKey<? extends Registry<E>> key) {
        return (DataResult)this.registryManager.getOptionalMutable(key).map((mutableRegistry) -> DataResult.success(mutableRegistry, mutableRegistry.getLifecycle())).orElseGet(() -> DataResult.error("Unknown registry: " + key));
    }

    public interface EntryLoader {
        /**
         * @return A collection of file Identifiers of all known entries of the given registry.
         * Note that these are file Identifiers for use in a resource manager, not the logical names of the entries.
         */
        Collection<Identifier> getKnownEntryPaths(RegistryKey<? extends Registry<?>> key);

        <E> Optional<DataResult<Pair<E, OptionalInt>>> load(DynamicOps<JsonElement> dynamicOps, RegistryKey<? extends Registry<E>> registryId, RegistryKey<E> entryId, Decoder<E> decoder);

        static RegistryOps.EntryLoader resourceBacked(net.minecraft.resource.ResourceManager resourceManager) {
            return new RegistryOps.EntryLoader() {
                public Collection<Identifier> getKnownEntryPaths(RegistryKey<? extends Registry<?>> key) {
                    return resourceManager.findResources(key.getValue().getPath(), (name) -> {
                        return name.endsWith(".json");
                    });
                }

                public <E> Optional<DataResult<Pair<E, OptionalInt>>> load(DynamicOps<JsonElement> dynamicOps, RegistryKey<? extends Registry<E>> registryId, RegistryKey<E> entryId, Decoder<E> decoder) {
                    Identifier identifier = entryId.getValue();
                    String var10002 = identifier.getNamespace();
                    String var10003 = registryId.getValue().getPath();
                    Identifier identifier2 = new Identifier(var10002, var10003 + "/" + identifier.getPath() + ".json");
                    if (!resourceManager.containsResource(identifier2)) {
                        return Optional.empty();
                    } else {
                        try {
                            Resource resource = resourceManager.getResource(identifier2);

                            Optional var11;
                            try {
                                InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

                                try {
                                    JsonParser jsonParser = new JsonParser();
                                    JsonElement jsonElement = jsonParser.parse(reader);
                                    var11 = Optional.of(decoder.parse(dynamicOps, jsonElement).map((object) -> {
                                        return Pair.of(object, OptionalInt.empty());
                                    }));
                                } catch (Throwable var14) {
                                    try {
                                        reader.close();
                                    } catch (Throwable var13) {
                                        var14.addSuppressed(var13);
                                    }

                                    throw var14;
                                }

                                reader.close();
                            } catch (Throwable var15) {
                                if (resource != null) {
                                    try {
                                        resource.close();
                                    } catch (Throwable var12) {
                                        var15.addSuppressed(var12);
                                    }
                                }

                                throw var15;
                            }

                            EntrustExecution.tryAssertNotNull(resource, Resource::close);

                            return var11;
                        } catch (JsonIOException | JsonSyntaxException | IOException var16) {
                            return Optional.of(DataResult.error("Failed to parse " + identifier2 + " file: " + var16.getMessage()));
                        }
                    }
                }

                public String toString() {
                    return "ResourceAccess[" + resourceManager + "]";
                }
            };
        }

        final class Impl implements RegistryOps.EntryLoader {
            private final Map<RegistryKey<?>, JsonElement> values = Maps.newIdentityHashMap();
            private final Object2IntMap<RegistryKey<?>> entryToRawId = new Object2IntOpenCustomHashMap<>(IdentityHashStrategy.INSTANCE);
            private final Map<RegistryKey<?>, Lifecycle> entryToLifecycle = Maps.newIdentityHashMap();

            public <E> void add(DynamicRegistryManager.Impl registryManager, RegistryKey<E> key, Encoder<E> encoder, int rawId, E entry, Lifecycle lifecycle) {
                DataResult<JsonElement> dataResult = encoder.encodeStart(RegistryReadingOps.of(JsonOps.INSTANCE, registryManager), entry);
                Optional<DataResult.PartialResult<JsonElement>> optional = dataResult.error();
                if (optional.isPresent()) {
                    RegistryOps.LOGGER.error("Error adding element: {}", optional.get().message());
                } else {
                    this.values.put(key, dataResult.result().get());
                    this.entryToRawId.put(key, rawId);
                    this.entryToLifecycle.put(key, lifecycle);
                }
            }

            public Collection<Identifier> getKnownEntryPaths(RegistryKey<? extends Registry<?>> key) {
                return this.values.keySet().stream().filter((registryKey2) -> {
                    return registryKey2.isOf(key);
                }).map((registryKey2) -> {
                    String var10002 = registryKey2.getValue().getNamespace();
                    String var10003 = key.getValue().getPath();
                    return new Identifier(var10002, var10003 + "/" + registryKey2.getValue().getPath() + ".json");
                }).collect(Collectors.toList());
            }

            public <E> Optional<DataResult<Pair<E, OptionalInt>>> load(DynamicOps<JsonElement> dynamicOps, RegistryKey<? extends Registry<E>> registryId, RegistryKey<E> entryId, Decoder<E> decoder) {
                JsonElement jsonElement = this.values.get(entryId);
                return jsonElement == null ? Optional.of(DataResult.error("Unknown element: " + entryId)) : Optional.of(decoder.parse(dynamicOps, jsonElement).setLifecycle(this.entryToLifecycle.get(entryId)).map((object) -> {
                    return Pair.of(object, OptionalInt.of(this.entryToRawId.getInt(entryId)));
                }));
            }
        }
    }

    static final class ValueHolder<E> {
        final Map<RegistryKey<E>, DataResult<Supplier<E>>> values = Maps.newIdentityHashMap();
    }
}
