package com.mojang.datafixers;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function12;
import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function14;
import com.mojang.datafixers.util.Function15;
import com.mojang.datafixers.util.Function16;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function9;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Products {
   static <T1, T2> Products.P2<IdF.Mu, T1, T2> of(T1 t1, T2 t2) {
      return new Products.P2(IdF.create(t1), IdF.create(t2));
   }

   public static final class P16<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;
      private final App<F, T11> t11;
      private final App<F, T12> t12;
      private final App<F, T13> t13;
      private final App<F, T14> t14;
      private final App<F, T15> t15;
      private final App<F, T16> t16;

      public P16(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15, App<F, T16> t16) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
         this.t11 = t11;
         this.t12 = t12;
         this.t13 = t13;
         this.t14 = t14;
         this.t15 = t15;
         this.t16 = t16;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> function) {
         return instance.ap16(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15, this.t16);
      }
   }

   public static final class P15<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;
      private final App<F, T11> t11;
      private final App<F, T12> t12;
      private final App<F, T13> t13;
      private final App<F, T14> t14;
      private final App<F, T15> t15;

      public P15(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
         this.t11 = t11;
         this.t12 = t12;
         this.t13 = t13;
         this.t14 = t14;
         this.t15 = t15;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> function) {
         return instance.ap15(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15);
      }
   }

   public static final class P14<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;
      private final App<F, T11> t11;
      private final App<F, T12> t12;
      private final App<F, T13> t13;
      private final App<F, T14> t14;

      public P14(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
         this.t11 = t11;
         this.t12 = t12;
         this.t13 = t13;
         this.t14 = t14;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> function) {
         return instance.ap14(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14);
      }
   }

   public static final class P13<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;
      private final App<F, T11> t11;
      private final App<F, T12> t12;
      private final App<F, T13> t13;

      public P13(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
         this.t11 = t11;
         this.t12 = t12;
         this.t13 = t13;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> function) {
         return instance.ap13(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13);
      }
   }

   public static final class P12<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;
      private final App<F, T11> t11;
      private final App<F, T12> t12;

      public P12(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
         this.t11 = t11;
         this.t12 = t12;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> function) {
         return instance.ap12(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12);
      }
   }

   public static final class P11<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;
      private final App<F, T11> t11;

      public P11(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
         this.t11 = t11;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> function) {
         return instance.ap11(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11);
      }
   }

   public static final class P10<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;
      private final App<F, T10> t10;

      public P10(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
         this.t10 = t10;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> function) {
         return instance.ap10(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10);
      }
   }

   public static final class P9<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;
      private final App<F, T9> t9;

      public P9(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
         this.t9 = t9;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> function) {
         return instance.ap9(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9);
      }
   }

   public static final class P8<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;
      private final App<F, T8> t8;

      public P8(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
         this.t8 = t8;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public App<F, T3> t3() {
         return this.t3;
      }

      public App<F, T4> t4() {
         return this.t4;
      }

      public App<F, T5> t5() {
         return this.t5;
      }

      public App<F, T6> t6() {
         return this.t6;
      }

      public App<F, T7> t7() {
         return this.t7;
      }

      public App<F, T8> t8() {
         return this.t8;
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> function) {
         return instance.ap8(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8);
      }
   }

   public static final class P7<F extends K1, T1, T2, T3, T4, T5, T6, T7> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;
      private final App<F, T7> t7;

      public P7(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
         this.t7 = t7;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public App<F, T3> t3() {
         return this.t3;
      }

      public App<F, T4> t4() {
         return this.t4;
      }

      public App<F, T5> t5() {
         return this.t5;
      }

      public App<F, T6> t6() {
         return this.t6;
      }

      public App<F, T7> t7() {
         return this.t7;
      }

      public <T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(App<F, T8> t8) {
         return new Products.P8(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, t8);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function7<T1, T2, T3, T4, T5, T6, T7, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> function) {
         return instance.ap7(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
      }
   }

   public static final class P6<F extends K1, T1, T2, T3, T4, T5, T6> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;
      private final App<F, T6> t6;

      public P6(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
         this.t6 = t6;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public App<F, T3> t3() {
         return this.t3;
      }

      public App<F, T4> t4() {
         return this.t4;
      }

      public App<F, T5> t5() {
         return this.t5;
      }

      public App<F, T6> t6() {
         return this.t6;
      }

      public <T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(App<F, T7> t7) {
         return new Products.P7(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, t7);
      }

      public <T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P2<F, T7, T8> p) {
         return new Products.P8(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, p.t1, p.t2);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function6<T1, T2, T3, T4, T5, T6, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function6<T1, T2, T3, T4, T5, T6, R>> function) {
         return instance.ap6(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6);
      }
   }

   public static final class P5<F extends K1, T1, T2, T3, T4, T5> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;
      private final App<F, T5> t5;

      public P5(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
         this.t5 = t5;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public App<F, T3> t3() {
         return this.t3;
      }

      public App<F, T4> t4() {
         return this.t4;
      }

      public App<F, T5> t5() {
         return this.t5;
      }

      public <T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(App<F, T6> t6) {
         return new Products.P6(this.t1, this.t2, this.t3, this.t4, this.t5, t6);
      }

      public <T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P2<F, T6, T7> p) {
         return new Products.P7(this.t1, this.t2, this.t3, this.t4, this.t5, p.t1, p.t2);
      }

      public <T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P3<F, T6, T7, T8> p) {
         return new Products.P8(this.t1, this.t2, this.t3, this.t4, this.t5, p.t1, p.t2, p.t3);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function5<T1, T2, T3, T4, T5, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function5<T1, T2, T3, T4, T5, R>> function) {
         return instance.ap5(function, this.t1, this.t2, this.t3, this.t4, this.t5);
      }
   }

   public static final class P4<F extends K1, T1, T2, T3, T4> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;
      private final App<F, T4> t4;

      public P4(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
         this.t4 = t4;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public App<F, T3> t3() {
         return this.t3;
      }

      public App<F, T4> t4() {
         return this.t4;
      }

      public <T5> Products.P5<F, T1, T2, T3, T4, T5> and(App<F, T5> t5) {
         return new Products.P5(this.t1, this.t2, this.t3, this.t4, t5);
      }

      public <T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P2<F, T5, T6> p) {
         return new Products.P6(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2);
      }

      public <T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P3<F, T5, T6, T7> p) {
         return new Products.P7(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2, p.t3);
      }

      public <T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P4<F, T5, T6, T7, T8> p) {
         return new Products.P8(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2, p.t3, p.t4);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function4<T1, T2, T3, T4, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function4<T1, T2, T3, T4, R>> function) {
         return instance.ap4(function, this.t1, this.t2, this.t3, this.t4);
      }
   }

   public static final class P3<F extends K1, T1, T2, T3> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;
      private final App<F, T3> t3;

      public P3(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3) {
         this.t1 = t1;
         this.t2 = t2;
         this.t3 = t3;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public App<F, T3> t3() {
         return this.t3;
      }

      public <T4> Products.P4<F, T1, T2, T3, T4> and(App<F, T4> t4) {
         return new Products.P4(this.t1, this.t2, this.t3, t4);
      }

      public <T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P2<F, T4, T5> p) {
         return new Products.P5(this.t1, this.t2, this.t3, p.t1, p.t2);
      }

      public <T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P3<F, T4, T5, T6> p) {
         return new Products.P6(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3);
      }

      public <T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P4<F, T4, T5, T6, T7> p) {
         return new Products.P7(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3, p.t4);
      }

      public <T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P5<F, T4, T5, T6, T7, T8> p) {
         return new Products.P8(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3, p.t4, p.t5);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function3<T1, T2, T3, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function3<T1, T2, T3, R>> function) {
         return instance.ap3(function, this.t1, this.t2, this.t3);
      }
   }

   public static final class P2<F extends K1, T1, T2> {
      private final App<F, T1> t1;
      private final App<F, T2> t2;

      public P2(App<F, T1> t1, App<F, T2> t2) {
         this.t1 = t1;
         this.t2 = t2;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public App<F, T2> t2() {
         return this.t2;
      }

      public <T3> Products.P3<F, T1, T2, T3> and(App<F, T3> t3) {
         return new Products.P3(this.t1, this.t2, t3);
      }

      public <T3, T4> Products.P4<F, T1, T2, T3, T4> and(Products.P2<F, T3, T4> p) {
         return new Products.P4(this.t1, this.t2, p.t1, p.t2);
      }

      public <T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P3<F, T3, T4, T5> p) {
         return new Products.P5(this.t1, this.t2, p.t1, p.t2, p.t3);
      }

      public <T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P4<F, T3, T4, T5, T6> p) {
         return new Products.P6(this.t1, this.t2, p.t1, p.t2, p.t3, p.t4);
      }

      public <T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P5<F, T3, T4, T5, T6, T7> p) {
         return new Products.P7(this.t1, this.t2, p.t1, p.t2, p.t3, p.t4, p.t5);
      }

      public <T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P6<F, T3, T4, T5, T6, T7, T8> p) {
         return new Products.P8(this.t1, this.t2, p.t1, p.t2, p.t3, p.t4, p.t5, p.t6);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, BiFunction<T1, T2, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, BiFunction<T1, T2, R>> function) {
         return instance.ap2(function, this.t1, this.t2);
      }
   }

   public static final class P1<F extends K1, T1> {
      private final App<F, T1> t1;

      public P1(App<F, T1> t1) {
         this.t1 = t1;
      }

      public App<F, T1> t1() {
         return this.t1;
      }

      public <T2> Products.P2<F, T1, T2> and(App<F, T2> t2) {
         return new Products.P2(this.t1, t2);
      }

      public <T2, T3> Products.P3<F, T1, T2, T3> and(Products.P2<F, T2, T3> p) {
         return new Products.P3(this.t1, p.t1, p.t2);
      }

      public <T2, T3, T4> Products.P4<F, T1, T2, T3, T4> and(Products.P3<F, T2, T3, T4> p) {
         return new Products.P4(this.t1, p.t1, p.t2, p.t3);
      }

      public <T2, T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P4<F, T2, T3, T4, T5> p) {
         return new Products.P5(this.t1, p.t1, p.t2, p.t3, p.t4);
      }

      public <T2, T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P5<F, T2, T3, T4, T5, T6> p) {
         return new Products.P6(this.t1, p.t1, p.t2, p.t3, p.t4, p.t5);
      }

      public <T2, T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P6<F, T2, T3, T4, T5, T6, T7> p) {
         return new Products.P7(this.t1, p.t1, p.t2, p.t3, p.t4, p.t5, p.t6);
      }

      public <T2, T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P7<F, T2, T3, T4, T5, T6, T7, T8> p) {
         return new Products.P8(this.t1, p.t1, p.t2, p.t3, p.t4, p.t5, p.t6, p.t7);
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, Function<T1, R> function) {
         return this.apply(instance, instance.point(function));
      }

      public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function<T1, R>> function) {
         return instance.ap(function, this.t1);
      }
   }
}
