package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalFieldCodec<A> extends MapCodec<Optional<A>> {
   private final String name;
   private final Codec<A> elementCodec;

   public OptionalFieldCodec(String name, Codec<A> elementCodec) {
      this.name = name;
      this.elementCodec = elementCodec;
   }

   public <T> DataResult<Optional<A>> decode(DynamicOps<T> ops, MapLike<T> input) {
      T value = input.get(this.name);
      if (value == null) {
         return DataResult.success(Optional.empty());
      } else {
         DataResult<A> parsed = this.elementCodec.parse(ops, value);
         return parsed.result().isPresent() ? parsed.map(Optional::of) : DataResult.success(Optional.empty());
      }
   }

   public <T> RecordBuilder<T> encode(Optional<A> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
      return input.isPresent() ? prefix.add(this.name, this.elementCodec.encodeStart(ops, input.get())) : prefix;
   }

   public <T> Stream<T> keys(DynamicOps<T> ops) {
      return Stream.of(ops.createString(this.name));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         OptionalFieldCodec<?> that = (OptionalFieldCodec)o;
         return Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(this.name, this.elementCodec);
   }

   public String toString() {
      return "OptionalFieldCodec[" + this.name + ": " + this.elementCodec + ']';
   }
}
