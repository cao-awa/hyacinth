package com.mojang.datafixers.kinds;

import com.mojang.datafixers.util.Pair;
import java.util.function.Function;

public interface CartesianLike<T extends K1, C, Mu extends CartesianLike.Mu> extends Functor<T, Mu>, Traversable<T, Mu> {
   static <F extends K1, C, Mu extends CartesianLike.Mu> CartesianLike<F, C, Mu> unbox(App<Mu, F> proofBox) {
      return (CartesianLike)proofBox;
   }

   <A> App<Pair.Mu<C>, A> to(App<T, A> var1);

   <A> App<T, A> from(App<Pair.Mu<C>, A> var1);

   default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input) {
      return applicative.map(var1 -> from((App<Pair.Mu<C>, A>)var1), (new Pair.Instance()).traverse(applicative, function, to(input)));
   }

   interface Mu extends Functor.Mu, Traversable.Mu {
   }
}
