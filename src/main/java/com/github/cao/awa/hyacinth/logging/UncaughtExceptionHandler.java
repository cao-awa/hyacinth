package com.github.cao.awa.hyacinth.logging;

import org.apache.logging.log4j.Logger;

public record UncaughtExceptionHandler(Logger logger) implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        this.logger.error("Caught previously unhandled exception :");
        this.logger.error(thread.getName(), throwable);
    }
}

