package com.github.cao.awa.hyacinth.logging;

public class PrintUtil {
    @SafeVarargs
    public static <T> void printsln(T... targets) {
        for (T target : targets) {
            System.out.println(target);
        }
    }
}
