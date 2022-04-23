package com.mojang.authlib.exceptions;

import com.mojang.authlib.yggdrasil.response.ErrorResponse;
import java.util.Optional;
import java.util.StringJoiner;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class MinecraftClientHttpException extends MinecraftClientException {
   public static final int UNAUTHORIZED = 401;
   public static final int FORBIDDEN = 403;
   private final int status;
   @Nullable
   private final ErrorResponse response;

   public MinecraftClientHttpException(int status) {
      super(MinecraftClientException.ErrorType.HTTP_ERROR, getErrorMessage(status, (ErrorResponse)null));
      this.status = status;
      this.response = null;
   }

   public MinecraftClientHttpException(int status, ErrorResponse response) {
      super(MinecraftClientException.ErrorType.HTTP_ERROR, getErrorMessage(status, response));
      this.status = status;
      this.response = response;
   }

   public int getStatus() {
      return this.status;
   }

   public Optional<ErrorResponse> getResponse() {
      return Optional.ofNullable(this.response);
   }

   public String toString() {
      return (new StringJoiner(", ", MinecraftClientHttpException.class.getSimpleName() + "[", "]")).add("type=" + this.type).add("status=" + this.status).add("response=" + this.response).toString();
   }

   public AuthenticationException toAuthenticationException() {
      if (!this.hasError("InsufficientPrivilegesException") && this.status != 403) {
         if (this.status == 401) {
            return new InvalidCredentialsException(this.getMessage(), this);
         } else {
            return (AuthenticationException)(this.status >= 500 ? new AuthenticationUnavailableException(this.getMessage(), this) : new AuthenticationException(this.getMessage(), this));
         }
      } else {
         return new InsufficientPrivilegesException(this.getMessage(), this);
      }
   }

   private Optional<String> getError() {
      return this.getResponse().map(ErrorResponse::getError).filter(StringUtils::isNotEmpty);
   }

   private static String getErrorMessage(int status, ErrorResponse response) {
      String errorMessage;
      if (response != null) {
         if (StringUtils.isNotEmpty(response.getErrorMessage())) {
            errorMessage = response.getErrorMessage();
         } else if (StringUtils.isNotEmpty(response.getError())) {
            errorMessage = response.getError();
         } else {
            errorMessage = "Status: " + status;
         }
      } else {
         errorMessage = "Status: " + status;
      }

      return errorMessage;
   }

   private boolean hasError(String error) {
      return this.getError().filter((value) -> {
         return value.equalsIgnoreCase(error);
      }).isPresent();
   }
}
