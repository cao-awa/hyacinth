package com.github.cao.awa.hyacinth.language;

import com.github.cao.awa.hyacinth.network.text.OrderedText;
import com.github.cao.awa.hyacinth.network.text.style.Style;
import com.github.cao.awa.hyacinth.network.text.visitor.StringVisitable;
import com.github.cao.awa.hyacinth.network.text.visitor.TextVisitFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.json.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public abstract class Language {
    public static final String DEFAULT_LANGUAGE = "en_us";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Pattern TOKEN_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
    private static volatile Language instance = Language.create();

    public Language(Map<Object, Object> finalInputStream) {

    }

    private static Language create() {
        InputStream inputStream;
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        BiConsumer<String, String> biConsumer = builder::put;
        String defaultLanguage = "/assets/minecraft/lang/en_us.json";
        try {
            inputStream = Language.class.getResourceAsStream(defaultLanguage);
            try {
                Language.load(inputStream, biConsumer);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (JsonParseException | IOException inputStream2) {
            LOGGER.error("Couldn't read strings from {}", defaultLanguage, inputStream2);
        }
        Map<Object, Object> finalInputStream = builder.build();
        return new Language(finalInputStream) {
            final Map<Object, Object> field_25308;

            {
                this.field_25308 = finalInputStream;
            }

            @Override
            public String get(String key) {
                return this.field_25308.getOrDefault(key, key).toString();
            }

            @Override
            public boolean hasTranslation(String key) {
                return this.field_25308.containsKey(key);
            }

            @Override
            public boolean isRightToLeft() {
                return false;
            }

            @Override
            public OrderedText reorder(StringVisitable text) {
                return visitor -> text.visit((style, string) -> TextVisitFactory.visitFormatted(string, style, visitor) ? Optional.empty() : StringVisitable.TERMINATE_VISIT, Style.EMPTY).isPresent();
            }
        };
    }

    public static void load(InputStream inputStream, BiConsumer<String, String> entryConsumer) {
        JsonObject jsonObject = GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String string = TOKEN_PATTERN.matcher(JsonHelper.asString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
            entryConsumer.accept(entry.getKey(), string);
        }
    }

    public static Language getInstance() {
        return instance;
    }

    public static void setInstance(Language language) {
        instance = language;
    }

    public abstract String get(String var1);

    public abstract boolean hasTranslation(String var1);

    public abstract boolean isRightToLeft();

    public List<OrderedText> reorder(List<StringVisitable> texts) {
        return texts.stream().map(this::reorder).collect(ImmutableList.toImmutableList());
    }

    public abstract OrderedText reorder(StringVisitable var1);
}


