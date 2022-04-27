package net.minecraft.util.thread;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Queues;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import net.minecraft.util.profiler.SampleType;
import net.minecraft.util.profiler.Sampler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ThreadExecutor<R extends Runnable> implements SampleableExecutor, MessageListener<R>, Executor {
    private static final Logger LOGGER = LogManager.getLogger("Executor:Thread");
    private final String name;
    private final Queue<R> tasks = Queues.newConcurrentLinkedQueue();
    private int executionsInProgress;

    protected ThreadExecutor(String name) {
        this.name = name;
        ExecutorSampling.INSTANCE.add(this);
    }

    public <V> CompletableFuture<V> submit(Supplier<V> task) {
        if (this.shouldExecuteAsync()) {
            return CompletableFuture.supplyAsync(task, this);
        }
        return CompletableFuture.completedFuture(task.get());
    }

    protected boolean shouldExecuteAsync() {
        return ! this.isOnThread();
    }

    public boolean isOnThread() {
        return Thread.currentThread() == this.getThread();
    }

    protected abstract Thread getThread();

    public CompletableFuture<Void> submit(Runnable task) {
        if (this.shouldExecuteAsync()) {
            return this.submitAsync(task);
        }
        task.run();
        return CompletableFuture.completedFuture(null);
    }

    private CompletableFuture<Void> submitAsync(Runnable runnable) {
        return CompletableFuture.supplyAsync(() -> {
            runnable.run();
            return null;
        }, this);
    }

    public void submitAndJoin(Runnable runnable) {
        if (! this.isOnThread()) {
            this.submitAsync(runnable).join();
        } else {
            runnable.run();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (this.shouldExecuteAsync()) {
            this.send(this.createTask(runnable));
        } else {
            runnable.run();
        }
    }

    protected abstract R createTask(Runnable var1);

    protected void cancelTasks() {
        this.tasks.clear();
    }

    protected void runTasks() {
        while (this.runTask()) {
        }
    }

    public boolean runTask() {
        Runnable runnable = this.tasks.peek();
        if (runnable == null) {
            return false;
        }
        if (this.executionsInProgress == 0 && ! this.canExecute((R) runnable)) {
            return false;
        }
        this.executeTask(this.tasks.remove());
        return true;
    }

    protected abstract boolean canExecute(R var1);

    protected void executeTask(R task) {
        try {
            task.run();
        } catch (Exception exception) {
            LOGGER.fatal("Error executing task on {}", this.getName(), exception);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void send(R runnable) {
        this.tasks.add(runnable);
        LockSupport.unpark(this.getThread());
    }

    public void runTasks(BooleanSupplier stopCondition) {
        ++ this.executionsInProgress;
        try {
            while (! stopCondition.getAsBoolean()) {
                if (this.runTask())
                    continue;
                this.waitForTasks();
            }
        } finally {
            -- this.executionsInProgress;
        }
    }

    protected void waitForTasks() {
        Thread.yield();
        LockSupport.parkNanos("waiting for tasks", 100000L);
    }

    @Override
    public List<Sampler> createSamplers() {
        return ImmutableList.of(Sampler.create(this.name + "-pending-tasks", SampleType.EVENT_LOOPS, this::getTaskCount));
    }

    public int getTaskCount() {
        return this.tasks.size();
    }
}

