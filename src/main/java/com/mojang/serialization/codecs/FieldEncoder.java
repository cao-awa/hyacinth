package com.mojang.serialization.codecs;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.stream.Stream;

public class FieldEncoder<A> extends MapEncoder.Implementation<A> {
   private final String name;
   private final Encoder<A> elementCodec;

   public FieldEncoder(String name, Encoder<A> elementCodec) {
      this.name = name;
      this.elementCodec = elementCodec;
   }

   public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
      return prefix.add(this.name, this.elementCodec.encodeStart(ops, input));
   }

   public <T> Stream<T> keys(DynamicOps<T> ops) {
      return Stream.of(ops.createString(this.name));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         FieldEncoder<?> that = (FieldEncoder<?>)o;
         return Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(this.name, this.elementCodec);
   }

   public String toString() {
      return "FieldEncoder[" + this.name + ": " + this.elementCodec + ']';
   }
}
