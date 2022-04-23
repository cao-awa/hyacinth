/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.profiler;

import net.minecraft.util.profiler.Profiler;

public interface Recorder {
    public void stop();

    public void startTick();

    public boolean isActive();

    public Profiler getProfiler();

    public void endTick();
}

