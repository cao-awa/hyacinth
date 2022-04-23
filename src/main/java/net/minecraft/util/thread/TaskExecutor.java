/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.thread;

import com.github.zhuaidadaya.rikaishinikui.handler.entrust.EntrustParser;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.profiler.SampleType;
import net.minecraft.util.profiler.Sampler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskExecutor<T>
implements SampleableExecutor,
MessageListener<T>,
AutoCloseable,
Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int field_29940 = 1;
    private static final int field_29941 = 2;
    private final AtomicInteger stateFlags = new AtomicInteger(0);
    private final TaskQueue<? super T, ? extends Runnable> queue;
    private final Executor executor;
    private final String name;

    public static TaskExecutor<Runnable> create(Executor executor, String name) {
        return new TaskExecutor<Runnable>(new TaskQueue.Simple(new ConcurrentLinkedQueue()), executor, name);
    }

    public TaskExecutor(TaskQueue<? super T, ? extends Runnable> queue, Executor executor, String name) {
        this.executor = executor;
        this.queue = queue;
        this.name = name;
        ExecutorSampling.INSTANCE.add(this);
    }

    private boolean unpause() {
        int i;
        do {
            if (((i = this.stateFlags.get()) & 3) == 0) continue;
            return false;
        } while (!this.stateFlags.compareAndSet(i, i | 2));
        return true;
    }

    private void pause() {
        int i;
        while (!this.stateFlags.compareAndSet(i = this.stateFlags.get(), i & 0xFFFFFFFD)) {
        }
    }

    private boolean hasMessages() {
        if ((this.stateFlags.get() & 1) != 0) {
            return false;
        }
        return !this.queue.isEmpty();
    }

    @Override
    public void close() {
        int i;
        while (!this.stateFlags.compareAndSet(i = this.stateFlags.get(), i | 1)) {
        }
    }

    private boolean isUnpaused() {
        return (this.stateFlags.get() & 2) != 0;
    }

    private boolean runNext() {
        if (!this.isUnpaused()) {
            return false;
        }
        Runnable runnable = this.queue.poll();
        if (runnable == null) {
            return false;
        }
        EntrustParser.debugRunnable(this.name, runnable).run();
        return true;
    }

    @Override
    public void run() {
        try {
            this.runWhile(runCount -> runCount == 0);
        }
        finally {
            this.pause();
            this.execute();
        }
    }

    public void awaitAll() {
        try {
            this.runWhile(runCount -> true);
        }
        finally {
            this.pause();
            this.execute();
        }
    }

    @Override
    public void send(T message) {
        this.queue.add(message);
        this.execute();
    }

    private void execute() {
        if (this.hasMessages() && this.unpause()) {
            try {
                this.executor.execute(this);
            }
            catch (RejectedExecutionException rejectedExecutionException) {
                try {
                    this.executor.execute(this);
                }
                catch (RejectedExecutionException rejectedExecutionException2) {
                    LOGGER.error("Cound not schedule mailbox", (Throwable)rejectedExecutionException2);
                }
            }
        }
    }

    /**
     * @param condition checks whether to run another task given the run task count
     */
    private int runWhile(Int2BooleanFunction condition) {
        int i = 0;
        while (condition.get(i) && this.runNext()) {
            ++i;
        }
        return i;
    }

    public int getQueueSize() {
        return this.queue.getSize();
    }

    public String toString() {
        return this.name + " " + this.stateFlags.get() + " " + this.queue.isEmpty();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Sampler> createSamplers() {
        return ImmutableList.of(Sampler.create(this.name + "-queue-size", SampleType.MAIL_BOXES, this::getQueueSize));
    }
}

