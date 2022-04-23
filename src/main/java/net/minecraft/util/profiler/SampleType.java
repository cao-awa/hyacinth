/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.profiler;

public enum SampleType {
    PATH_FINDING("pathfinding"),
    EVENT_LOOPS("event-loops"),
    MAIL_BOXES("mailboxes"),
    TICK_LOOP("ticking"),
    JVM("jvm"),
    CHUNK_RENDERING("chunk rendering"),
    CHUNK_RENDERING_DISPATCHING("chunk rendering dispatching"),
    CPU("cpu");

    private final String name;

    private SampleType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

