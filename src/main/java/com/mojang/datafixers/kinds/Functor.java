package com.mojang.datafixers.kinds;

import java.util.function.Function;

public interface Functor<F extends K1, Mu extends Functor.Mu> extends Kind1<F, Mu> {
   static <F extends K1, Mu extends Functor.Mu> Functor<F, Mu> unbox(App<Mu, F> proofBox) {
      return (Functor)proofBox;
   }

   <T, R> App<F, R> map(Function<? super T, ? extends R> var1, App<F, T> var2);

   public interface Mu extends Kind1.Mu {
   }
}
