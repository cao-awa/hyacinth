package com.github.cao.awa.hyacinth.network.text.translate;

import com.github.cao.awa.hyacinth.language.Language;
import com.github.cao.awa.hyacinth.network.text.*;
import com.github.cao.awa.hyacinth.network.text.style.Style;
import com.github.cao.awa.hyacinth.network.text.visitor.StringVisitable;
import com.github.cao.awa.hyacinth.server.command.source.ServerCommandSource;
import com.github.cao.awa.hyacinth.server.entity.Entity;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.identifier.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatableText
        extends BaseText
        implements ParsableText {
    private static final Object[] EMPTY_ARGUMENTS = new Object[0];
    private static final StringVisitable LITERAL_PERCENT_SIGN = StringVisitable.plain("%");
    private static final StringVisitable NULL_ARGUMENT = StringVisitable.plain("null");
    private final String key;
    private final Object[] args;
    @Nullable
    private Language languageCache;
    private List<StringVisitable> translations = ImmutableList.of();
    private static final Pattern ARG_FORMAT = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TranslatableText(String key) {
        this.key = key;
        this.args = EMPTY_ARGUMENTS;
    }

    public TranslatableText(String key, Object ... args) {
        this.key = key;
        this.args = args;
    }

    private void updateTranslations() {
        Language language = Language.getInstance();
        if (language == this.languageCache) {
            return;
        }
        this.languageCache = language;
        String string = language.get(this.key);
        try {
            ImmutableList.Builder builder = ImmutableList.builder();
            this.forEachPart(string, builder::add);
            this.translations = builder.build();
        }
        catch (TranslationException builder) {
            this.translations = ImmutableList.of(StringVisitable.plain(string));
        }
    }

    private void forEachPart(String translation, Consumer<StringVisitable> partsConsumer) {
        Matcher matcher = ARG_FORMAT.matcher(translation);
        try {
            int i = 0;
            int j = 0;
            while (matcher.find(j)) {
                String string;
                int k = matcher.start();
                int l = matcher.end();
                if (k > j) {
                    string = translation.substring(j, k);
                    if (string.indexOf(37) != -1) {
                        throw new IllegalArgumentException();
                    }
                    partsConsumer.accept(StringVisitable.plain(string));
                }
                string = matcher.group(2);
                String string2 = translation.substring(k, l);
                if ("%".equals(string) && "%%".equals(string2)) {
                    partsConsumer.accept(LITERAL_PERCENT_SIGN);
                } else if ("s".equals(string)) {
                    int m;
                    String string3 = matcher.group(1);
                    int n = m = string3 != null ? Integer.parseInt(string3) - 1 : i++;
                    if (m < this.args.length) {
                        partsConsumer.accept(this.getArg(m));
                    }
                } else {
                    throw new TranslationException(this, "Unsupported format: '" + string2 + "'");
                }
                j = l;
            }
            if (j < translation.length()) {
                String k = translation.substring(j);
                if (k.indexOf(37) != -1) {
                    throw new IllegalArgumentException();
                }
                partsConsumer.accept(StringVisitable.plain(k));
            }
        }
        catch (IllegalArgumentException i) {
            throw new TranslationException(this, (Throwable)i);
        }
    }

    private StringVisitable getArg(int index) {
        if (index >= this.args.length) {
            throw new TranslationException(this, index);
        }
        Object object = this.args[index];
        if (object instanceof Text) {
            return (Text)object;
        }
        return object == null ? NULL_ARGUMENT : StringVisitable.plain(object.toString());
    }

    @Override
    public JsonObject toJSONObject() {
        JsonObject json = new JsonObject();
        json.addProperty("translate", key);
        json.add("with", formatArgs());
        return json;
    }

    public JsonArray formatArgs() {
        JsonArray array = new JsonArray();
        for (Object o : args) {
            array.add(o.toString());
        }
        return array;
    }

    @Override
    public TranslatableText copy() {
        return new TranslatableText(this.key, this.args);
    }

    @Override
    public <T> Optional<T> visitSelf(StringVisitable.StyledVisitor<T> visitor, Style style) {
        this.updateTranslations();
        for (StringVisitable stringVisitable : this.translations) {
            Optional<T> optional = stringVisitable.visit(visitor, style);
            if (!optional.isPresent()) continue;
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> visitSelf(StringVisitable.Visitor<T> visitor) {
//        this.updateTranslations();
        for (StringVisitable stringVisitable : this.translations) {
            Optional<T> optional = stringVisitable.visit(visitor);
            if (!optional.isPresent()) continue;
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public MutableText parse(@Nullable ServerCommandSource source, @Nullable Entity sender, int depth) throws CommandSyntaxException {
        Object[] objects = new Object[this.args.length];
        for (int i = 0; i < objects.length; ++i) {
            Object object = this.args[i];
            objects[i] = object instanceof Text ? Texts.parse(source, (Text)object, sender, depth) : object;
        }
        return new TranslatableText(this.key, objects);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof TranslatableText) {
            TranslatableText translatableText = (TranslatableText)object;
            return Arrays.equals(this.args, translatableText.args) && this.key.equals(translatableText.key) && super.equals(object);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.key.hashCode();
        i = 31 * i + Arrays.hashCode(this.args);
        return i;
    }

    @Override
    public String toString() {
        return "TranslatableComponent{key='" + this.key + "', args=" + Arrays.toString(this.args) + ", siblings=" + this.siblings + ", style=" + this.getStyle() + "}";
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public static String createTranslationKey(String type, @Nullable Identifier id) {
        if (id == null) {
            return type + ".unregistered_sadface";
        }
        return type + "." + id.getNamespace() + "." + id.getPath().replace('/', '.');
    }
}

