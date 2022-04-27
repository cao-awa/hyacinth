package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.Objects;

public record UnboundedMapCodec<K, V>(Codec<K> keyCodec,
                                      Codec<V> elementCodec) implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
   public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
      return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap((map) -> this.decode(ops, map)).map((r) -> Pair.of(r, input));
   }

   public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
      return this.encode(input, ops, ops.mapBuilder()).build(prefix);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         UnboundedMapCodec<?, ?> that = (UnboundedMapCodec<?,?>) o;
         return Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(this.keyCodec, this.elementCodec);
   }

   public String toString() {
      return "UnboundedMapCodec[" + this.keyCodec + " -> " + this.elementCodec + ']';
   }
}
