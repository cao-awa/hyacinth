package com.mojang.authlib.yggdrasil.request;

public class ValidateRequest {
   private String clientToken;
   private String accessToken;

   public ValidateRequest(String accessToken, String clientToken) {
      this.clientToken = clientToken;
      this.accessToken = accessToken;
   }
}
