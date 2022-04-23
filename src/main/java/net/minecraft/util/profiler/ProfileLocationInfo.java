/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.profiler;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

/**
 * Profiling information on a specific profiler location.
 */
public interface ProfileLocationInfo {
    /**
     * Returns the total time spent visiting the profiler location.
     */
    public long getTotalTime();

    public long getMaxTime();

    /**
     * Returns the number of times the profiler location has been visited.
     */
    public long getVisitCount();

    /**
     * Returns a marker to count map indicating the times each marker has been
     * visited in the profiler location.
     */
    public Object2LongMap<String> getCounts();
}

