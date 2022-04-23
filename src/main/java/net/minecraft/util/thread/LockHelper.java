/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.thread;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class LockHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private final String name;
    private final Semaphore semaphore = new Semaphore(1);
    private final Lock lock = new ReentrantLock();
    @Nullable
    private volatile Thread thread;

    public LockHelper(String name) {
        this.name = name;
    }

    public void lock() {
        block6: {
            boolean bl = false;
            try {
                this.lock.lock();
                if (this.semaphore.tryAcquire()) break block6;
                this.thread = Thread.currentThread();
                bl = true;
                this.lock.unlock();
                try {
                    this.semaphore.acquire();
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
            finally {
                if (!bl) {
                    this.lock.unlock();
                }
            }
        }
    }

    public void unlock() {
        try {
            this.lock.lock();
            Thread thread = this.thread;
            if (thread != null) {
                this.semaphore.release();
            }
            this.semaphore.release();
        }
        finally {
            this.lock.unlock();
        }
    }

    private static String method_39936(Thread thread) {
        return thread.getName() + ": \n\tat " + Arrays.stream(thread.getStackTrace()).map(Object::toString).collect(Collectors.joining("\n\tat "));
    }
}

