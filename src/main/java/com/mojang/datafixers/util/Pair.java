package com.mojang.datafixers.util;

import com.mojang.datafixers.kinds.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Pair<F, S> implements App<Pair.Mu<S>, F> {
   private final F first;
   private final S second;

   public static <F, S> Pair<F, S> unbox(App<Pair.Mu<S>, F> box) {
      return (Pair)box;
   }

   public Pair(F first, S second) {
      this.first = first;
      this.second = second;
   }

   public F getFirst() {
      return this.first;
   }

   public S getSecond() {
      return this.second;
   }

   public Pair<S, F> swap() {
      return of(this.second, this.first);
   }

   public String toString() {
      return "(" + this.first + ", " + this.second + ")";
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Pair)) {
         return false;
      } else {
         Pair<?, ?> other = (Pair)obj;
         return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
      }
   }

   public int hashCode() {
      return com.google.common.base.Objects.hashCode(this.first, this.second);
   }

   public <F2> Pair<F2, S> mapFirst(Function<? super F, ? extends F2> function) {
      return of(function.apply(this.first), this.second);
   }

   public <S2> Pair<F, S2> mapSecond(Function<? super S, ? extends S2> function) {
      return of(this.first, function.apply(this.second));
   }

   public static <F, S> Pair<F, S> of(F first, S second) {
      return new Pair(first, second);
   }

   public static <F, S> Collector<Pair<F, S>, ?, Map<F, S>> toMap() {
      return Collectors.toMap(Pair::getFirst, Pair::getSecond);
   }

   public static final class Instance<S2> implements Traversable<Mu<S2>, Instance.Mu<S2>>, CartesianLike<Mu<S2>, S2, Instance.Mu<S2>> {
      public <T, R> App<Pair.Mu<S2>, R> map(Function<? super T, ? extends R> func, App<Pair.Mu<S2>, T> ts) {
         return Pair.unbox(ts).mapFirst(func);
      }

      public <F extends K1, A, B> App<F, App<Pair.Mu<S2>, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<Pair.Mu<S2>, A> input) {
         Pair<A, S2> pair = Pair.unbox(input);
         return applicative.ap((b) -> Pair.of(b, pair.second), (App)function.apply(pair.first));
      }

      public <A> App<Pair.Mu<S2>, A> to(App<Pair.Mu<S2>, A> input) {
         return input;
      }

      public <A> App<Pair.Mu<S2>, A> from(App<Pair.Mu<S2>, A> input) {
         return input;
      }

      public static final class Mu<S2> implements Traversable.Mu, CartesianLike.Mu {
      }
   }

   public static final class Mu<S> implements K1 {
   }
}
