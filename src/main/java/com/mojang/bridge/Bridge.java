package com.mojang.bridge;

import com.mojang.bridge.launcher.Launcher;
import com.mojang.bridge.launcher.LauncherProvider;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Bridge {
   private static boolean INITIALIZED;
   private static Launcher LAUNCHER;

   private Bridge() {
   }

   public static Launcher getLauncher() {
      if (!INITIALIZED) {
         Class var0 = Bridge.class;
         synchronized(Bridge.class) {
            if (!INITIALIZED) {
               LAUNCHER = createLauncher();
               INITIALIZED = true;
            }
         }
      }

      return LAUNCHER;
   }

   private static Launcher createLauncher() {
      Iterator var0 = ServiceLoader.load(LauncherProvider.class).iterator();

      Launcher launcher;
      do {
         if (!var0.hasNext()) {
            return null;
         }

         LauncherProvider provider = (LauncherProvider)var0.next();
         launcher = provider.createLauncher();
      } while(launcher == null);

      return launcher;
   }
}
