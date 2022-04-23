package com.github.cao.awa.hyacinth.network.text;

import com.github.cao.awa.hyacinth.network.text.event.HoverEvent;
import com.github.cao.awa.hyacinth.network.text.style.Formatting;
import com.github.cao.awa.hyacinth.network.text.style.Style;
import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import com.github.cao.awa.hyacinth.server.command.source.ServerCommandSource;
import com.github.cao.awa.hyacinth.server.entity.Entity;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class Texts {
    public static final String DEFAULT_SEPARATOR = ", ";
    public static final Text GRAY_DEFAULT_SEPARATOR_TEXT = new LiteralText(", ").formatted(Formatting.GRAY);
    public static final Text DEFAULT_SEPARATOR_TEXT = new LiteralText(", ");

    public static MutableText setStyleIfAbsent(MutableText text, Style style) {
        if (style.isEmpty()) {
            return text;
        }
        Style style2 = text.getStyle();
        if (style2.isEmpty()) {
            return text.setStyle(style);
        }
        if (style2.equals(style)) {
            return text;
        }
        return text.setStyle(style2.withParent(style));
    }

    public static Optional<MutableText> parse(@Nullable ServerCommandSource source, Optional<Text> text, @Nullable Entity sender, int depth) throws CommandSyntaxException {
        return text.isPresent() ? Optional.of(Texts.parse(source, text.get(), sender, depth)) : Optional.empty();
    }

    public static MutableText parse(@Nullable ServerCommandSource source, Text text, @Nullable Entity sender, int depth) throws CommandSyntaxException {
        if (depth > 100) {
            return text.shallowCopy();
        }
        MutableText mutableText = text instanceof ParsableText ? ((ParsableText)((Object)text)).parse(source, sender, depth + 1) : text.copy();
        for (Text text2 : text.getSiblings()) {
            mutableText.append(Texts.parse(source, text2, sender, depth + 1));
        }
        return mutableText.fillStyle(Texts.parseStyle(source, text.getStyle(), sender, depth));
    }

    private static Style parseStyle(@Nullable ServerCommandSource source, Style style, @Nullable Entity sender, int depth) throws CommandSyntaxException {
        Text text;
        HoverEvent hoverEvent = style.getHoverEvent();
        if (hoverEvent != null && (text = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT)) != null) {
            HoverEvent hoverEvent2 = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Texts.parse(source, text, sender, depth + 1));
            return style.withHoverEvent(hoverEvent2);
        }
        return style;
    }

    public static Text toText(GameProfile profile) {
        if (profile.getName() != null) {
            return new LiteralText(profile.getName());
        }
        if (profile.getId() != null) {
            return new LiteralText(profile.getId().toString());
        }
        return new LiteralText("(unknown)");
    }

    public static Text joinOrdered(Collection<String> strings) {
        return Texts.joinOrdered(strings, string -> new LiteralText((String)string).formatted(Formatting.GREEN));
    }

    public static <T extends Comparable<T>> Text joinOrdered(Collection<T> elements, Function<T, Text> transformer) {
        if (elements.isEmpty()) {
            return LiteralText.EMPTY;
        }
        if (elements.size() == 1) {
            return transformer.apply(elements.iterator().next());
        }
        ArrayList<T> list = Lists.newArrayList(elements);
        list.sort(Comparable::compareTo);
        return Texts.join(list, transformer);
    }

    public static <T> Text join(Collection<? extends T> elements, Function<T, Text> transformer) {
        return Texts.join(elements, GRAY_DEFAULT_SEPARATOR_TEXT, transformer);
    }

    public static <T> MutableText join(Collection<? extends T> elements, Optional<? extends Text> separator, Function<T, Text> transformer) {
        return Texts.join(elements, DataFixUtils.orElse(separator, GRAY_DEFAULT_SEPARATOR_TEXT), transformer);
    }

    public static Text join(Collection<? extends Text> texts, Text separator) {
        return Texts.join(texts, separator, Function.identity());
    }

    public static <T> MutableText join(Collection<? extends T> elements, Text separator, Function<T, Text> transformer) {
        if (elements.isEmpty()) {
            return new LiteralText("");
        }
        if (elements.size() == 1) {
            return transformer.apply(elements.iterator().next()).shallowCopy();
        }
        LiteralText mutableText = new LiteralText("");
        boolean bl = true;
        for (T object : elements) {
            if (!bl) {
                mutableText.append(separator);
            }
            mutableText.append(transformer.apply(object));
            bl = false;
        }
        return mutableText;
    }

    public static MutableText bracketed(Text text) {
        return new TranslatableText("chat.square_brackets", text);
    }

    public static Text toText(Message message) {
        if (message instanceof Text) {
            return (Text)message;
        }
        return new LiteralText(message.getString());
    }
}
