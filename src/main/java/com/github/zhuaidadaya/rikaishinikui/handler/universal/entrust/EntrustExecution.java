package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust;

import com.github.cao.awa.hyacinth.constants.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.*;
import org.apache.logging.log4j.*;

import java.time.*;
import java.util.Collection;
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

    public static <T> void operation(T target, Consumer<T> action) {
        action.accept(target);
    }

    @SafeVarargs
    public static <T> void order(T target, Consumer<T>... actions) {
        for (Consumer<T> action : actions) {
            action.accept(target);
        }
    }

    public static <T> void trying(ExceptingConsumer<T> action) {
        try {
            action.accept((T) o);
        } catch (Exception e) {

        }
    }

    public static <T> void trying(ExceptingConsumer<T> action, Consumer<T> actionWhenException) {
        try {
            action.accept((T) o);
        } catch (Exception e) {
            actionWhenException.accept((T) o);
        }
    }

    public static <T> void trying(T target, ExceptingConsumer<T> action) {
        try {
            action.accept(target);
        } catch (Exception e) {

        }
    }

    public static <T> void trying(T target, ExceptingConsumer<T> action, Consumer<T> actionWhenException) {
        try {
            action.accept(target);
        } catch (Exception e) {
            actionWhenException.accept(target);
        }
    }

    public static <T> void tryFor(Collection<T> targets, ExceptingConsumer<T> action) {
        if (targets != null) {
            for (T target : targets) {
                try {
                    action.accept(target);
                } catch (Exception e) {

                }
            }
        }
    }

    public static <T> void tryFor(Collection<T> targets, ExceptingConsumer<T> action, Consumer<T> whenException) {
        if (targets != null) {
            for (T target : targets) {
                try {
                    action.accept(target);
                } catch (Exception e) {
                    whenException.accept(target);
                }
            }
        }
    }

    public static void temporary(Temporary action) {
        action.apply();
    }

    public static <T> void tryTemporary(ExceptingTemporary action, Temporary whenException) {
        try {
            action.apply();
        } catch (Exception e) {
            whenException.apply();
        }
    }

    public static <T> void tryTemporary(ExceptingTemporary action) {
        try {
            action.apply();
        } catch (Exception e) {

        }
    }

    public static <T> void tryAssertNotNull(T target, ExceptingConsumer<T> action) {
        try {
            if (target != null) {
                action.accept(target);
            }
        } catch (Exception e) {

        }
    }

    public static <T> void tryAssertNotNull(T target, ExceptingConsumer<T> action, Consumer<T> whenException) {
        try {
            if (target != null) {
                action.accept(target);
            }
        } catch (Exception e) {
            whenException.accept(target);
        }
    }

    public static <T> void tryExecuteNull(T target, ExceptingConsumer<T> asNotNull, ExceptingConsumer<T> asNull) {
        try {
            if (target != null) {
                asNotNull.accept(target);
            } else {
                asNull.accept(null);
            }
        } catch (Exception e) {

        }
    }

    public static <T> void tryExecuteNull(T target, ExceptingConsumer<T> asNotNull, ExceptingConsumer<T> asNull, Consumer<T> whenException) {
        try {
            if (target != null) {
                asNotNull.accept(target);
            } else {
                asNull.accept(null);
            }
        } catch (Exception e) {
            whenException.accept(target);
        }
    }

    public static <T> void catching(T target, ExceptingConsumer<T> action, Consumer<Exception> excepting) {
        try {
            action.accept(target);
        } catch (Exception e) {
            excepting.accept(e);
        }
    }

    public static <T> void catchingTemporary(ExceptingTemporary action, Consumer<Exception> excepting) {
        try {
            action.apply();
        } catch (Exception e) {
            excepting.accept(e);
        }
    }

    // Mojang Utils

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

    public static void error(String message) {
        LOGGER.error(message);
        if (SharedConstants.isDevelopment) {
            pause(message);
        }
    }

}

