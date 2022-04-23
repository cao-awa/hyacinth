package com.mojang.serialization;

public class Lifecycle {
   private static final Lifecycle STABLE = new Lifecycle() {
      public String toString() {
         return "Stable";
      }
   };
   private static final Lifecycle EXPERIMENTAL = new Lifecycle() {
      public String toString() {
         return "Experimental";
      }
   };

   private Lifecycle() {
   }

   public static Lifecycle experimental() {
      return EXPERIMENTAL;
   }

   public static Lifecycle stable() {
      return STABLE;
   }

   public static Lifecycle deprecated(int since) {
      return new Lifecycle.Deprecated(since);
   }

   public Lifecycle add(Lifecycle other) {
      if (this != EXPERIMENTAL && other != EXPERIMENTAL) {
         if (this instanceof Lifecycle.Deprecated) {
            return other instanceof Lifecycle.Deprecated && ((Lifecycle.Deprecated)other).since < ((Lifecycle.Deprecated)this).since ? other : this;
         } else {
            return other instanceof Lifecycle.Deprecated ? other : STABLE;
         }
      } else {
         return EXPERIMENTAL;
      }
   }

   // $FF: synthetic method
   Lifecycle(Object x0) {
      this();
   }

   public static final class Deprecated extends Lifecycle {
      private final int since;

      public Deprecated(int since) {
         super(null);
         this.since = since;
      }

      public int since() {
         return this.since;
      }
   }
}
