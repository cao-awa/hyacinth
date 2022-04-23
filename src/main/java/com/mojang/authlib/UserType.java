package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
   LEGACY("legacy"),
   MOJANG("mojang");

   private static final Map<String, UserType> BY_NAME = new HashMap();
   private final String name;

   private UserType(String name) {
      this.name = name;
   }

   public static UserType byName(String name) {
      return (UserType)BY_NAME.get(name.toLowerCase());
   }

   public String getName() {
      return this.name;
   }

   static {
      UserType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         UserType type = var0[var2];
         BY_NAME.put(type.name, type);
      }

   }
}
