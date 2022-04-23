package com.mojang.authlib.yggdrasil;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.util.UUIDTypeAdapter;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilMinecraftSessionService extends HttpMinecraftSessionService {
   private static final String[] ALLOWED_DOMAINS = new String[]{".minecraft.net", ".mojang.com"};
   private static final String[] BLOCKED_DOMAINS = new String[]{"education.minecraft.net", "bugs.mojang.com"};
   private static final Logger LOGGER = LogManager.getLogger();
   private final String baseUrl;
   private final URL joinUrl;
   private final URL checkUrl;
   private final PublicKey publicKey;
   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
   private final LoadingCache<GameProfile, GameProfile> insecureProfiles;

   protected YggdrasilMinecraftSessionService(YggdrasilAuthenticationService service, Environment env) {
      super(service);
      this.insecureProfiles = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build(new CacheLoader<GameProfile, GameProfile>() {
         public GameProfile load(GameProfile key) throws Exception {
            return YggdrasilMinecraftSessionService.this.fillGameProfile(key, false);
         }
      });
      this.baseUrl = env.getSessionHost() + "/session/minecraft/";
      this.joinUrl = HttpAuthenticationService.constantURL(this.baseUrl + "join");
      this.checkUrl = HttpAuthenticationService.constantURL(this.baseUrl + "hasJoined");

      try {
         X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(YggdrasilMinecraftSessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         this.publicKey = keyFactory.generatePublic(spec);
      } catch (Exception var5) {
         throw new Error("Missing/invalid yggdrasil public key!");
      }
   }

   public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
      JoinMinecraftServerRequest request = new JoinMinecraftServerRequest();
      request.accessToken = authenticationToken;
      request.selectedProfile = profile.getId();
      request.serverId = serverId;
      this.getAuthenticationService().makeRequest(this.joinUrl, request, Response.class);
   }

   public GameProfile hasJoinedServer(GameProfile user, String serverId, InetAddress address) throws AuthenticationUnavailableException {
      Map<String, Object> arguments = new HashMap();
      arguments.put("username", user.getName());
      arguments.put("serverId", serverId);
      if (address != null) {
         arguments.put("ip", address.getHostAddress());
      }

      URL url = HttpAuthenticationService.concatenateURL(this.checkUrl, HttpAuthenticationService.buildQuery(arguments));

      try {
         HasJoinedMinecraftServerResponse response = this.getAuthenticationService().makeRequest(url, null, HasJoinedMinecraftServerResponse.class);
         if (response != null && response.getId() != null) {
            GameProfile result = new GameProfile(response.getId(), user.getName());
            if (response.getProperties() != null) {
               result.getProperties().putAll(response.getProperties());
            }

            return result;
         } else {
            return null;
         }
      } catch (AuthenticationUnavailableException var8) {
         throw var8;
      } catch (AuthenticationException var9) {
         return null;
      }
   }

   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
      Property textureProperty = (Property)Iterables.getFirst(profile.getProperties().get("textures"), (Object)null);
      if (textureProperty == null) {
         return new HashMap();
      } else {
         if (requireSecure) {
            if (!textureProperty.hasSignature()) {
               LOGGER.error("Signature is missing from textures payload");
               throw new InsecureTextureException("Signature is missing from textures payload");
            }

            if (!textureProperty.isSignatureValid(this.publicKey)) {
               LOGGER.error("Textures payload has been tampered with (signature invalid)");
               throw new InsecureTextureException("Textures payload has been tampered with (signature invalid)");
            }
         }

         MinecraftTexturesPayload result;
         try {
            String json = new String(Base64.decodeBase64(textureProperty.getValue()), StandardCharsets.UTF_8);
            result = this.gson.fromJson(json, MinecraftTexturesPayload.class);
         } catch (JsonParseException var8) {
            LOGGER.error("Could not decode textures payload", var8);
            return new HashMap();
         }

         if (result != null && result.getTextures() != null) {
            Iterator<Entry<MinecraftProfileTexture.Type, MinecraftProfileTexture>> var9 = result.getTextures().entrySet().iterator();

            String url;
            do {
               if (!var9.hasNext()) {
                  return result.getTextures();
               }

               Entry<MinecraftProfileTexture.Type, MinecraftProfileTexture> entry = var9.next();
               url = entry.getValue().getUrl();
            } while(isAllowedTextureDomain(url));

            LOGGER.error("Textures payload contains blocked domain: {}", url);
         }
         return new HashMap();
      }
   }

   public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
      if (profile.getId() == null) {
         return profile;
      } else {
         return !requireSecure ? this.insecureProfiles.getUnchecked(profile) : this.fillGameProfile(profile, true);
      }
   }

   protected GameProfile fillGameProfile(GameProfile profile, boolean requireSecure) {
      try {
         URL url = HttpAuthenticationService.constantURL(this.baseUrl + "profile/" + UUIDTypeAdapter.fromUUID(profile.getId()));
         url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + !requireSecure);
         MinecraftProfilePropertiesResponse response = this.getAuthenticationService().makeRequest(url, null, MinecraftProfilePropertiesResponse.class);
         if (response == null) {
            LOGGER.debug("Couldn't fetch profile properties for " + profile + " as the profile does not exist");
            return profile;
         } else {
            GameProfile result = new GameProfile(response.getId(), response.getName());
            result.getProperties().putAll(response.getProperties());
            profile.getProperties().putAll(response.getProperties());
            LOGGER.debug("Successfully fetched profile properties for " + profile);
            return result;
         }
      } catch (AuthenticationException var6) {
         LOGGER.warn("Couldn't look up profile properties for " + profile, var6);
         return profile;
      }
   }

   public YggdrasilAuthenticationService getAuthenticationService() {
      return (YggdrasilAuthenticationService)super.getAuthenticationService();
   }

   private static boolean isAllowedTextureDomain(String url) {
      URI uri;
      try {
         uri = new URI(url);
      } catch (URISyntaxException var3) {
         throw new IllegalArgumentException("Invalid URL '" + url + "'");
      }

      String domain = uri.getHost();
      return isDomainOnList(domain, ALLOWED_DOMAINS) && !isDomainOnList(domain, BLOCKED_DOMAINS);
   }

   private static boolean isDomainOnList(String domain, String[] list) {
      String[] var2 = list;
      int var3 = list.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String entry = var2[var4];
         if (domain.endsWith(entry)) {
            return true;
         }
      }

      return false;
   }
}
