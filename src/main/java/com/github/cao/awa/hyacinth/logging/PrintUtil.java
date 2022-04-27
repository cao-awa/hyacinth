package com.github.cao.awa.hyacinth.logging;

import com.github.cao.awa.hyacinth.logging.track.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class PrintUtil {
    private static final Logger TRACKER = LogManager.getLogger("Hyacinth:Tracker");
    public static boolean debugging = false;

    @SafeVarargs
    public static <T> void printsln(T... messages) {
        for (T target : messages) {
            System.out.println(target);
        }
    }

    public static <T> void info(Logger logger, T message) {
        logger.info(message);
    }

    public static <T> void info(Logger logger, T[] messages, int limit) {
        int printed = 0;
        for (T message : messages) {
            if (printed++ == limit) {
                break;
            }
            logger.info(message);
        }
    }

    public static <T> void debug(Logger logger, T[] messages) {
        for (T message : messages) {
            logger.debug(message);
        }
    }

    public static <T> void debug(Logger logger, T[] messages, int limit) {
        int printed = 0;
        for (T message : messages) {
            if (printed++ == limit) {
                break;
            }
            logger.debug(message);
        }
    }

    public static <T> void info(Logger logger, Collection<T> messages) {
        for (T message : messages) {
            logger.info(message);
        }
    }

    public static <T> void info(Logger logger, Collection<T> messages, int limit) {
        int printed = 0;
        for (T message : messages) {
            if (printed++ == limit) {
                break;
            }
            logger.info(message);
        }
    }

    public static <T> void debug(Logger logger, Collection<T> messages) {
        for (T message : messages) {
            logger.debug(message);
        }
    }

    public static <T> void debug(Logger logger, Collection<T> messages, int limit) {
        int printed = 0;
        for (T message : messages) {
            if (printed++ == limit) {
                break;
            }
            logger.debug(message);
        }
    }

    public static void messageToTracker(StackTraceElement[] tracker) {
        messageToTracker(new Tracking(tracker, 5,1, "Tracking..."));
    }

    public static void messageToTracker(String... messages) {
        messageToTracker(new Tracking(Thread.currentThread().getStackTrace(), 5,2, messages));
    }

    public static void messageToTracker(StackTraceElement[] tracker, String... messages) {
        messageToTracker(new Tracking(tracker, 5,1, messages));
    }

    public static void messageToTracker(StackTraceElement[] tracker, int trackLimit, String... messages) {
        messageToTracker(new Tracking(tracker, trackLimit,1, messages));
    }

    public static void messageToTracker(Tracking tracking) {
        if (debugging) {
            int limit = tracking.getTrackLimit();
            PrintUtil.debug(TRACKER, "--Hyacinth Tracking: ");
            for (String message : tracking.getMessages()) {
                TRACKER.debug("      " + message);
            }
            PrintUtil.debug(TRACKER, "      --Hyacinth Tracking(Thread Traces): ");
            for (int i = tracking.getStartFrom(), trackerLength = tracking.getTracker().length; i < trackerLength; i++) {
                StackTraceElement elements = tracking.getTracker()[i];
                if (limit-- == 0) {
                    break;
                }
                TRACKER.trace("         " + elements);
            }
        }
    }

    public static <T> void info(Logger logger, T[] messages) {
        for (T message : messages) {
            logger.info(message);
        }
    }

    public static <T> void debug(Logger logger, T message) {
        logger.debug(message);
    }
}
