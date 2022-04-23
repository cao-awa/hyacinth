/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.thread;

import java.util.List;
import net.minecraft.util.profiler.Sampler;

public interface SampleableExecutor {
    public List<Sampler> createSamplers();
}

