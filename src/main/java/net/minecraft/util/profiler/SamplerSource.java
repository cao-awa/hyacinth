/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.profiler;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.util.profiler.ReadableProfiler;
import net.minecraft.util.profiler.Sampler;

public interface SamplerSource {
    public Set<Sampler> getSamplers(Supplier<ReadableProfiler> var1);
}

