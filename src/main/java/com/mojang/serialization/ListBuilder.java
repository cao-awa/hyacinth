package com.mojang.serialization;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.UnaryOperator;

public interface ListBuilder<T> {
   DynamicOps<T> ops();

   DataResult<T> build(T var1);

   ListBuilder<T> add(T var1);

   ListBuilder<T> add(DataResult<T> var1);

   ListBuilder<T> withErrorsFrom(DataResult<?> var1);

   ListBuilder<T> mapError(UnaryOperator<String> var1);

   default DataResult<T> build(DataResult<T> prefix) {
      return prefix.flatMap(this::build);
   }

   default <E> ListBuilder<T> add(E value, Encoder<E> encoder) {
      return this.add(encoder.encodeStart(this.ops(), value));
   }

   default <E> ListBuilder<T> addAll(Iterable<E> values, Encoder<E> encoder) {
      values.forEach((v) -> {
         encoder.encode(v, this.ops(), this.ops().empty());
      });
      return this;
   }

   public static final class Builder<T> implements ListBuilder<T> {
      private final DynamicOps<T> ops;
      private DataResult<ImmutableList.Builder<T>> builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());

      public Builder(DynamicOps<T> ops) {
         this.ops = ops;
      }

      public DynamicOps<T> ops() {
         return this.ops;
      }

      public ListBuilder<T> add(T value) {
         this.builder = this.builder.map((b) -> {
            return b.add(value);
         });
         return this;
      }

      public ListBuilder<T> add(DataResult<T> value) {
         this.builder = this.builder.apply2stable(ImmutableList.Builder::add, value);
         return this;
      }

      public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
         this.builder = this.builder.flatMap((r) -> {
            return result.map((v) -> {
               return r;
            });
         });
         return this;
      }

      public ListBuilder<T> mapError(UnaryOperator<String> onError) {
         this.builder = this.builder.mapError(onError);
         return this;
      }

      public DataResult<T> build(T prefix) {
         DataResult<T> result = this.builder.flatMap((b) -> {
            return this.ops.mergeToList(prefix, (List)b.build());
         });
         this.builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
         return result;
      }
   }
}
