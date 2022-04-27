package com.github.cao.awa.hyacinth.logging;

import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class LoggerPrintStream extends PrintStream {
    protected static final Logger LOGGER = LogManager.getLogger("Stream:STDOUT");
    protected final String name;

    public LoggerPrintStream(String name, OutputStream out) {
        super(out);
        this.name = name;
    }

    @Override
    public void println(@Nullable String message) {
        this.log(message);
    }

    @Override
    public void println(Object object) {
        this.log(String.valueOf(object));
    }

    protected void log(@Nullable String message) {
        LOGGER.info("[{}]: {}", this.name, message);
    }
}

