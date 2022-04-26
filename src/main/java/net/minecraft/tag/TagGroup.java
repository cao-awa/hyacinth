package net.minecraft.tag;

import com.github.cao.awa.hyacinth.network.packet.buf.*;
import com.google.common.collect.*;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Contains the set of tags all of the same type.
 */
public interface TagGroup<T> {
    Map<Identifier, Tag<T>> getTags();

    @Nullable
    default Tag<T> getTag(Identifier id) {
        return this.getTags().get(id);
    }

    Tag<T> getTagOrEmpty(Identifier var1);

    @Nullable
    default Identifier getId(Tag.Identified<T> tag) {
        return tag.getId();
    }

    @Nullable Identifier getUncheckedTagId(Tag<T> var1);

    default boolean contains(Identifier id) {
        return this.getTags().containsKey(id);
    }

    default Collection<Identifier> getTagIds() {
        return this.getTags().keySet();
    }

    /**
     * Gets the identifiers of all tags an object is applicable to.
     */
    default Collection<Identifier> getTagsFor(T object) {
        ArrayList<Identifier> list = Lists.newArrayList();
        for (Map.Entry<Identifier, Tag<T>> entry : this.getTags().entrySet()) {
            if (!entry.getValue().contains(object)) continue;
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * Serializes this tag group.
     */
    default Serialized serialize(Registry<T> registry) {
        Map<Identifier, Tag<T>> map = this.getTags();
        HashMap<Identifier, IntList> map2 = Maps.newHashMapWithExpectedSize(map.size());
        map.forEach((id, tag) -> {
            List<T> list = tag.values();
            IntArrayList intList = new IntArrayList(list.size());
            for (T object : list) {
                intList.add(registry.getRawId(object));
            }
            map2.put(id, intList);
        });
        return new Serialized(map2);
    }

    /**
     * Deserializes a serialized tag group.
     */
    static <T> TagGroup<T> deserialize(Serialized serialized, Registry<? extends T> registry) {
        HashMap map = Maps.newHashMapWithExpectedSize(serialized.contents.size());
        serialized.contents.forEach((id, entries) -> {
            ImmutableSet.Builder builder = ImmutableSet.builder();
            IntListIterator intListIterator = entries.iterator();
            while (intListIterator.hasNext()) {
                int i = intListIterator.next();
                builder.add(registry.get(i));
            }
            map.put(id, Tag.of(builder.build()));
        });
        return TagGroup.create(map);
    }

    static <T> TagGroup<T> createEmpty() {
        return TagGroup.create(ImmutableBiMap.of());
    }

    static <T> TagGroup<T> create(Map<Identifier, Tag<T>> tags) {
        final ImmutableBiMap<Identifier, Tag<T>> biMap = ImmutableBiMap.copyOf(tags);
        return new TagGroup<T>(){
            private final Tag<T> emptyTag = SetTag.empty();

            @Override
            public Tag<T> getTagOrEmpty(Identifier id) {
                return biMap.getOrDefault(id, this.emptyTag);
            }

            @Override
            @Nullable
            public Identifier getUncheckedTagId(Tag<T> tag) {
                if (tag instanceof Tag.Identified) {
                    return ((Tag.Identified)tag).getId();
                }
                return biMap.inverse().get(tag);
            }

            @Override
            public Map<Identifier, Tag<T>> getTags() {
                return biMap;
            }
        };
    }

    class Serialized {
        final Map<Identifier, IntList> contents;

        Serialized(Map<Identifier, IntList> contents) {
            this.contents = contents;
        }

        public void writeBuf(PacketByteBuf buf) {
            buf.writeMap(this.contents, PacketByteBuf::writeIdentifier, PacketByteBuf::writeIntList);
        }

        public static Serialized fromBuf(PacketByteBuf buf) {
            return new Serialized(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readIntList));
        }
    }
}

