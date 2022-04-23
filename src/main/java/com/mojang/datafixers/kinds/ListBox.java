package com.mojang.datafixers.kinds;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ListBox<T> implements App<ListBox.Mu, T> {
   private final List<T> value;

   public static <T> List<T> unbox(App<ListBox.Mu, T> box) {
      return ((ListBox)box).value;
   }

   public static <T> ListBox<T> create(List<T> value) {
      return new ListBox(value);
   }

   private ListBox(List<T> value) {
      this.value = value;
   }

   public static <F extends K1, A, B> App<F, List<B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, List<A> input) {
      return applicative.map(ListBox::unbox, ListBox.Instance.INSTANCE.traverse(applicative, function, create(input)));
   }

   public static <F extends K1, A> App<F, List<A>> flip(Applicative<F, ?> applicative, List<App<F, A>> input) {
      return applicative.map(ListBox::unbox, ListBox.Instance.INSTANCE.flip(applicative, create(input)));
   }

   public enum Instance implements Traversable<ListBox.Mu, ListBox.Instance.Mu> {
      INSTANCE;

      public <T, R> App<ListBox.Mu, R> map(Function<? super T, ? extends R> func, App<ListBox.Mu, T> ts) {
         return ListBox.create(ListBox.unbox(ts).stream().map(func).collect(Collectors.toList()));
      }

      public <F extends K1, A, B> App<F, App<ListBox.Mu, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<ListBox.Mu, A> input) {
         List<? extends A> list = ListBox.unbox(input);
         App<F, ImmutableList.Builder<B>> result = applicative.point(ImmutableList.builder());

         App fb;
         for(Iterator<? extends A> var6 = list.iterator(); var6.hasNext(); result = applicative.ap2(applicative.ap(e -> null, null), result, fb)) {
            fb = function.apply(var6.next());
         }

         return applicative.map((b) -> ListBox.create(b.build()), result);
      }

      public static final class Mu implements Traversable.Mu {
      }
   }

   public static final class Mu implements K1 {
   }
}
