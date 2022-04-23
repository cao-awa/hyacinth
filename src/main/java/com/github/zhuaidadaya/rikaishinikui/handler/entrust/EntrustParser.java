package com.github.zhuaidadaya.rikaishinikui.handler.entrust;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import com.google.common.collect.Lists;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntrustParser {
    private static Logger LOGGER = LogManager.getLogger();

    public static <T> T getNotNull(T target, @NotNull T defaultValue) {
        if (target == null) {
            return defaultValue;
        }
        return target;
    }

    public static <T> T nullRequires(T target, Supplier<T> action) {
        return action.get();
    }

    public static <T> T build(Supplier<T> obj) {
        return obj.get();
    }

    public static <T> T operation(T target, Consumer<T> action) {
        action.accept(target);
        return target;
    }

    public static <T> T operation(Supplier<T> target) {
        return target.get();
    }

    public static <T> DataResult<List<T>> toArray(List<T> list, int length) {
        if (list.size() != length) {
            String string = "Input is not a list of " + length + " elements";
            if (list.size() >= length) {
                return DataResult.error(string, list.subList(0, length));
            }
            return DataResult.error(string);
        }
        return DataResult.success(list);
    }

    public static DataResult<int[]> toArray(IntStream stream, int length) {
        int[] is = stream.limit(length + 1).toArray();
        if (is.length != length) {
            String string = "Input is not a list of " + length + " ints";
            if (is.length >= length) {
                return DataResult.error(string, Arrays.copyOf(is, length));
            }
            return DataResult.error(string);
        }
        return DataResult.success(is);
    }

    public static <T> T select(T[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

    public static String replaceInvalidChars(String string, CharPredicate predicate) {
        return string.toLowerCase(Locale.ROOT).chars().mapToObj(charCode -> predicate.test((char) charCode) ? Character.toString((char) charCode) : "_").collect(Collectors.joining());
    }

    public static Runnable debugRunnable(String activeThreadName, Runnable task) {
        if (SharedConstants.isDevelopment) {
            return () -> {
                Thread thread = Thread.currentThread();
                String string2 = thread.getName();
                thread.setName(activeThreadName);
                try {
                    task.run();
                } finally {
                    thread.setName(string2);
                }
            };
        }
        return task;
    }

    public static <V> Supplier<V> debugSupplier(String activeThreadName, Supplier<V> supplier) {
        if (SharedConstants.isDevelopment) {
            return () -> {
                Thread thread = Thread.currentThread();
                String string2 = thread.getName();
                thread.setName(activeThreadName);
                try {
                    return supplier.get();
                } finally {
                    thread.setName(string2);
                }
            };
        }
        return supplier;
    }

    public static <V> CompletableFuture<List<V>> combine(List<? extends CompletableFuture<? extends V>> futures) {
        ArrayList<@Nullable Object> list = Lists.newArrayListWithCapacity(futures.size());
        CompletableFuture[] completableFutures = new CompletableFuture[futures.size()];
        CompletableFuture completableFuture = new CompletableFuture();
        futures.forEach(future -> {
            int i = list.size();
            list.add(null);
            completableFutures[i] = future.whenComplete((object, throwable) -> {
                if (throwable != null) {
                    completableFuture.completeExceptionally(throwable);
                } else {
                    list.set(i, object);
                }
            });
        });
        return CompletableFuture.allOf(completableFutures).applyToEither(completableFuture, v -> list);
    }
}
