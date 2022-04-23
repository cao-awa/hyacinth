package com.mojang.authlib.yggdrasil.response;

import java.util.Optional;

public class PrivilegesResponse extends Response {
   private PrivilegesResponse.Privileges privileges = new PrivilegesResponse.Privileges();

   public PrivilegesResponse.Privileges getPrivileges() {
      return this.privileges;
   }

   public class Privileges {
      private PrivilegesResponse.Privileges.Privilege onlineChat = new PrivilegesResponse.Privileges.Privilege();
      private PrivilegesResponse.Privileges.Privilege multiplayerServer = new PrivilegesResponse.Privileges.Privilege();
      private PrivilegesResponse.Privileges.Privilege multiplayerRealms = new PrivilegesResponse.Privileges.Privilege();
      private PrivilegesResponse.Privileges.Privilege telemetry = new PrivilegesResponse.Privileges.Privilege();

      public Optional<PrivilegesResponse.Privileges.Privilege> getOnlineChat() {
         return Optional.ofNullable(this.onlineChat);
      }

      public Optional<PrivilegesResponse.Privileges.Privilege> getMultiplayerServer() {
         return Optional.ofNullable(this.multiplayerServer);
      }

      public Optional<PrivilegesResponse.Privileges.Privilege> getMultiplayerRealms() {
         return Optional.ofNullable(this.multiplayerRealms);
      }

      public Optional<PrivilegesResponse.Privileges.Privilege> getTelemetry() {
         return Optional.ofNullable(this.telemetry);
      }

      public class Privilege {
         private boolean enabled;

         public boolean isEnabled() {
            return this.enabled;
         }
      }
   }
}
