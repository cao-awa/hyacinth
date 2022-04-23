package com.mojang.serialization;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Keyable {
   <T> Stream<T> keys(DynamicOps<T> var1);

   static Keyable forStrings(final Supplier<Stream<String>> keys) {
      return new Keyable() {
         public <T> Stream<T> keys(DynamicOps<T> ops) {
            Stream<String> var10000 = keys.get();
            ops.getClass();
            return var10000.map(ops::createString);
         }
      };
   }
}
