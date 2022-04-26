package net.minecraft.resource;

import com.github.zhuaidadaya.rikaishinikui.handler.times.*;
import com.google.common.base.Stopwatch;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.mojang.datafixers.util.*;
import net.minecraft.util.profiler.ProfileResult;
import net.minecraft.util.profiler.ProfilerSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An implementation of resource reload that includes an additional profiling
 * summary for each reloader.
 */
public class ProfiledResourceReload
extends SimpleResourceReload<ProfiledResourceReload.Summary> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Stopwatch reloadTimer = Stopwatch.createUnstarted();

    public ProfiledResourceReload(ResourceManager manager, List<ResourceReloader> reloaders, Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage) {
        super(prepareExecutor, applyExecutor, manager, reloaders, (synchronizer, resourceManager, reloader, prepare, apply) -> {
            AtomicLong atomicLong = new AtomicLong();
            AtomicLong atomicLong2 = new AtomicLong();
            ProfilerSystem profilerSystem = new ProfilerSystem(TimeUtil.nanoSupplier(), () -> 0, false);
            ProfilerSystem profilerSystem2 = new ProfilerSystem(TimeUtil.nanoSupplier(), () -> 0, false);
            CompletableFuture<Void> completableFuture = reloader.reload(synchronizer, resourceManager, profilerSystem, profilerSystem2, preparation -> prepare.execute(() -> {
                long l = TimeUtil.measuringTimeNano();
                preparation.run();
                atomicLong.addAndGet(TimeUtil.measuringTimeNano() - l);
            }), application -> apply.execute(() -> {
                long l = TimeUtil.measuringTimeNano();
                application.run();
                atomicLong2.addAndGet(TimeUtil.measuringTimeNano() - l);
            }));
            return completableFuture.thenApplyAsync(void_ -> {
                LOGGER.debug("Finished reloading " + reloader.getName());
                return new Summary(reloader.getName(), profilerSystem.getResult(), profilerSystem2.getResult(), atomicLong, atomicLong2);
            }, applyExecutor);
        }, initialStage);
        this.reloadTimer.start();
        this.applyStageFuture.thenAcceptAsync(this::finish, applyExecutor);
    }

    private void finish(List<Summary> summaries) {
        this.reloadTimer.stop();
        int i = 0;
        LOGGER.info("Resource reload finished after {} ms", (Object)this.reloadTimer.elapsed(TimeUnit.MILLISECONDS));
        for (Summary summary : summaries) {
            ProfileResult profileResult = summary.prepareProfile;
            ProfileResult profileResult2 = summary.applyProfile;
            int j = (int)((double)summary.prepareTimeMs.get() / 1000000.0);
            int k = (int)((double)summary.applyTimeMs.get() / 1000000.0);
            int l = j + k;
            String string = summary.name;
            LOGGER.info("{} took approximately {} ms ({} ms preparing, {} ms applying)", (Object)string, (Object)l, (Object)j, (Object)k);
            i += k;
        }
        LOGGER.info("Total blocking time: {} ms", (Object)i);
    }

    public static class Summary {
        final String name;
        final ProfileResult prepareProfile;
        final ProfileResult applyProfile;
        final AtomicLong prepareTimeMs;
        final AtomicLong applyTimeMs;

        Summary(String name, ProfileResult prepareProfile, ProfileResult applyProfile, AtomicLong prepareTimeMs, AtomicLong applyTimeMs) {
            this.name = name;
            this.prepareProfile = prepareProfile;
            this.applyProfile = applyProfile;
            this.prepareTimeMs = prepareTimeMs;
            this.applyTimeMs = applyTimeMs;
        }
    }
}

