package com.mojang.datafixers.kinds;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class OptionalBox<T> implements App<OptionalBox.Mu, T> {
   private final Optional<T> value;

   public static <T> Optional<T> unbox(App<OptionalBox.Mu, T> box) {
      return ((OptionalBox)box).value;
   }

   public static <T> OptionalBox<T> create(Optional<T> value) {
      return new OptionalBox(value);
   }

   private OptionalBox(Optional<T> value) {
      this.value = value;
   }

   public static enum Instance implements Applicative<OptionalBox.Mu, OptionalBox.Instance.Mu>, Traversable<OptionalBox.Mu, OptionalBox.Instance.Mu> {
      INSTANCE;

      public <T, R> App<OptionalBox.Mu, R> map(Function<? super T, ? extends R> func, App<OptionalBox.Mu, T> ts) {
         return OptionalBox.create(OptionalBox.unbox(ts).map(func));
      }

      public <A> App<OptionalBox.Mu, A> point(A a) {
         return OptionalBox.create(Optional.of(a));
      }

      public <A, R> Function<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, R>> lift1(App<OptionalBox.Mu, Function<A, R>> function) {
         return (a) -> {
            return OptionalBox.create(OptionalBox.unbox(function).flatMap((f) -> {
               return OptionalBox.unbox(a).map(f);
            }));
         };
      }

      public <A, B, R> BiFunction<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, B>, App<OptionalBox.Mu, R>> lift2(App<OptionalBox.Mu, BiFunction<A, B, R>> function) {
         return (a, b) -> {
            return OptionalBox.create(OptionalBox.unbox(function).flatMap((f) -> {
               return OptionalBox.unbox(a).flatMap((av) -> {
                  return OptionalBox.unbox(b).map((bv) -> {
                     return f.apply(av, bv);
                  });
               });
            }));
         };
      }

      public <F extends K1, A, B> App<F, App<OptionalBox.Mu, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<OptionalBox.Mu, A> input) {
         Optional<App<F, B>> traversed = OptionalBox.unbox(input).map(function);
         return traversed.isPresent() ? applicative.map((b) -> {
            return OptionalBox.create(Optional.of(b));
         }, (App)traversed.get()) : applicative.point(OptionalBox.create(Optional.empty()));
      }

      public static final class Mu implements Applicative.Mu, Traversable.Mu {
      }
   }

   public static final class Mu implements K1 {
   }
}
