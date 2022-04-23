/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.profiler;

import com.github.zhuaidadaya.rikaishinikui.handler.times.TimeUtil;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SamplerFactory {
    private final Set<String> sampledFullPaths = new ObjectOpenHashSet<>();

    public Set<Sampler> createSamplers(Supplier<ReadableProfiler> profilerSupplier) {
        Set<Sampler> set = profilerSupplier.get().getSampleTargets().stream().filter(target -> !this.sampledFullPaths.contains(target.getLeft())).map(target -> SamplerFactory.createSampler(profilerSupplier, (String)target.getLeft(), (SampleType)((Object)((Object)target.getRight())))).collect(Collectors.toSet());
        for (Sampler sampler : set) {
            this.sampledFullPaths.add(sampler.getName());
        }
        return set;
    }

    private static Sampler createSampler(Supplier<ReadableProfiler> profilerSupplier, String id, SampleType type) {
        return Sampler.create(id, type, () -> {
            ProfilerSystem.LocatedInfo locatedInfo = profilerSupplier.get().getInfo(id);
            return locatedInfo == null ? 0.0 : (double)locatedInfo.getMaxTime() / TimeUtil.MILLI_IN_NANOS;
        });
    }
}

