package com.mojang.bridge.launcher;

import com.mojang.bridge.game.GameSession;

public interface SessionEventListener {
   SessionEventListener NONE = new SessionEventListener() {
   };

   default void onStartGameSession(GameSession session) {
   }

   default void onLeaveGameSession(GameSession session) {
   }
}
