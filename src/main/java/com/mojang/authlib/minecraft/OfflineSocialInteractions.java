package com.mojang.authlib.minecraft;

import java.util.UUID;

public class OfflineSocialInteractions implements SocialInteractionsService {
   public boolean serversAllowed() {
      return true;
   }

   public boolean realmsAllowed() {
      return true;
   }

   public boolean chatAllowed() {
      return true;
   }

   public boolean telemetryAllowed() {
      return false;
   }

   public boolean isBlockedPlayer(UUID playerID) {
      return false;
   }
}
