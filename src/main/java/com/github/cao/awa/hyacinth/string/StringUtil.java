package com.github.cao.awa.hyacinth.string;

import java.util.function.Consumer;

public class StringUtil {
    public static Consumer<String> addPrefix(String prefix, Consumer<String> consumer) {
        return string -> consumer.accept(prefix + string);
    }
}
