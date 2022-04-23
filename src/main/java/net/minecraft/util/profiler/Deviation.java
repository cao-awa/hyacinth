/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.profiler;

import java.time.Instant;
import net.minecraft.util.profiler.ProfileResult;

public final class Deviation {
    public final Instant instant;
    public final int ticks;
    public final ProfileResult result;

    public Deviation(Instant instant, int ticks, ProfileResult result) {
        this.instant = instant;
        this.ticks = ticks;
        this.result = result;
    }
}

