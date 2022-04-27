package com.mojang.serialization.codecs;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import org.apache.commons.lang3.mutable.MutableObject;

public record ListCodec<A>(Codec<A> elementCodec) implements Codec<List<A>> {
   public <T> DataResult<T> encode(List<A> input, DynamicOps<T> ops, T prefix) {
      ListBuilder<T> builder = ops.listBuilder();

      for (A a : input) {
         builder.add(this.elementCodec.encodeStart(ops, a));
      }

      return builder.build(prefix);
   }

   public <T> DataResult<Pair<List<A>, T>> decode(DynamicOps<T> ops, T input) {
      return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap((stream) -> {
         ImmutableList.Builder<A> read = ImmutableList.builder();
         Builder<T> failed = Stream.builder();
         MutableObject<DataResult<Unit>> result = new MutableObject<>(DataResult.success(Unit.INSTANCE, Lifecycle.stable()));
         stream.accept((t) -> {
            DataResult<Pair<A, T>> element = this.elementCodec.decode(ops, t);
            element.error().ifPresent((e) -> failed.add(t));
            result.setValue(result.getValue().apply2stable((r, v) -> {
               read.add(v.getFirst());
               return r;
            }, element));
         });
         ImmutableList<A> elements = read.build();
         T errors = ops.createList(failed.build());
         Pair<List<A>, T> pair = Pair.of(elements, errors);
         return result.getValue().map((unit) -> pair).setPartial(pair);
      });
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ListCodec<?> listCodec = (ListCodec<?>) o;
         return Objects.equals(this.elementCodec, listCodec.elementCodec);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(this.elementCodec);
   }

   public String toString() {
      return "ListCodec[" + this.elementCodec + ']';
   }
}
