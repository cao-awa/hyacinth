package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function3<T1, T2, T3, R> {
   R apply(T1 var1, T2 var2, T3 var3);

   default Function<T1, BiFunction<T2, T3, R>> curry() {
      return (t1) -> {
         return (t2, t3) -> {
            return this.apply(t1, t2, t3);
         };
      };
   }

   default BiFunction<T1, T2, Function<T3, R>> curry2() {
      return (t1, t2) -> {
         return (t3) -> {
            return this.apply(t1, t2, t3);
         };
      };
   }
}
