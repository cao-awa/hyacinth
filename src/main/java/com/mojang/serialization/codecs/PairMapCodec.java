package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.stream.Stream;

public final class PairMapCodec<F, S> extends MapCodec<Pair<F, S>> {
   private final MapCodec<F> first;
   private final MapCodec<S> second;

   public PairMapCodec(MapCodec<F> first, MapCodec<S> second) {
      this.first = first;
      this.second = second;
   }

   public <T> DataResult<Pair<F, S>> decode(DynamicOps<T> ops, MapLike<T> input) {
      return this.first.decode(ops, input).flatMap((p1) -> this.second.decode(ops, input).map((p2) -> Pair.of(p1, p2)));
   }

   public <T> RecordBuilder<T> encode(Pair<F, S> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
      return this.first.encode(input.getFirst(), ops, this.second.encode(input.getSecond(), ops, prefix));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         PairMapCodec<?, ?> pairCodec = (PairMapCodec)o;
         return Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(this.first, this.second);
   }

   public String toString() {
      return "PairMapCodec[" + this.first + ", " + this.second + ']';
   }

   public <T> Stream<T> keys(DynamicOps<T> ops) {
      return Stream.concat(this.first.keys(ops), this.second.keys(ops));
   }
}
