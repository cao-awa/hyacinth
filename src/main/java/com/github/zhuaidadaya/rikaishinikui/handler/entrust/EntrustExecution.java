package com.github.zhuaidadaya.rikaishinikui.handler.entrust;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntrustExecution {
    private static final Object o = new Object();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Consumer<String> missingBreakpointHandler = string -> {};

    public static <T> void notNull(T target, Consumer<T> action) {
        if (target != null) {
            action.accept(target);
        }
    }

    public static <T> void nullRequires(T target, Consumer<T> action) {
        if (target == null) {
            action.accept(null);
        }
    }

    public static <T> void executeNull(T target, Consumer<T> asNotNull, Consumer<T> asNull) {
        if (target == null) {
            asNull.accept((T) o);
        } else {
            asNotNull.accept(target);
        }
    }

    public static <T> void before(T target, Consumer<T> first, Consumer<T> before) {
        first.accept(target);
        before.accept(target);
    }

    public static <T> void equalsValue(Supplier<T> target, Supplier<T> tester, Consumer<T> equalsAction, Consumer<T> elseAction) {
        if (tester.get().equals(target.get())) {
            equalsAction.accept(target.get());
        } else {
            elseAction.accept(target.get());
        }
    }

    public static <T> void assertValue(Supplier<T> target, Supplier<T> tester, Consumer<T> equalsAction, Consumer<T> elseAction) {
        if (tester.get() == target.get()) {
            equalsAction.accept(target.get());
        } else {
            elseAction.accept(target.get());
        }
    }

    public static <T> void noArg(Consumer<T> action) {
        action.accept(null);
    }

    public static <T> void action(T target, Consumer<T> action) {
        action.accept(target);
    }

    @SafeVarargs
    public static <T> void order(T target, Consumer<T>... actions) {
        for (Consumer<T> action : actions) {
            action.accept(target);
        }
    }

    private static void pause(String message) {
        boolean bl;
        Instant instant = Instant.now();
        LOGGER.warn("Did you remember to set a breakpoint here?");
        bl = Duration.between(instant, Instant.now()).toMillis() > 500L;
        if (!bl) {
            missingBreakpointHandler.accept(message);
        }
    }

    public static <T extends Throwable> T throwOrPause(T t) {
        if (SharedConstants.isDevelopment) {
            LOGGER.error("Trying to throw a fatal exception, pausing in IDE", t);
            pause(t.getMessage());
        }
        return t;
    }
}

