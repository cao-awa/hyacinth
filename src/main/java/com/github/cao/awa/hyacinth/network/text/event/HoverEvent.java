package com.github.cao.awa.hyacinth.network.text.event;

import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.MutableText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.json.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class HoverEvent {
    static final Logger LOGGER = LogManager.getLogger();
    private final Action<?> action;
    private final Object contents;

    public <T> HoverEvent(Action<T> action, T contents) {
        this.action = action;
        this.contents = contents;
    }

    public Action<?> getAction() {
        return this.action;
    }

    @Nullable
    public <T> T getValue(Action<T> action) {
        if (this.action == action) {
            return action.cast(this.contents);
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        HoverEvent hoverEvent = (HoverEvent)o;
        return this.action == hoverEvent.action && Objects.equals(this.contents, hoverEvent.contents);
    }

    public String toString() {
        return "HoverEvent{action=" + this.action + ", value='" + this.contents + "'}";
    }

    public int hashCode() {
        int i = this.action.hashCode();
        i = 31 * i + (this.contents != null ? this.contents.hashCode() : 0);
        return i;
    }

    @Nullable
    public static HoverEvent fromJson(JsonObject json) {
        String string = JsonHelper.getString(json, "action", null);
        if (string == null) {
            return null;
        }
        Action<?> action = Action.byName(string);
        if (action == null) {
            return null;
        }
        JsonElement jsonElement = json.get("contents");
        if (jsonElement != null) {
            return action.buildHoverEvent(jsonElement);
        }
        MutableText text = Text.Serializer.fromJson(json.get("value"));
        if (text != null) {
            return action.buildHoverEvent(text);
        }
        return null;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", this.action.getName());
        jsonObject.add("contents", this.action.contentsToJson(this.contents));
        return jsonObject;
    }

    public static class Action<T> {
        // TODO: 2022/4/22
        public static final Action<Text> SHOW_TEXT = new Action<Text>("show_text", true, Text.Serializer::fromJson, Text.Serializer::toJsonTree, Function.identity());
//        public static final Action<ItemStackContent> SHOW_ITEM = new Action<ItemStackContent>("show_item", true, ItemStackContent::parse, ItemStackContent::toJson, ItemStackContent::parse);
//        public static final Action<EntityContent> SHOW_ENTITY = new Action<EntityContent>("show_entity", true, EntityContent::parse, EntityContent::toJson, EntityContent::parse);
//        private static final Map<String, Action<?>> BY_NAME = Stream.of(SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY).collect(ImmutableMap.toImmutableMap(Action::getName, action -> action));
        private final String name;
        private final boolean parsable;
        private final Function<JsonElement, T> deserializer;
        private final Function<T, JsonElement> serializer;
        private final Function<Text, T> legacyDeserializer;

        public Action(String name, boolean parsable, Function<JsonElement, T> deserializer, Function<T, JsonElement> serializer, Function<Text, T> legacyDeserializer) {
            this.name = name;
            this.parsable = parsable;
            this.deserializer = deserializer;
            this.serializer = serializer;
            this.legacyDeserializer = legacyDeserializer;
        }

        public boolean isParsable() {
            return this.parsable;
        }

        public String getName() {
            return this.name;
        }

        // TODO: 2022/4/22
        @Nullable
        public static Action<?> byName(String name) {
            return null;
//            return BY_NAME.get(name);
        }

        T cast(Object o) {
            return (T)o;
        }

        @Nullable
        public HoverEvent buildHoverEvent(JsonElement contents) {
            T object = this.deserializer.apply(contents);
            if (object == null) {
                return null;
            }
            return new HoverEvent(this, object);
        }

        @Nullable
        public HoverEvent buildHoverEvent(Text value) {
            T object = this.legacyDeserializer.apply(value);
            if (object == null) {
                return null;
            }
            return new HoverEvent(this, object);
        }

        public JsonElement contentsToJson(Object contents) {
            return this.serializer.apply(this.cast(contents));
        }

        public String toString() {
            return "<action " + this.name + ">";
        }
    }

    // TODO: 2022/4/22
//    public static class ItemStackContent {
//        private final Item item;
//        private final int count;
//        @Nullable
//        private final NbtCompound nbt;
//        @Nullable
//        private ItemStack stack;
//
//        ItemStackContent(Item item, int count, @Nullable NbtCompound nbt) {
//            this.item = item;
//            this.count = count;
//            this.nbt = nbt;
//        }
//
//        public ItemStackContent(ItemStack stack) {
//            this(stack.getItem(), stack.getCount(), stack.getNbt() != null ? stack.getNbt().copy() : null);
//        }
//
//        public boolean equals(Object o) {
//            if (this == o) {
//                return true;
//            }
//            if (o == null || this.getClass() != o.getClass()) {
//                return false;
//            }
//            ItemStackContent itemStackContent = (ItemStackContent)o;
//            return this.count == itemStackContent.count && this.item.equals(itemStackContent.item) && Objects.equals(this.nbt, itemStackContent.nbt);
//        }
//
//        public int hashCode() {
//            int i = this.item.hashCode();
//            i = 31 * i + this.count;
//            i = 31 * i + (this.nbt != null ? this.nbt.hashCode() : 0);
//            return i;
//        }
//
//        public ItemStack asStack() {
//            if (this.stack == null) {
//                this.stack = new ItemStack(this.item, this.count);
//                if (this.nbt != null) {
//                    this.stack.setNbt(this.nbt);
//                }
//            }
//            return this.stack;
//        }
//
//        private static ItemStackContent parse(JsonElement json) {
//            if (json.isJsonPrimitive()) {
//                return new ItemStackContent(Registry.ITEM.get(new Identifier(json.getAsString())), 1, null);
//            }
//            JsonObject jsonObject = JsonHelper.asObject(json, "item");
//            Item item = Registry.ITEM.get(new Identifier(JsonHelper.getString(jsonObject, "id")));
//            int i = JsonHelper.getInt(jsonObject, "count", 1);
//            if (jsonObject.has("tag")) {
//                String string = JsonHelper.getString(jsonObject, "tag");
//                try {
//                    NbtCompound nbtCompound = StringNbtReader.parse(string);
//                    return new ItemStackContent(item, i, nbtCompound);
//                }
//                catch (CommandSyntaxException nbtCompound) {
//                    LOGGER.warn("Failed to parse tag: {}", (Object)string, (Object)nbtCompound);
//                }
//            }
//            return new ItemStackContent(item, i, null);
//        }
//
//        @Nullable
//        private static ItemStackContent parse(Text text) {
//            try {
//                NbtCompound nbtCompound = StringNbtReader.parse(text.getString());
//                return new ItemStackContent(ItemStack.fromNbt(nbtCompound));
//            }
//            catch (CommandSyntaxException nbtCompound) {
//                LOGGER.warn("Failed to parse item tag: {}", (Object)text, (Object)nbtCompound);
//                return null;
//            }
//        }
//
//        private JsonElement toJson() {
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("id", Registry.ITEM.getId(this.item).toString());
//            if (this.count != 1) {
//                jsonObject.addProperty("count", this.count);
//            }
//            if (this.nbt != null) {
//                jsonObject.addProperty("tag", this.nbt.toString());
//            }
//            return jsonObject;
//        }
//    }

//    public static class EntityContent {
//        public final EntityType<?> entityType;
//        public final UUID uuid;
//        @Nullable
//        public final Text name;
//        @Nullable
//        private List<Text> tooltip;
//
//        public EntityContent(EntityType<?> entityType, UUID uuid, @Nullable Text name) {
//            this.entityType = entityType;
//            this.uuid = uuid;
//            this.name = name;
//        }
//
//        @Nullable
//        public static EntityContent parse(JsonElement json) {
//            if (!json.isJsonObject()) {
//                return null;
//            }
//            JsonObject jsonObject = json.getAsJsonObject();
//            EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(jsonObject, "type")));
//            UUID uUID = UUID.fromString(JsonHelper.getString(jsonObject, "id"));
//            MutableText text = Text.Serializer.fromJson(jsonObject.get("name"));
//            return new EntityContent(entityType, uUID, text);
//        }
//
//        @Nullable
//        public static EntityContent parse(Text text) {
//            try {
//                NbtCompound nbtCompound = StringNbtReader.parse(text.getString());
//                MutableText text2 = Text.Serializer.fromJson(nbtCompound.getString("name"));
//                EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(nbtCompound.getString("type")));
//                UUID uUID = UUID.fromString(nbtCompound.getString("id"));
//                return new EntityContent(entityType, uUID, text2);
//            }
//            catch (JsonSyntaxException | CommandSyntaxException nbtCompound) {
//                return null;
//            }
//        }
//
//        public JsonElement toJson() {
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("type", Registry.ENTITY_TYPE.getId(this.entityType).toString());
//            jsonObject.addProperty("id", this.uuid.toString());
//            if (this.name != null) {
//                jsonObject.add("name", Text.Serializer.toJsonTree(this.name));
//            }
//            return jsonObject;
//        }
//
//        public List<Text> asTooltip() {
//            if (this.tooltip == null) {
//                this.tooltip = Lists.newArrayList();
//                if (this.name != null) {
//                    this.tooltip.add(this.name);
//                }
//                this.tooltip.add(new TranslatableText("gui.entity_tooltip.type", this.entityType.getName()));
//                this.tooltip.add(new LiteralText(this.uuid.toString()));
//            }
//            return this.tooltip;
//        }
//
//        public boolean equals(Object o) {
//            if (this == o) {
//                return true;
//            }
//            if (o == null || this.getClass() != o.getClass()) {
//                return false;
//            }
//            EntityContent entityContent = (EntityContent)o;
//            return this.entityType.equals(entityContent.entityType) && this.uuid.equals(entityContent.uuid) && Objects.equals(this.name, entityContent.name);
//        }
//
//        public int hashCode() {
//            int i = this.entityType.hashCode();
//            i = 31 * i + this.uuid.hashCode();
//            i = 31 * i + (this.name != null ? this.name.hashCode() : 0);
//            return i;
//        }
//    }
}
