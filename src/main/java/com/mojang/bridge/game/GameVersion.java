package com.mojang.bridge.game;

import java.util.Date;

public interface GameVersion {
   String getId();

   String getName();

   String getReleaseTarget();

   int getWorldVersion();

   int getProtocolVersion();

   /** @deprecated */
   @Deprecated
   default int getPackVersion() {
      return this.getPackVersion(PackType.RESOURCE);
   }

   default int getPackVersion(PackType packType) {
      return this.getPackVersion();
   }

   Date getBuildTime();

   boolean isStable();
}
