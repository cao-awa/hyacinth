package com.mojang.authlib;

import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import java.util.Arrays;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvironmentParser {
   @Nullable
   private static String environmentOverride;
   private static final String PROP_PREFIX = "minecraft.api.";
   private static final Logger LOGGER = LogManager.getLogger();
   public static final String PROP_ENV = "minecraft.api.env";
   public static final String PROP_AUTH_HOST = "minecraft.api.auth.host";
   public static final String PROP_ACCOUNT_HOST = "minecraft.api.account.host";
   public static final String PROP_SESSION_HOST = "minecraft.api.session.host";
   public static final String PROP_SERVICES_HOST = "minecraft.api.services.host";

   public static void setEnvironmentOverride(String override) {
      environmentOverride = override;
   }

   public static Optional<Environment> getEnvironmentFromProperties() {
      String envName = environmentOverride != null ? environmentOverride : System.getProperty("minecraft.api.env");
      Optional var10000 = YggdrasilEnvironment.fromString(envName);
      Environment.class.getClass();
      Optional<Environment> env = var10000.map(Environment.class::cast);
      return env.isPresent() ? env : fromHostNames();
   }

   private static Optional<Environment> fromHostNames() {
      String auth = System.getProperty("minecraft.api.auth.host");
      String account = System.getProperty("minecraft.api.account.host");
      String session = System.getProperty("minecraft.api.session.host");
      String services = System.getProperty("minecraft.api.services.host");
      if (auth != null && account != null && session != null) {
         return Optional.of(Environment.create(auth, account, session, services, "properties"));
      } else {
         if (auth != null || account != null || session != null) {
            LOGGER.info("Ignoring hosts properties. All need to be set: " + Arrays.asList("minecraft.api.auth.host", "minecraft.api.account.host", "minecraft.api.session.host"));
         }

         return Optional.empty();
      }
   }
}
