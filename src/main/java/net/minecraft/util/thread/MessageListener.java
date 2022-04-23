/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.thread;

import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface MessageListener<Msg>
extends AutoCloseable {
    public String getName();

    public void send(Msg var1);

    @Override
    default public void close() {
    }

    /**
     * Asks a message provider for a message.
     * 
     * The {@link CompletableFuture} returned from this function will never complete exceptionally.
     * 
     * @return CompletableFuture future that completes with the received message
     */
    default public <Source> CompletableFuture<Source> ask(Function<? super MessageListener<Source>, ? extends Msg> messageProvider) {
        CompletableFuture completableFuture = new CompletableFuture();
        Msg object = messageProvider.apply(MessageListener.create("ask future procesor handle", value -> {
            completableFuture.complete(value);
        }));
        this.send(object);
        return completableFuture;
    }

    /**
     * Asks a fallible message provider for a message.
     * 
     * The provider is given a MessageListener that accepts a {@link Either} representing either
     * a valid response (generic parameter Source) or an Exception, which decides whether the
     * future completes successfully or exceptionally.
     * 
     * @return CompletableFuture that may either complete successfully or exceptionally
     */
    default public <Source> CompletableFuture<Source> askFallible(Function<? super MessageListener<Either<Source, Exception>>, ? extends Msg> messageProvider) {
        CompletableFuture completableFuture = new CompletableFuture();
        Msg object = messageProvider.apply(MessageListener.create("ask future procesor handle", either -> {
            either.ifLeft(completableFuture::complete);
            either.ifRight(completableFuture::completeExceptionally);
        }));
        this.send(object);
        return completableFuture;
    }

    public static <Msg> MessageListener<Msg> create(final String name, final Consumer<Msg> action) {
        return new MessageListener<Msg>(){

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void send(Msg message) {
                action.accept(message);
            }

            public String toString() {
                return name;
            }
        };
    }
}

