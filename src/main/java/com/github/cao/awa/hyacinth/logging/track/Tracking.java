package com.github.cao.awa.hyacinth.logging.track;

public class Tracking {
    private final StackTraceElement[] tracker;
    private final String[] messages;
    private final int startFrom;
    private final int trackLimit;

    public Tracking(String... messages) {
        this.tracker = Thread.currentThread().getStackTrace();
        this.messages = messages;
        this.trackLimit = 5;
        this.startFrom = 2;
    }

    public Tracking(StackTraceElement[] tracker, String... messages) {
        this.tracker = tracker;
        this.messages = messages;
        this.trackLimit = 5;
        this.startFrom = 1;
    }

    public Tracking(StackTraceElement[] tracker, int trackLimit, String... messages) {
        this.tracker = tracker;
        this.messages = messages;
        this.trackLimit = trackLimit;
        this.startFrom = 1;
    }

    public Tracking(StackTraceElement[] tracker, int trackLimit, int startFrom, String... messages) {
        this.tracker = tracker;
        this.messages = messages;
        this.trackLimit = trackLimit;
        this.startFrom = startFrom;
    }

    public StackTraceElement[] getTracker() {
        return tracker;
    }

    public String[] getMessages() {
        return messages;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public int getTrackLimit() {
        return trackLimit;
    }
}
