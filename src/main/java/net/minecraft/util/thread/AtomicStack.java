/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.thread;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * A fixed-size atomic stack, useful for tracking multithreaded access to
 * an object. When the stack is full on addition, it overrides the earliest
 * content in the stack.
 * 
 * @apiNote Vanilla uses this for debugging purpose on paletted container and
 * chunk holder's asynchronous access checks.
 */
public class AtomicStack<T> {
    private final AtomicReferenceArray<T> contents;
    private final AtomicInteger size;

    public AtomicStack(int maxSize) {
        this.contents = new AtomicReferenceArray(maxSize);
        this.size = new AtomicInteger(0);
    }

    /**
     * Adds a value to this stack.
     * 
     * <p>If the stack is already at full capacity, the earliest pushed item in
     * the stack is discarded.
     * 
     * @param value the value to add
     */
    public void push(T value) {
        int k;
        int j;
        int i = this.contents.length();
        while (!this.size.compareAndSet(j = this.size.get(), k = (j + 1) % i)) {
        }
        this.contents.set(k, value);
    }

    /**
     * Builds a list of the contents of the stack.
     * 
     * <p>The more recently pushed elements will appear earlier in the returned
     * list. The returned list is immutable and its size won't exceed this stack's
     * size.
     * 
     * @return a list of contents
     */
    public List<T> toList() {
        int i = this.size.get();
        ImmutableList.Builder builder = ImmutableList.builder();
        for (int j = 0; j < this.contents.length(); ++j) {
            int k = Math.floorMod(i - j, this.contents.length());
            T object = this.contents.get(k);
            if (object == null) continue;
            builder.add(object);
        }
        return builder.build();
    }
}

