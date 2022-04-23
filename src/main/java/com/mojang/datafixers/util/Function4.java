package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function4<T1, T2, T3, T4, R> {
   R apply(T1 var1, T2 var2, T3 var3, T4 var4);

   default Function<T1, Function3<T2, T3, T4, R>> curry() {
      return (t1) -> {
         return (t2, t3, t4) -> {
            return this.apply(t1, t2, t3, t4);
         };
      };
   }

   default BiFunction<T1, T2, BiFunction<T3, T4, R>> curry2() {
      return (t1, t2) -> {
         return (t3, t4) -> {
            return this.apply(t1, t2, t3, t4);
         };
      };
   }

   default Function3<T1, T2, T3, Function<T4, R>> curry3() {
      return (t1, t2, t3) -> {
         return (t4) -> {
            return this.apply(t1, t2, t3, t4);
         };
      };
   }
}
