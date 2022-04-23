package com.mojang.authlib;

import java.util.StringJoiner;

public interface Environment {
   String getAuthHost();

   String getAccountsHost();

   String getSessionHost();

   String getServicesHost();

   String getName();

   String asString();

   static Environment create(final String auth, final String account, final String session, final String services, final String name) {
      return new Environment() {
         public String getAuthHost() {
            return auth;
         }

         public String getAccountsHost() {
            return account;
         }

         public String getSessionHost() {
            return session;
         }

         public String getServicesHost() {
            return services;
         }

         public String getName() {
            return name;
         }

         public String asString() {
            return (new StringJoiner(", ", "", "")).add("authHost='" + this.getAuthHost() + "'").add("accountsHost='" + this.getAccountsHost() + "'").add("sessionHost='" + this.getSessionHost() + "'").add("servicesHost='" + this.getServicesHost() + "'").add("name='" + this.getName() + "'").toString();
         }
      };
   }
}
