package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface MapLike<T> {
   @Nullable
   T get(T var1);

   @Nullable
   T get(String var1);

   Stream<Pair<T, T>> entries();

   static <T> MapLike<T> forMap(final Map<T, T> map, final DynamicOps<T> ops) {
      return new MapLike<T>() {
         @Nullable
         public T get(T key) {
            return map.get(key);
         }

         @Nullable
         public T get(String key) {
            return this.get(ops.createString(key));
         }

         public Stream<Pair<T, T>> entries() {
            return map.entrySet().stream().map((e) -> Pair.of(e.getKey(), e.getValue()));
         }

         public String toString() {
            return "MapLike[" + map + "]";
         }
      };
   }
}
