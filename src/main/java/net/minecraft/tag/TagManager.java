package net.minecraft.tag;

import com.google.common.collect.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.registry.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class TagManager {
    static final Logger LOGGER = LogManager.getLogger();
    public static final TagManager EMPTY = new TagManager(ImmutableMap.of());
    private final Map<RegistryKey<? extends Registry<?>>, TagGroup<?>> tagGroups;

    TagManager(Map<RegistryKey<? extends Registry<?>>, TagGroup<?>> tagGroups) {
        this.tagGroups = tagGroups;
    }

    @Nullable
    private <T> TagGroup<T> getTagGroup(RegistryKey<? extends Registry<T>> registryKey) {
        return (TagGroup<T>) this.tagGroups.get(registryKey);
    }

    public <T> TagGroup<T> getOrCreateTagGroup(RegistryKey<? extends Registry<T>> registryKey) {
        return (TagGroup<T>) this.tagGroups.getOrDefault(registryKey, TagGroup.createEmpty());
    }

    public <T, E extends Exception> Tag<T> getTag(RegistryKey<? extends Registry<T>> registryKey, Identifier id, Function<Identifier, E> exceptionFactory) throws E {
        TagGroup<T> tagGroup = this.getTagGroup(registryKey);
        if (tagGroup == null) {
            throw exceptionFactory.apply(id);
        }
        Tag<T> tag = tagGroup.getTag(id);
        if (tag == null) {
            throw exceptionFactory.apply(id);
        }
        return tag;
    }

    public <T, E extends Exception> Identifier getTagId(RegistryKey<? extends Registry<T>> registryKey, Tag<T> tag, Supplier<E> exceptionSupplier) throws E {
        TagGroup<T> tagGroup = this.getTagGroup(registryKey);
        if (tagGroup == null) {
            throw exceptionSupplier.get();
        }
        Identifier identifier = tagGroup.getUncheckedTagId(tag);
        if (identifier == null) {
            throw exceptionSupplier.get();
        }
        return identifier;
    }

    public void accept(Visitor visitor) {
        this.tagGroups.forEach((type, group) -> TagManager.offerTo(visitor, type, group));
    }

    private static <T> void offerTo(Visitor visitor, RegistryKey<? extends Registry<?>> type, TagGroup<?> group) {
        visitor.visit((RegistryKey<? extends Registry<T>>) type, (TagGroup<T>)group);
    }

    public void apply() {
        RequiredTagListRegistry.updateTagManager(this);
//        Blocks.refreshShapeCache();
    }

    public Map<RegistryKey<? extends Registry<?>>, TagGroup.Serialized> toPacket(final DynamicRegistryManager registryManager) {
        final HashMap<RegistryKey<? extends Registry<?>>, TagGroup.Serialized> map = Maps.newHashMap();
        this.accept(new Visitor(){

            @Override
            public <T> void visit(RegistryKey<? extends Registry<T>> type, TagGroup<T> group) {
                Optional<? extends Registry<T>> optional = registryManager.getOptional(type);
                if (optional.isPresent()) {
                    map.put(type, group.serialize(optional.get()));
                } else {
                    LOGGER.error("Unknown registry {}", type);
                }
            }
        });
        return map;
    }

    public static TagManager fromPacket(DynamicRegistryManager registryManager, Map<RegistryKey<? extends Registry<?>>, TagGroup.Serialized> groups) {
        Builder builder = new Builder();
        groups.forEach((type, group) -> TagManager.tryAdd(registryManager, builder, type, group));
        return builder.build();
    }

    private static <T> void tryAdd(DynamicRegistryManager registryManager, Builder builder, RegistryKey<? extends Registry<? extends T>> type, TagGroup.Serialized group) {
        Optional<? extends Registry<T>> optional = registryManager.getOptional(type);
        if (optional.isPresent()) {
            builder.add(type, TagGroup.deserialize(group, optional.get()));
        } else {
            LOGGER.error("Unknown registry {}", type);
        }
    }

    @FunctionalInterface
    interface Visitor {
        <T> void visit(RegistryKey<? extends Registry<T>> var1, TagGroup<T> var2);
    }

    public static class Builder {
        private final ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, TagGroup<?>> groups = ImmutableMap.builder();

        public <T> Builder add(RegistryKey<? extends Registry<? extends T>> type, TagGroup<T> tagGroup) {
            this.groups.put(type, tagGroup);
            return this;
        }

        public TagManager build() {
            return new TagManager(this.groups.build());
        }
    }
}

