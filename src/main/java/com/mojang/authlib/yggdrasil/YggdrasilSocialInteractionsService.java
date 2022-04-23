package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Environment;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import com.mojang.authlib.yggdrasil.response.PrivilegesResponse;
import java.net.Proxy;
import java.net.URL;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class YggdrasilSocialInteractionsService implements SocialInteractionsService {
   private static final long BLOCKLIST_REQUEST_COOLDOWN_SECONDS = 120L;
   private static final UUID ZERO_UUID = new UUID(0L, 0L);
   private final URL routePrivileges;
   private final URL routeBlocklist;
   private final MinecraftClient minecraftClient;
   private boolean serversAllowed;
   private boolean realmsAllowed;
   private boolean chatAllowed;
   private boolean telemetryAllowed;
   @Nullable
   private Instant nextAcceptableBlockRequest;
   @Nullable
   private Set<UUID> blockList;

   public YggdrasilSocialInteractionsService(String accessToken, Proxy proxy, Environment env) throws AuthenticationException {
      this.minecraftClient = new MinecraftClient(accessToken, proxy);
      this.routePrivileges = HttpAuthenticationService.constantURL(env.getServicesHost() + "/privileges");
      this.routeBlocklist = HttpAuthenticationService.constantURL(env.getServicesHost() + "/privacy/blocklist");
      this.checkPrivileges();
   }

   public boolean serversAllowed() {
      return this.serversAllowed;
   }

   public boolean realmsAllowed() {
      return this.realmsAllowed;
   }

   public boolean chatAllowed() {
      return this.chatAllowed;
   }

   public boolean telemetryAllowed() {
      return this.telemetryAllowed;
   }

   public boolean isBlockedPlayer(UUID playerID) {
      if (playerID.equals(ZERO_UUID)) {
         return false;
      } else {
         if (this.blockList == null) {
            this.blockList = this.fetchBlockList();
            if (this.blockList == null) {
               return false;
            }
         }

         return this.blockList.contains(playerID);
      }
   }

   @Nullable
   private Set<UUID> fetchBlockList() {
      if (this.nextAcceptableBlockRequest != null && this.nextAcceptableBlockRequest.isAfter(Instant.now())) {
         return null;
      } else {
         this.nextAcceptableBlockRequest = Instant.now().plusSeconds(120L);

         try {
            BlockListResponse response = (BlockListResponse)this.minecraftClient.get(this.routeBlocklist, BlockListResponse.class);
            return response.getBlockedProfiles();
         } catch (MinecraftClientHttpException var2) {
            return null;
         } catch (MinecraftClientException var3) {
            return null;
         }
      }
   }

   private void checkPrivileges() throws AuthenticationException {
      try {
         PrivilegesResponse response = (PrivilegesResponse)this.minecraftClient.get(this.routePrivileges, PrivilegesResponse.class);
         this.chatAllowed = (Boolean)response.getPrivileges().getOnlineChat().map(PrivilegesResponse.Privileges.Privilege::isEnabled).orElse(false);
         this.serversAllowed = (Boolean)response.getPrivileges().getMultiplayerServer().map(PrivilegesResponse.Privileges.Privilege::isEnabled).orElse(false);
         this.realmsAllowed = (Boolean)response.getPrivileges().getMultiplayerRealms().map(PrivilegesResponse.Privileges.Privilege::isEnabled).orElse(false);
         this.telemetryAllowed = (Boolean)response.getPrivileges().getTelemetry().map(PrivilegesResponse.Privileges.Privilege::isEnabled).orElse(false);
      } catch (MinecraftClientHttpException var2) {
         throw var2.toAuthenticationException();
      } catch (MinecraftClientException var3) {
         throw var3.toAuthenticationException();
      }
   }
}
