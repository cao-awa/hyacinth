package com.mojang.serialization;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Function3;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public class DataResult<R> implements App<DataResult.Mu, R> {
    private final Either<R, DataResult.PartialResult<R>> result;
    private final Lifecycle lifecycle;

    private DataResult(Either<R, DataResult.PartialResult<R>> result, Lifecycle lifecycle) {
        this.result = result;
        this.lifecycle = lifecycle;
    }

    public static <R> DataResult<R> unbox(App<DataResult.Mu, R> box) {
        return (DataResult) box;
    }

    public static <R> DataResult<R> success(R result) {
        return success(result, Lifecycle.experimental());
    }

    public static <R> DataResult<R> error(String message, R partialResult) {
        return error(message, partialResult, Lifecycle.experimental());
    }

    public static <R> DataResult<R> error(String message) {
        return error(message, Lifecycle.experimental());
    }

    public static <R> DataResult<R> success(R result, Lifecycle experimental) {
        return new DataResult(Either.left(result), experimental);
    }

    public static <R> DataResult<R> error(String message, R partialResult, Lifecycle lifecycle) {
        return new DataResult(Either.right(new DataResult.PartialResult(message, Optional.of(partialResult))), lifecycle);
    }

    public static <R> DataResult<R> error(String message, Lifecycle lifecycle) {
        return new DataResult(Either.right(new DataResult.PartialResult(message, Optional.empty())), lifecycle);
    }

    public static <K, V> Function<K, DataResult<V>> partialGet(Function<K, V> partialGet, Supplier<String> errorPrefix) {
        return (name) -> Optional.ofNullable(partialGet.apply(name)).map(DataResult :: success).orElseGet(() -> error(errorPrefix.get() + name));
    }

    private static <R> DataResult<R> create(Either<R, DataResult.PartialResult<R>> result, Lifecycle lifecycle) {
        return new DataResult(result, lifecycle);
    }

    private static String appendMessages(String first, String second) {
        return first + "; " + second;
    }

    public static DataResult.Instance instance() {
        return DataResult.Instance.INSTANCE;
    }

    public Either<R, DataResult.PartialResult<R>> get() {
        return this.result;
    }

    public Optional<R> result() {
        return this.result.left();
    }

    public Lifecycle lifecycle() {
        return this.lifecycle;
    }

    public Optional<R> resultOrPartial(Consumer<String> onError) {
        return this.result.map(Optional :: of, (r) -> {
            onError.accept(r.message);
            return r.partialResult;
        });
    }

    public R getOrThrow(boolean allowPartial, Consumer<String> onError) {
        return this.result.map((l) -> l, (r) -> {
            onError.accept(r.message);
            if(allowPartial && r.partialResult.isPresent()) {
                return r.partialResult.get();
            } else {
                throw new RuntimeException(r.message);
            }
        });
    }

    public Optional<DataResult.PartialResult<R>> error() {
        return this.result.right();
    }

    public <T> DataResult<T> map(Function<? super R, ? extends T> function) {
        return create(this.result.mapBoth(function, (r) -> new PartialResult(r.message, r.partialResult.map(function))), this.lifecycle);
    }

    public DataResult<R> promotePartial(Consumer<String> onError) {
        return this.result.map((r) -> new DataResult(Either.left(r), this.lifecycle), (r) -> {
            onError.accept(r.message);
            return r.partialResult.map((pr) -> new DataResult(Either.left(pr), this.lifecycle)).orElseGet(() -> create(Either.right(r), this.lifecycle));
        });
    }

    public <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> function) {
        return this.result.map((l) -> {
            DataResult<R2> second = function.apply(l);
            return create(second.get(), this.lifecycle.add(second.lifecycle));
        }, (r) -> (DataResult) r.partialResult.map((value) -> {
            DataResult<R2> second = function.apply(value);
            return create(Either.right(second.get().map((l2) -> new PartialResult(r.message, Optional.of(l2)), (r2) -> new PartialResult(appendMessages(r.message, r2.message), r2.partialResult))), this.lifecycle.add(second.lifecycle));
        }).orElseGet(() -> create(Either.right(new PartialResult(r.message, Optional.empty())), this.lifecycle)));
    }

    public <R2> DataResult<R2> ap(DataResult<Function<R, R2>> functionResult) {
        return create(this.result.map((arg) -> functionResult.result.mapBoth((func) -> func.apply(arg), (funcError) -> new PartialResult(funcError.message, funcError.partialResult.map((f) -> f.apply(arg)))), (argError) -> Either.right(functionResult.result.map((func) -> new PartialResult(argError.message, argError.partialResult.map(func)), (funcError) -> new PartialResult(appendMessages(argError.message, funcError.message), argError.partialResult.flatMap((a) -> funcError.partialResult.map((f) -> f.apply(a))))))), this.lifecycle.add(functionResult.lifecycle));
    }

    public <R2, S> DataResult<S> apply2(BiFunction<R, R2, S> function, DataResult<R2> second) {
        return unbox(instance().apply2(function, this, second));
    }

    public <R2, S> DataResult<S> apply2stable(BiFunction<R, R2, S> function, DataResult<R2> second) {
        Applicative<DataResult.Mu, DataResult.Instance.Mu> instance = instance();
        DataResult<BiFunction<R, R2, S>> f = unbox(instance.point(function)).setLifecycle(Lifecycle.stable());
        return unbox(instance.ap2(f, this, second));
    }

    public <R2, R3, S> DataResult<S> apply3(Function3<R, R2, R3, S> function, DataResult<R2> second, DataResult<R3> third) {
        return unbox(instance().apply3(function, this, second, third));
    }

    public DataResult<R> setPartial(Supplier<R> partial) {
        return create(this.result.mapRight((r) -> {
            return new DataResult.PartialResult(r.message, Optional.of(partial.get()));
        }), this.lifecycle);
    }

    public DataResult<R> setPartial(R partial) {
        return create(this.result.mapRight((r) -> {
            return new DataResult.PartialResult(r.message, Optional.of(partial));
        }), this.lifecycle);
    }

    public DataResult<R> mapError(UnaryOperator<String> function) {
        return create(this.result.mapRight((r) -> {
            return new DataResult.PartialResult((String) function.apply(r.message), r.partialResult);
        }), this.lifecycle);
    }

    public DataResult<R> setLifecycle(Lifecycle lifecycle) {
        return create(this.result, lifecycle);
    }

    public DataResult<R> addLifecycle(Lifecycle lifecycle) {
        return create(this.result, this.lifecycle.add(lifecycle));
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            DataResult<?> that = (DataResult) o;
            return Objects.equals(this.result, that.result);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.result);
    }

    public String toString() {
        return "DataResult[" + this.result + ']';
    }

    public enum Instance implements Applicative<Mu, Instance.Mu> {
        INSTANCE;

        public <T, R> App<DataResult.Mu, R> map(Function<? super T, ? extends R> func, App<DataResult.Mu, T> ts) {
            return DataResult.unbox(ts).map(func);
        }

        public <A> App<DataResult.Mu, A> point(A a) {
            return DataResult.success(a);
        }

        public <A, R> Function<App<DataResult.Mu, A>, App<DataResult.Mu, R>> lift1(App<DataResult.Mu, Function<A, R>> function) {
            return (fa) -> this.ap(function, fa);
        }

        public <A, R> App<DataResult.Mu, R> ap(App<DataResult.Mu, Function<A, R>> func, App<DataResult.Mu, A> arg) {
            return DataResult.unbox(arg).ap(DataResult.unbox(func));
        }

        public <A, B, R> App<DataResult.Mu, R> ap2(App<DataResult.Mu, BiFunction<A, B, R>> func, App<DataResult.Mu, A> a, App<DataResult.Mu, B> b) {
            DataResult<BiFunction<A, B, R>> fr = DataResult.unbox(func);
            DataResult<A> ra = DataResult.unbox(a);
            DataResult<B> rb = DataResult.unbox(b);
            return fr.result.left().isPresent() && ra.result.left().isPresent() && rb.result.left().isPresent() ? new DataResult(Either.left(((BiFunction) fr.result.left().get()).apply(ra.result.left().get(), rb.result.left().get())), fr.lifecycle.add(ra.lifecycle).add(rb.lifecycle)) : Applicative.super.ap2(func, a, b);
        }

        public <T1, T2, T3, R> App<DataResult.Mu, R> ap3(App<DataResult.Mu, Function3<T1, T2, T3, R>> func, App<DataResult.Mu, T1> t1, App<DataResult.Mu, T2> t2, App<DataResult.Mu, T3> t3) {
            DataResult<Function3<T1, T2, T3, R>> fr = DataResult.unbox(func);
            DataResult<T1> dr1 = DataResult.unbox(t1);
            DataResult<T2> dr2 = DataResult.unbox(t2);
            DataResult<T3> dr3 = DataResult.unbox(t3);
            return fr.result.left().isPresent() && dr1.result.left().isPresent() && dr2.result.left().isPresent() && dr3.result.left().isPresent() ? new DataResult(Either.left(((Function3) fr.result.left().get()).apply(dr1.result.left().get(), dr2.result.left().get(), dr3.result.left().get())), fr.lifecycle.add(dr1.lifecycle).add(dr2.lifecycle).add(dr3.lifecycle)) : Applicative.super.ap3(func, t1, t2, t3);
        }

        public static final class Mu implements Applicative.Mu {
        }
    }

    public static class PartialResult<R> {
        private final String message;
        private final Optional<R> partialResult;

        public PartialResult(String message, Optional<R> partialResult) {
            this.message = message;
            this.partialResult = partialResult;
        }

        public <R2> DataResult.PartialResult<R2> map(Function<? super R, ? extends R2> function) {
            return new DataResult.PartialResult(this.message, this.partialResult.map(function));
        }

        public <R2> DataResult.PartialResult<R2> flatMap(Function<R, DataResult.PartialResult<R2>> function) {
            if(this.partialResult.isPresent()) {
                DataResult.PartialResult<R2> result = function.apply(this.partialResult.get());
                return new DataResult.PartialResult(DataResult.appendMessages(this.message, result.message), result.partialResult);
            } else {
                return new DataResult.PartialResult(this.message, Optional.empty());
            }
        }

        public String message() {
            return this.message;
        }

        public boolean equals(Object o) {
            if(this == o) {
                return true;
            } else if(o != null && this.getClass() == o.getClass()) {
                DataResult.PartialResult<?> that = (DataResult.PartialResult) o;
                return Objects.equals(this.message, that.message) && Objects.equals(this.partialResult, that.partialResult);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(this.message, this.partialResult);
        }

        public String toString() {
            return "DynamicException[" + this.message + ' ' + this.partialResult + ']';
        }
    }

    public static final class Mu implements K1 {
    }
}
