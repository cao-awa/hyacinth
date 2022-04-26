package net.minecraft.util.registry;

import com.github.cao.awa.hyacinth.identity.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.identifier.*;
import org.apache.commons.lang3.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleRegistry<T>
extends MutableRegistry<T> {
    protected static final Logger LOGGER = LogManager.getLogger();
    private final ObjectList<T> rawIdToEntry = new ObjectArrayList<T>(256);
    private final Object2IntMap<T> entryToRawId = EntrustParser.operation(new Object2IntOpenCustomHashMap<>(IdentityHashStrategy.INSTANCE), object2IntOpenCustomHashMap -> object2IntOpenCustomHashMap.defaultReturnValue(-1));
    private final BiMap<Identifier, T> idToEntry = HashBiMap.create();
    private final BiMap<RegistryKey<T>, T> keyToEntry = HashBiMap.create();
    private final Map<T, Lifecycle> entryToLifecycle = Maps.newIdentityHashMap();
    private Lifecycle lifecycle;
    @Nullable
    protected Object[] randomEntries;
    private int nextId;

    public SimpleRegistry(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
        this.lifecycle = lifecycle;
    }

    public static <T> MapCodec<RegistryManagerEntry<T>> createRegistryManagerEntryCodec(RegistryKey<? extends Registry<T>> key, MapCodec<T> entryCodec) {
        return RecordCodecBuilder.mapCodec(instance -> {
            return instance.group((Identifier.CODEC.xmap(
                            RegistryKey.createKeyFactory(key), RegistryKey::getValue).fieldOf("name")).forGetter(RegistryManagerEntry::key),
                    (Codec.INT.fieldOf("id")).forGetter(RegistryManagerEntry::rawId), entryCodec.forGetter(RegistryManagerEntry::entry)).apply(instance, RegistryManagerEntry::new);
        });
    }

    @Override
    public <V extends T> V set(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle) {
        return this.set(rawId, key, entry, lifecycle, true);
    }

    private <V extends T> V set(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle, boolean checkDuplicateKeys) {
        Validate.notNull(key);
        Validate.notNull(entry);
        this.rawIdToEntry.size(Math.max(this.rawIdToEntry.size(), rawId + 1));
        this.rawIdToEntry.set(rawId, entry);
        this.entryToRawId.put((T)entry, rawId);
        this.randomEntries = null;
        if (checkDuplicateKeys && this.keyToEntry.containsKey(key)) {
            EntrustExecution.error("Adding duplicate key '" + key + "' to registry");
        }
        if (this.idToEntry.containsValue(entry)) {
            EntrustExecution.error("Adding duplicate value '" + entry + "' to registry");
        }
        this.idToEntry.put(key.getValue(), entry);
        this.keyToEntry.put(key, entry);
        this.entryToLifecycle.put(entry, lifecycle);
        this.lifecycle = this.lifecycle.add(lifecycle);
        if (this.nextId <= rawId) {
            this.nextId = rawId + 1;
        }
        return entry;
    }

    @Override
    public <V extends T> V add(RegistryKey<T> key, V entry, Lifecycle lifecycle) {
        return this.set(this.nextId, key, entry, lifecycle);
    }

    @Override
    public <V extends T> V replace(OptionalInt rawId, RegistryKey<T> key, V newEntry, Lifecycle lifecycle) {
        int i;
        Validate.notNull(key);
        Validate.notNull(newEntry);
        Object object = this.keyToEntry.get(key);
        if (object == null) {
            i = rawId.isPresent() ? rawId.getAsInt() : this.nextId;
        } else {
            i = this.entryToRawId.getInt(object);
            if (rawId.isPresent() && rawId.getAsInt() != i) {
                throw new IllegalStateException("ID mismatch");
            }
            this.entryToRawId.removeInt(object);
            this.entryToLifecycle.remove(object);
        }
        return this.set(i, key, newEntry, lifecycle, false);
    }

    @Override
    @Nullable
    public Identifier getId(T entry) {
        return (Identifier)this.idToEntry.inverse().get(entry);
    }

    @Override
    public Optional<RegistryKey<T>> getKey(T entry) {
        return Optional.ofNullable((RegistryKey)this.keyToEntry.inverse().get(entry));
    }

    @Override
    public int getRawId(@Nullable T entry) {
        return this.entryToRawId.getInt(entry);
    }

    @Override
    @Nullable
    public T get(@Nullable RegistryKey<T> key) {
        return (T)this.keyToEntry.get(key);
    }

    @Override
    @Nullable
    public T get(int index) {
        if (index < 0 || index >= this.rawIdToEntry.size()) {
            return null;
        }
        return (T)this.rawIdToEntry.get(index);
    }

    @Override
    public int size() {
        return this.idToEntry.size();
    }

    @Override
    public Lifecycle getEntryLifecycle(T entry) {
        return this.entryToLifecycle.get(entry);
    }

    @Override
    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.filter(this.rawIdToEntry.iterator(), Objects::nonNull);
    }

    @Override
    @Nullable
    public T get(@Nullable Identifier id) {
        return (T)this.idToEntry.get(id);
    }

    @Override
    public Set<Identifier> getIds() {
        return Collections.unmodifiableSet(this.idToEntry.keySet());
    }

    @Override
    public Set<Map.Entry<RegistryKey<T>, T>> getEntries() {
        return Collections.unmodifiableMap(this.keyToEntry).entrySet();
    }

    @Override
    public boolean isEmpty() {
        return this.idToEntry.isEmpty();
    }

    @Override
    @Nullable
    public T getRandom(Random random) {
        if (this.randomEntries == null) {
            Collection<T> collection = this.idToEntry.values();
            if (collection.isEmpty()) {
                return null;
            }
            this.randomEntries = collection.toArray(Object[]::new);
        }
        return (T)EntrustParser.select(this.randomEntries, random);
    }

    @Override
    public boolean containsId(Identifier id) {
        return this.idToEntry.containsKey(id);
    }

    @Override
    public boolean contains(RegistryKey<T> key) {
        return this.keyToEntry.containsKey(key);
    }

    public static <T> Codec<SimpleRegistry<T>> createRegistryManagerCodec(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, Codec<T> entryCodec) {
        return SimpleRegistry.createRegistryManagerEntryCodec(key, entryCodec.fieldOf("element")).codec().listOf().xmap(list -> {
            SimpleRegistry<T> simpleRegistry = new SimpleRegistry<>(key, lifecycle);
            for (RegistryManagerEntry<T> registryManagerEntry : list) {
                simpleRegistry.set(registryManagerEntry.rawId(), registryManagerEntry.key(), registryManagerEntry.entry(), lifecycle);
            }
            return simpleRegistry;
        }, simpleRegistry -> {
            ImmutableList.Builder<RegistryManagerEntry<T>> builder = ImmutableList.builder();
            for (T object : simpleRegistry) {
                builder.add(new RegistryManagerEntry<>(simpleRegistry.getKey(object).get(), simpleRegistry.getRawId(object), object));
            }
            return builder.build();
        });
    }

    public static <T> Codec<SimpleRegistry<T>> createRegistryCodec(RegistryKey<? extends Registry<T>> registryRef, Lifecycle lifecycle, Codec<T> entryCodec) {
        return RegistryCodec.of(registryRef, lifecycle, entryCodec);
    }

    public static <T> Codec<SimpleRegistry<T>> createCodec(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, Codec<T> entryCodec) {
        return Codec.unboundedMap(Identifier.CODEC.xmap(RegistryKey.createKeyFactory(key), RegistryKey::getValue), entryCodec).xmap(map -> {
            SimpleRegistry<T> simpleRegistry = new SimpleRegistry<>(key, lifecycle);
            map.forEach((registryKey, object) -> simpleRegistry.add(registryKey, object, lifecycle));
            return simpleRegistry;
        }, simpleRegistry -> ImmutableMap.copyOf(simpleRegistry.keyToEntry));
    }

    record RegistryManagerEntry<T>(RegistryKey<T> key, int rawId, T entry) {
    }
}

