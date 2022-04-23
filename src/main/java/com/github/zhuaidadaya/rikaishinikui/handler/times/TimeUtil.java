package com.github.zhuaidadaya.rikaishinikui.handler.times;

import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class TimeUtil {
    public static final long SECOND_IN_MILLIS = TimeUnit.SECONDS.toNanos(1L);
    public static final long MILLI_IN_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);

//    public static UniformIntProvider betweenSeconds(int min, int max) {
//        return UniformIntProvider.create(min * 20, max * 20);
//    }

    public static long measuringTimeMillions() {
        return measuringTimeNano() / 1000000L;
    }

    public static long measuringTimeNano() {
        return nano();
    }

    public static long millions() {
        return System.currentTimeMillis();
    }

    public static long nano() {
        return System.nanoTime();
    }

    public static LongSupplier nanoSupplier() {
        return TimeUtil::nano;
    }

    public static long processMillion(long million) {
        return millions() - million;
    }

    public static long processNano(long nano) {
        return nano() - nano;
    }

    public static void sleep(long millions) throws InterruptedException {
        if (millions < 0)
            return;
        Thread.sleep(millions);
    }

    public static void barricade(long millions) throws InterruptedException {
        sleep(millions);
    }
}
