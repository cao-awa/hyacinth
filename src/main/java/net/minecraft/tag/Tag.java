package net.minecraft.tag;

import com.google.common.collect.*;
import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.json.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A tag is a set of objects.
 * 
 * <p>Tags simplifies reference to multiple objects, especially for
 * predicate (testing against) purposes.
 * 
 * <p>A tag is immutable by design. It has a builder, which is a mutable
 * equivalent.
 * 
 * <p>Its entries' iteration may be ordered
 * or unordered, depending on the configuration from the tag builder.
 */
public interface Tag<T> {
    static <T> Codec<Tag<T>> codec(Supplier<TagGroup<T>> groupGetter) {
        return Identifier.CODEC.flatXmap(id -> Optional.ofNullable((groupGetter.get()).getTag(id)).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown tag: " + id)), tag -> Optional.ofNullable(groupGetter.get().getUncheckedTagId(tag)).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown tag: " + tag)));
    }

    boolean contains(T var1);

    List<T> values();

    default T getRandom(Random random) {
        List<T> list = this.values();
        return list.get(random.nextInt(list.size()));
    }

    static <T> Tag<T> of(Set<T> values) {
        return SetTag.of(values);
    }

    interface Identified<T>
    extends Tag<T> {
        Identifier getId();
    }

    class OptionalTagEntry
    implements Entry {
        private final Identifier id;

        public OptionalTagEntry(Identifier id) {
            this.id = id;
        }

        @Override
        public <T> boolean resolve(Function<Identifier, Tag<T>> tagGetter, Function<Identifier, T> objectGetter, Consumer<T> collector) {
            Tag<T> tag = tagGetter.apply(this.id);
            if (tag != null) {
                tag.values().forEach(collector);
            }
            return true;
        }

        @Override
        public void addToJson(JsonArray json) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "#" + this.id);
            jsonObject.addProperty("required", false);
            json.add(jsonObject);
        }

        public String toString() {
            return "#" + this.id + "?";
        }

        @Override
        public void forEachGroupId(Consumer<Identifier> consumer) {
            consumer.accept(this.id);
        }

        @Override
        public boolean canAdd(Predicate<Identifier> existenceTest, Predicate<Identifier> duplicationTest) {
            return true;
        }
    }

    class TagEntry
    implements Entry {
        private final Identifier id;

        public TagEntry(Identifier id) {
            this.id = id;
        }

        @Override
        public <T> boolean resolve(Function<Identifier, Tag<T>> tagGetter, Function<Identifier, T> objectGetter, Consumer<T> collector) {
            Tag<T> tag = tagGetter.apply(this.id);
            if (tag == null) {
                return false;
            }
            tag.values().forEach(collector);
            return true;
        }

        @Override
        public void addToJson(JsonArray json) {
            json.add("#" + this.id);
        }

        public String toString() {
            return "#" + this.id;
        }

        @Override
        public boolean canAdd(Predicate<Identifier> existenceTest, Predicate<Identifier> duplicationTest) {
            return duplicationTest.test(this.id);
        }

        @Override
        public void forEachTagId(Consumer<Identifier> consumer) {
            consumer.accept(this.id);
        }
    }

    class OptionalObjectEntry
    implements Entry {
        private final Identifier id;

        public OptionalObjectEntry(Identifier id) {
            this.id = id;
        }

        @Override
        public <T> boolean resolve(Function<Identifier, Tag<T>> tagGetter, Function<Identifier, T> objectGetter, Consumer<T> collector) {
            T object = objectGetter.apply(this.id);
            if (object != null) {
                collector.accept(object);
            }
            return true;
        }

        @Override
        public void addToJson(JsonArray json) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", this.id.toString());
            jsonObject.addProperty("required", false);
            json.add(jsonObject);
        }

        @Override
        public boolean canAdd(Predicate<Identifier> existenceTest, Predicate<Identifier> duplicationTest) {
            return true;
        }

        public String toString() {
            return this.id + "?";
        }
    }

    class ObjectEntry
    implements Entry {
        private final Identifier id;

        public ObjectEntry(Identifier id) {
            this.id = id;
        }

        @Override
        public <T> boolean resolve(Function<Identifier, Tag<T>> tagGetter, Function<Identifier, T> objectGetter, Consumer<T> collector) {
            T object = objectGetter.apply(this.id);
            if (object == null) {
                return false;
            }
            collector.accept(object);
            return true;
        }

        @Override
        public void addToJson(JsonArray json) {
            json.add(this.id.toString());
        }

        @Override
        public boolean canAdd(Predicate<Identifier> existenceTest, Predicate<Identifier> duplicationTest) {
            return existenceTest.test(this.id);
        }

        public String toString() {
            return this.id.toString();
        }
    }

    interface Entry {
        <T> boolean resolve(Function<Identifier, Tag<T>> var1, Function<Identifier, T> var2, Consumer<T> var3);

        void addToJson(JsonArray var1);

        default void forEachTagId(Consumer<Identifier> consumer) {
        }

        default void forEachGroupId(Consumer<Identifier> consumer) {
        }

        boolean canAdd(Predicate<Identifier> var1, Predicate<Identifier> var2);
    }

    class Builder {
        private final List<TrackedEntry> entries = Lists.newArrayList();

        public static Builder create() {
            return new Builder();
        }

        public Builder add(TrackedEntry trackedEntry) {
            this.entries.add(trackedEntry);
            return this;
        }

        public Builder add(Entry entry, String source) {
            return this.add(new TrackedEntry(entry, source));
        }

        public Builder add(Identifier id, String source) {
            return this.add(new ObjectEntry(id), source);
        }

        public Builder addOptional(Identifier id, String source) {
            return this.add(new OptionalObjectEntry(id), source);
        }

        public Builder addTag(Identifier id, String source) {
            return this.add(new TagEntry(id), source);
        }

        public Builder addOptionalTag(Identifier id, String source) {
            return this.add(new OptionalTagEntry(id), source);
        }

        public <T> Either<Collection<TrackedEntry>, Tag<T>> build(Function<Identifier, Tag<T>> tagGetter, Function<Identifier, T> objectGetter) {
            ImmutableSet.Builder<T> builder = ImmutableSet.builder();
            ArrayList<TrackedEntry> list = Lists.newArrayList();
            for (TrackedEntry trackedEntry : this.entries) {
                if (trackedEntry.getEntry().resolve(tagGetter, objectGetter, builder::add)) continue;
                list.add(trackedEntry);
            }
            return list.isEmpty() ? Either.right(Tag.of(builder.build())) : Either.left(list);
        }

        public Stream<TrackedEntry> streamEntries() {
            return this.entries.stream();
        }

        public void forEachTagId(Consumer<Identifier> consumer) {
            this.entries.forEach(trackedEntry -> trackedEntry.entry.forEachTagId(consumer));
        }

        public void forEachGroupId(Consumer<Identifier> consumer) {
            this.entries.forEach(trackedEntry -> trackedEntry.entry.forEachGroupId(consumer));
        }

        public Builder read(JsonObject json, String source) {
            JsonArray jsonArray = JsonHelper.getArray(json, "values");
            ArrayList<Entry> list = Lists.newArrayList();
            for (JsonElement jsonElement : jsonArray) {
                list.add(Builder.resolveEntry(jsonElement));
            }
            if (JsonHelper.getBoolean(json, "replace", false)) {
                this.entries.clear();
            }
            list.forEach(entry -> this.entries.add(new TrackedEntry(entry, source)));
            return this;
        }

        private static Entry resolveEntry(JsonElement json) {
            boolean bl;
            String string;
            Object jsonObject;
            if (json.isJsonObject()) {
                jsonObject = json.getAsJsonObject();
                string = JsonHelper.getString((JsonObject)jsonObject, "id");
                bl = JsonHelper.getBoolean((JsonObject)jsonObject, "required", true);
            } else {
                string = JsonHelper.asString(json, "id");
                bl = true;
            }
            if (string.startsWith("#")) {
                jsonObject = new Identifier(string.substring(1));
                return bl ? new TagEntry((Identifier)jsonObject) : new OptionalTagEntry((Identifier)jsonObject);
            }
            jsonObject = new Identifier(string);
            return bl ? new ObjectEntry((Identifier)jsonObject) : new OptionalObjectEntry((Identifier)jsonObject);
        }

        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            for (TrackedEntry trackedEntry : this.entries) {
                trackedEntry.getEntry().addToJson(jsonArray);
            }
            jsonObject.addProperty("replace", false);
            jsonObject.add("values", jsonArray);
            return jsonObject;
        }
    }

    class TrackedEntry {
        final Entry entry;
        private final String source;

        TrackedEntry(Entry entry, String source) {
            this.entry = entry;
            this.source = source;
        }

        public Entry getEntry() {
            return this.entry;
        }

        public String getSource() {
            return this.source;
        }

        public String toString() {
            return this.entry + " (from " + this.source + ")";
        }
    }
}

