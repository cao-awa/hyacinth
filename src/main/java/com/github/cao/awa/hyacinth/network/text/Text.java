package com.github.cao.awa.hyacinth.network.text;

import com.github.cao.awa.hyacinth.network.text.style.Style;
import com.github.cao.awa.hyacinth.network.text.visitor.StringVisitable;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustParser;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.Message;
import net.minecraft.util.enums.LowercaseEnumTypeAdapterFactory;
import net.minecraft.util.json.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A text. Can be converted to and from JSON format.
 *
 * <p>Each text has a tree structure, embodying all its {@link
 * #getSiblings() siblings}. To iterate contents in the text and all
 * its siblings, call {@code visit} methods.
 *
 * <p>This interface does not expose mutation operations. For mutation,
 * refer to {@link MutableText}.
 *
 * @see MutableText
 */
public interface Text extends Message, StringVisitable {
    /**
     * Creates a literal text with the given string as content.
     */
    static Text of(@Nullable String string) {
        return string != null ? new LiteralText(string) : LiteralText.EMPTY;
    }

    /**
     * Returns the style of this text.
     */
    Style getStyle();

    JsonObject toJSONObject();

    /**
     * Returns the string representation of this text itself, excluding siblings.
     */
    String asString();

    @Override
    default String getString() {
        return StringVisitable.super.getString();
    }

    /**
     * Returns the full string representation of this text, truncated beyond
     * the supplied {@code length}.
     *
     * @param length
     *         the max length allowed for the string representation of the text
     */
    default String asTruncatedString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        this.visit(string -> {
            int j = length - stringBuilder.length();
            if (j <= 0) {
                return TERMINATE_VISIT;
            }
            stringBuilder.append(string.length() <= j ? string : string.substring(0, j));
            return Optional.empty();
        });
        return stringBuilder.toString();
    }

    /**
     * Returns the siblings of this text.
     */
    List<Text> getSiblings();

    /**
     * Copies the text itself, excluding the styles or siblings.
     */
    MutableText copy();

    /**
     * Copies the text itself, the style, and the siblings.
     *
     * <p>A shallow copy is made for the siblings.
     */
    MutableText shallowCopy();

    OrderedText asOrderedText();

    @Override
    default <T> Optional<T> visit(StringVisitable.Visitor<T> visitor) {
        Optional<T> optional = this.visitSelf(visitor);
        if (optional.isPresent()) {
            return optional;
        }
        for (Text text : this.getSiblings()) {
            Optional<T> optional2 = text.visit(visitor);
            if (! optional2.isPresent())
                continue;
            return optional2;
        }
        return Optional.empty();
    }

    @Override
    default <T> Optional<T> visit(StringVisitable.StyledVisitor<T> styledVisitor, Style style) {
        Style style2 = this.getStyle().withParent(style);
        Optional<T> optional = this.visitSelf(styledVisitor, style2);
        if (optional.isPresent()) {
            return optional;
        }
        for (Text text : this.getSiblings()) {
            Optional<T> optional2 = text.visit(styledVisitor, style2);
            if (! optional2.isPresent())
                continue;
            return optional2;
        }
        return Optional.empty();
    }

    /**
     * Visits the text itself.
     *
     * @param visitor
     *         the visitor
     * @param style
     *         the current style
     * @return the visitor's return value
     * @see #visit(StyledVisitor, Style)
     */
    default <T> Optional<T> visitSelf(StringVisitable.StyledVisitor<T> visitor, Style style) {
        return visitor.accept(style, this.asString());
    }

    /**
     * Visits the text itself.
     *
     * @param visitor
     *         the visitor
     * @return the visitor's return value
     * @see #visit(Visitor)
     */
    default <T> Optional<T> visitSelf(StringVisitable.Visitor<T> visitor) {
        return visitor.accept(this.asString());
    }

    default List<Text> getWithStyle(Style style) {
        ArrayList<Text> list = Lists.newArrayList();
        this.visit((styleOverride, text) -> {
            if (! text.isEmpty()) {
                list.add(new LiteralText(text).fillStyle(styleOverride));
            }
            return Optional.empty();
        }, style);
        return list;
    }

    class Serializer implements JsonDeserializer<MutableText>, JsonSerializer<Text> {
        private static final Gson GSON = EntrustParser.operation(() -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.disableHtmlEscaping();
            gsonBuilder.registerTypeHierarchyAdapter(Text.class, new Serializer());
            gsonBuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
            gsonBuilder.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory());
            return gsonBuilder.create();
        });
        private static final Field JSON_READER_POS = EntrustParser.operation(() -> {
            try {
                new JsonReader(new StringReader(""));
                Field field = JsonReader.class.getDeclaredField("pos");
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException field) {
                throw new IllegalStateException("Couldn't get field 'pos' for JsonReader", field);
            }
        });
        private static final Field JSON_READER_LINE_START = EntrustParser.operation(() -> {
            try {
                new JsonReader(new StringReader(""));
                Field field = JsonReader.class.getDeclaredField("lineStart");
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException field) {
                throw new IllegalStateException("Couldn't get field 'lineStart' for JsonReader", field);
            }
        });

        public static String toJson(Text text) {
            return GSON.toJson(text);
        }

        public static JsonElement toJsonTree(Text text) {
            return GSON.toJsonTree(text);
        }

        @Nullable
        public static MutableText fromJson(String json) {
            return JsonHelper.deserialize(GSON, json, MutableText.class, false);
        }

        @Nullable
        public static MutableText fromJson(JsonElement json) {
            return GSON.fromJson(json, MutableText.class);
        }

        @Nullable
        public static MutableText fromLenientJson(String json) {
            return JsonHelper.deserialize(GSON, json, MutableText.class, true);
        }

        public static MutableText fromJson(com.mojang.brigadier.StringReader reader) {
            try {
                JsonReader jsonReader = new JsonReader(new StringReader(reader.getRemaining()));
                jsonReader.setLenient(false);
                MutableText mutableText = GSON.getAdapter(MutableText.class).read(jsonReader);
                reader.setCursor(reader.getCursor() + Serializer.getPosition(jsonReader));
                return mutableText;
            } catch (IOException | StackOverflowError jsonReader) {
                throw new JsonParseException(jsonReader);
            }
        }

        private static int getPosition(JsonReader reader) {
            try {
                return JSON_READER_POS.getInt(reader) - JSON_READER_LINE_START.getInt(reader) + 1;
            } catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException("Couldn't read position of JsonReader", illegalAccessException);
            }
        }

        /*
         * WARNING - void declaration
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public MutableText deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            MutableText var5_19 = null;
            if (jsonElement.isJsonPrimitive()) {
                return new LiteralText(jsonElement.getAsString());
            }
            if (jsonElement.isJsonObject()) {
                MutableText var5_17 = new LiteralText("");
                Object string;
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("text")) {
                    LiteralText literalText = new LiteralText(JsonHelper.getString(jsonObject, "text"));
                } else if (jsonObject.has("translate")) {
                    string = JsonHelper.getString(jsonObject, "translate");
                    if (jsonObject.has("with")) {
                        JsonArray jsonArray = JsonHelper.getArray(jsonObject, "with");
                        Object[] objects = new Object[jsonArray.size()];
                        for (int i = 0; i < objects.length; ++ i) {
                            LiteralText literalText;
                            objects[i] = this.deserialize(jsonArray.get(i), type, jsonDeserializationContext);
                            if (! (objects[i] instanceof LiteralText) || ! (literalText = (LiteralText) objects[i]).getStyle().isEmpty() || ! literalText.getSiblings().isEmpty())
                                continue;
                            objects[i] = literalText.getRawString();
                        }
//                        TranslatableText translatableText = new TranslatableText((String) string, objects);
                    } else {
//                        TranslatableText translatableText = new TranslatableText((String) string);
                    }
                } else if (jsonObject.has("score")) {
                    string = JsonHelper.getObject(jsonObject, "score");
                    if (! ((JsonObject) string).has("name") || ! ((JsonObject) string).has("objective"))
                        throw new JsonParseException("A score component needs a least a name and an objective");
//                    ScoreText scoreText = new ScoreText(JsonHelper.getString((JsonObject) string, "name"), JsonHelper.getString((JsonObject) string, "objective"));
                } else if (jsonObject.has("selector")) {
                    string = this.getSeparator(type, jsonDeserializationContext, jsonObject);
//                    SelectorText selectorText = new SelectorText(JsonHelper.getString(jsonObject, "selector"), (Optional<Text>) string);
                } else if (jsonObject.has("keybind")) {
//                    KeybindText keybindText = new KeybindText(JsonHelper.getString(jsonObject, "keybind"));
                } else {
                    if (! jsonObject.has("nbt"))
                        throw new JsonParseException("Don't know how to turn " + jsonElement + " into a Component");
                    string = JsonHelper.getString(jsonObject, "nbt");
                    Optional<Text> text = this.getSeparator(type, jsonDeserializationContext, jsonObject);
                    boolean objects = JsonHelper.getBoolean(jsonObject, "interpret", false);
                    if (jsonObject.has("block")) {
//                        NbtText.BlockNbtText blockNbtText = new NbtText.BlockNbtText((String) string, objects, JsonHelper.getString(jsonObject, "block"), text);
                    } else if (jsonObject.has("entity")) {
//                        NbtText.EntityNbtText entityNbtText = new NbtText.EntityNbtText((String) string, objects, JsonHelper.getString(jsonObject, "entity"), text);
                    } else {
                        if (! jsonObject.has("storage"))
                            throw new JsonParseException("Don't know how to turn " + jsonElement + " into a Component");
//                        NbtText.StorageNbtText storageNbtText = new NbtText.StorageNbtText((String) string, objects, new Identifier(JsonHelper.getString(jsonObject, "storage")), text);
                    }
                }
                if (jsonObject.has("extra")) {
                    string = JsonHelper.getArray(jsonObject, "extra");
                    if (((JsonArray) string).size() <= 0)
                        throw new JsonParseException("Unexpected empty array of components");
                    for (int jsonArray = 0; jsonArray < ((JsonArray) string).size(); ++ jsonArray) {
                        var5_17.append(this.deserialize(((JsonArray) string).get(jsonArray), type, jsonDeserializationContext));
                    }
                }
                var5_17.setStyle(jsonDeserializationContext.deserialize(jsonElement, Style.class));
                return var5_17;
            }
            if (! jsonElement.isJsonArray())
                throw new JsonParseException("Don't know how to turn " + jsonElement + " into a Component");
            JsonArray jsonObject = jsonElement.getAsJsonArray();
            Object var5_18 = null;
            for (JsonElement jsonArray : jsonObject) {
                MutableText objects = this.deserialize(jsonArray, jsonArray.getClass(), jsonDeserializationContext);
                if (var5_19 == null) {
                    var5_19 = objects;
                    continue;
                }
                var5_19.append(objects);
            }
            return var5_19;
        }

        private Optional<Text> getSeparator(Type type, JsonDeserializationContext context, JsonObject json) {
            if (json.has("separator")) {
                return Optional.of(this.deserialize(json.get("separator"), type, context));
            }
            return Optional.empty();
        }

        private void addStyle(Style style, JsonObject json, JsonSerializationContext context) {
            JsonElement jsonElement = context.serialize(style);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = (JsonObject) jsonElement;
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    json.add(entry.getKey(), entry.getValue());
                }
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public JsonElement serialize(Text text, Type type, JsonSerializationContext jsonSerializationContext) {
            Object jsonArray;
            JsonObject jsonObject = new JsonObject();
            if (! text.getStyle().isEmpty()) {
                this.addStyle(text.getStyle(), jsonObject, jsonSerializationContext);
            }
            if (! text.getSiblings().isEmpty()) {
                jsonArray = new JsonArray();
                for (Text text2 : text.getSiblings()) {
                    ((JsonArray) jsonArray).add(this.serialize(text2, text2.getClass(), jsonSerializationContext));
                }
                jsonObject.add("extra", (JsonElement) jsonArray);
            }
            if (text instanceof LiteralText) {
                jsonObject.addProperty("text", ((LiteralText) text).getRawString());
                return jsonObject;
//            } else if (text instanceof TranslatableText) {
//                jsonArray = text;
//                jsonObject.addProperty("translate", ((TranslatableText) jsonArray).getKey());
//                if (((TranslatableText) jsonArray).getArgs() == null || ((TranslatableText) jsonArray).getArgs().length <= 0)
//                    return jsonObject;
//                JsonArray jsonArray2 = new JsonArray();
//                for (Object object : ((TranslatableText) jsonArray).getArgs()) {
//                    if (object instanceof Text) {
//                        ((JsonArray) jsonArray2).add(this.serialize((Text) object, object.getClass(), jsonSerializationContext));
//                        continue;
//                    }
//                    ((JsonArray) jsonArray2).add(new JsonPrimitive(String.valueOf(object)));
//                }
//                jsonObject.add("with", (JsonElement) jsonArray2);
//                return jsonObject;
//            } else if (text instanceof ScoreText) {
//                jsonArray = text;
//                JsonObject jsonObject2 = new JsonObject();
//                ((JsonObject) jsonObject2).addProperty("name", ((ScoreText) jsonArray).getName());
//                ((JsonObject) jsonObject2).addProperty("objective", ((ScoreText) jsonArray).getObjective());
//                jsonObject.add("score", (JsonElement) jsonObject2);
//                return jsonObject;
//            } else if (text instanceof SelectorText) {
//                jsonArray = text;
//                jsonObject.addProperty("selector", ((SelectorText) jsonArray).getPattern());
//                this.addSeparator(jsonSerializationContext, jsonObject, ((SelectorText) jsonArray).getSeparator());
//                return jsonObject;
//            } else if (text instanceof KeybindText) {
//                jsonArray = text;
//                jsonObject.addProperty("keybind", ((KeybindText) jsonArray).getKey());
//                return jsonObject;
//            } else {
//                if (! (text instanceof NbtText))
//                    throw new IllegalArgumentException("Don't know how to serialize " + text + " as a Component");
//                jsonArray = text;
//                jsonObject.addProperty("nbt", ((NbtText) jsonArray).getPath());
//                jsonObject.addProperty("interpret", ((NbtText) jsonArray).shouldInterpret());
//                this.addSeparator(jsonSerializationContext, jsonObject, ((NbtText) jsonArray).separator);
//                if (text instanceof NbtText.BlockNbtText) {
//                    NbtText.BlockNbtText nbtText = (NbtText.BlockNbtText) text;
//                    jsonObject.addProperty("block", nbtText.getPos());
//                    return jsonObject;
//                } else if (text instanceof NbtText.EntityNbtText) {
//                    NbtText.EntityNbtText nbtText = (NbtText.EntityNbtText) text;
//                    jsonObject.addProperty("entity", nbtText.getSelector());
//                    return jsonObject;
//                } else {
//                    if (! (text instanceof NbtText.StorageNbtText))
//                        throw new IllegalArgumentException("Don't know how to serialize " + text + " as a Component");
//                    NbtText.StorageNbtText nbtText = (NbtText.StorageNbtText) text;
//                    jsonObject.addProperty("storage", nbtText.getId().toString());
//                }
            }
            return jsonObject;
        }

        private void addSeparator(JsonSerializationContext context, JsonObject json, Optional<Text> separator2) {
            separator2.ifPresent(separator -> json.add("separator", this.serialize(separator, separator.getClass(), context)));
        }
    }
}

