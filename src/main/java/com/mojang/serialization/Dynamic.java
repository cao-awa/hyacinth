package com.mojang.serialization;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class Dynamic<T> extends DynamicLike<T> {
   private final T value;

   public Dynamic(DynamicOps<T> ops) {
      this(ops, ops.empty());
   }

   public Dynamic(DynamicOps<T> ops, @Nullable T value) {
      super(ops);
      this.value = value == null ? ops.empty() : value;
   }

   public T getValue() {
      return this.value;
   }

   public Dynamic<T> map(Function<? super T, ? extends T> function) {
      return new Dynamic(this.ops, function.apply(this.value));
   }

   public <U> Dynamic<U> castTyped(DynamicOps<U> ops) {
      if (!Objects.equals(this.ops, ops)) {
         throw new IllegalStateException("Dynamic type doesn't match");
      } else {
         return (Dynamic<U>) this;
      }
   }

   public <U> U cast(DynamicOps<U> ops) {
      return this.castTyped(ops).getValue();
   }

   public OptionalDynamic<T> merge(Dynamic<?> value) {
      DataResult<T> merged = this.ops.mergeToList(this.value, value.cast(this.ops));
      return new OptionalDynamic(this.ops, merged.map((m) -> {
         return new Dynamic(this.ops, m);
      }));
   }

   public OptionalDynamic<T> merge(Dynamic<?> key, Dynamic<?> value) {
      DataResult<T> merged = this.ops.mergeToMap(this.value, key.cast(this.ops), value.cast(this.ops));
      return new OptionalDynamic(this.ops, merged.map((m) -> {
         return new Dynamic(this.ops, m);
      }));
   }

   public DataResult<Map<Dynamic<T>, Dynamic<T>>> getMapValues() {
      return this.ops.getMapValues(this.value).map((map) -> {
         ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder();
         map.forEach((entry) -> {
            builder.put(new Dynamic(this.ops, entry.getFirst()), new Dynamic(this.ops, entry.getSecond()));
         });
         return builder.build();
      });
   }

   public Dynamic<T> updateMapValues(Function<Pair<Dynamic<?>, Dynamic<?>>, Pair<Dynamic<?>, Dynamic<?>>> updater) {
      return (Dynamic)DataFixUtils.orElse(this.getMapValues().map((map) -> {
         return (Map)map.entrySet().stream().map((e) -> {
            Pair<Dynamic<?>, Dynamic<?>> pair = (Pair)updater.apply(Pair.of(e.getKey(), e.getValue()));
            return Pair.of(((Dynamic)pair.getFirst()).castTyped(this.ops), ((Dynamic)pair.getSecond()).castTyped(this.ops));
         }).collect(Pair.toMap());
      }).map(this::createMap).result(), this);
   }

   public DataResult<Number> asNumber() {
      return this.ops.getNumberValue(this.value);
   }

   public DataResult<String> asString() {
      return this.ops.getStringValue(this.value);
   }

   public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
      return this.ops.getStream(this.value).map((s) -> {
         return s.map((e) -> {
            return new Dynamic(this.ops, e);
         });
      });
   }

   public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
      return this.ops.getMapValues(this.value).map((s) -> {
         return s.map((p) -> {
            return Pair.of(new Dynamic(this.ops, p.getFirst()), new Dynamic(this.ops, p.getSecond()));
         });
      });
   }

   public DataResult<ByteBuffer> asByteBufferOpt() {
      return this.ops.getByteBuffer(this.value);
   }

   public DataResult<IntStream> asIntStreamOpt() {
      return this.ops.getIntStream(this.value);
   }

   public DataResult<LongStream> asLongStreamOpt() {
      return this.ops.getLongStream(this.value);
   }

   public OptionalDynamic<T> get(String key) {
      return new OptionalDynamic(this.ops, this.ops.getMap(this.value).flatMap((m) -> {
         T value = m.get(key);
         return value == null ? DataResult.error("key missing: " + key + " in " + this.value) : DataResult.success(new Dynamic(this.ops, value));
      }));
   }

   public DataResult<T> getGeneric(T key) {
      return this.ops.getGeneric(this.value, key);
   }

   public Dynamic<T> remove(String key) {
      return this.map((v) -> {
         return this.ops.remove(v, key);
      });
   }

   public Dynamic<T> set(String key, Dynamic<?> value) {
      return this.map((v) -> {
         return this.ops.set(v, key, value.cast(this.ops));
      });
   }

   public Dynamic<T> update(String key, Function<Dynamic<?>, Dynamic<?>> function) {
      return this.map((v) -> this.ops.update(v, key, (value) -> (function.apply(new Dynamic(this.ops, value))).cast(this.ops)));
   }

   public Dynamic<T> updateGeneric(T key, Function<T, T> function) {
      return this.map((v) -> this.ops.updateGeneric(v, key, function));
   }

   public DataResult<T> getElement(String key) {
      return this.getElementGeneric(this.ops.createString(key));
   }

   public DataResult<T> getElementGeneric(T key) {
      return this.ops.getGeneric(this.value, key);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Dynamic<?> dynamic = (Dynamic)o;
         return Objects.equals(this.ops, dynamic.ops) && Objects.equals(this.value, dynamic.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(this.ops, this.value);
   }

   public String toString() {
      return String.format("%s[%s]", this.ops, this.value);
   }

   public <R> Dynamic<R> convert(DynamicOps<R> outOps) {
      return new Dynamic(outOps, convert(this.ops, outOps, this.value));
   }

   public <V> V into(Function<? super Dynamic<T>, ? extends V> action) {
      return action.apply(this);
   }

   public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> decoder) {
      return decoder.decode(this.ops, this.value).map((p) -> {
         return p.mapFirst(Function.identity());
      });
   }

   public static <S, T> T convert(DynamicOps<S> inOps, DynamicOps<T> outOps, S input) {
      return Objects.equals(inOps, outOps) ? (T) input : inOps.convertTo(outOps, input);
   }
}
