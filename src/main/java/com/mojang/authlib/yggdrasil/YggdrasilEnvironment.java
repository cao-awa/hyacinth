package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Environment;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum YggdrasilEnvironment implements Environment {
   PROD("https://authserver.mojang.com", "https://api.mojang.com", "https://sessionserver.mojang.com", "https://api.minecraftservices.com"),
   STAGING("https://yggdrasil-auth-staging.mojang.com", "https://api-staging.mojang.com", "https://yggdrasil-auth-session-staging.mojang.zone", "https://api-staging.minecraftservices.com");

   private final String authHost;
   private final String accountsHost;
   private final String sessionHost;
   private final String servicesHost;

   private YggdrasilEnvironment(String authHost, String accountsHost, String sessionHost, String servicesHost) {
      this.authHost = authHost;
      this.accountsHost = accountsHost;
      this.sessionHost = sessionHost;
      this.servicesHost = servicesHost;
   }

   public String getAuthHost() {
      return this.authHost;
   }

   public String getAccountsHost() {
      return this.accountsHost;
   }

   public String getSessionHost() {
      return this.sessionHost;
   }

   public String getServicesHost() {
      return this.servicesHost;
   }

   public String getName() {
      return this.name();
   }

   public String asString() {
      return (new StringJoiner(", ", "", "")).add("authHost='" + this.authHost + "'").add("accountsHost='" + this.accountsHost + "'").add("sessionHost='" + this.sessionHost + "'").add("servicesHost='" + this.servicesHost + "'").add("name='" + this.getName() + "'").toString();
   }

   public static Optional<YggdrasilEnvironment> fromString(@Nullable String value) {
      return Stream.of(values()).filter((env) -> {
         return value != null && value.equalsIgnoreCase(env.name());
      }).findFirst();
   }
}
