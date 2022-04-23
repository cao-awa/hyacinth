package com.github.cao.awa.hyacinth.server.task;

public record ServerTask(int creationTicks, Runnable runnable)
        implements Runnable {

    public int getCreationTicks() {
        return this.creationTicks;
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}

